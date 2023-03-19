#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.NotImplementedException;

/**
 * This class is a wrapper around HttpServletRequestWrapper, so we can read the request body
 * multiple times.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/09/05
 */
public class RequestWrapper extends HttpServletRequestWrapper {

  private ByteArrayOutputStream cachedBytes;

  public RequestWrapper(HttpServletRequest request) {
    super(request);
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    if (cachedBytes == null) {
      cacheInputStream();
    }

    return new CachedServletInputStream(cachedBytes.toByteArray());
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(getInputStream()));
  }

  private void cacheInputStream() throws IOException {
    cachedBytes = new ByteArrayOutputStream();
    IOUtils.copy(super.getInputStream(), cachedBytes);
  }

  public String getPayLoad() throws IOException {
    return IOUtils.toString(this.getInputStream(), this.getCharacterEncoding());
  }

  public Map<String, String> getRequestHeaders() {
    final HttpServletRequest request = (HttpServletRequest) this.getRequest();
    Map<String, String> headers = new HashMap<>();
    Collections.list(request.getHeaderNames())
        .forEach(key -> headers.put(key, request.getHeader(key)));
    return headers;
  }

  private static class CachedServletInputStream extends ServletInputStream {

    private final ByteArrayInputStream buffer;

    public CachedServletInputStream(byte[] contents) {
      this.buffer = new ByteArrayInputStream(contents);
    }

    @Override
    public int read() throws IOException {
      return buffer.read();
    }

    @Override
    public boolean isFinished() {
      return buffer.available() == 0;
    }

    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
      throw new NotImplementedException("Not implemented");
    }
  }

}
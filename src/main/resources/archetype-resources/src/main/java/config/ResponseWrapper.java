#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang3.NotImplementedException;

/**
 * This class is a wrapper around HttpServletResponseWrapper, so we can read the response body
 * multiple times.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/09/05
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

  private ServletOutputStreamWrapper servletOutputStreamWrapper;

  private PrintWriter printWriter;

  private ByteArrayOutputStream bos;

  public ResponseWrapper(HttpServletResponse response) {
    super(response);
    bos = new ByteArrayOutputStream();
  }

  public String getResponseBody() {
    return bos.toString();
  }

  public Map<String, String> getResponseHeaders() {
    Map<String, String> headers = new HashMap<>();
    HttpServletResponse response = (HttpServletResponse) this.getResponse();
    response.getHeaderNames().forEach(key -> headers.put(key, response.getHeader(key)));
    return headers;
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    if (printWriter == null) {
      printWriter = new PrintWriter(new OutputStreamWriter(super.getOutputStream()));
    }
    return printWriter;
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    if (servletOutputStreamWrapper == null) {
      bos = new ByteArrayOutputStream();
      servletOutputStreamWrapper = new ServletOutputStreamWrapper(super.getOutputStream(),
          bos);
    }
    return servletOutputStreamWrapper;
  }

  @Override
  public void flushBuffer() throws IOException {
    if (servletOutputStreamWrapper != null) {
      servletOutputStreamWrapper.flush();
    }
    if (printWriter != null) {
      printWriter.flush();
    }
  }

  private static class ServletOutputStreamWrapper extends ServletOutputStream {

    private final TeeOutputStream teeOutputStream;

    public ServletOutputStreamWrapper(OutputStream firstStream, OutputStream secondStream) {
      teeOutputStream = new TeeOutputStream(firstStream, secondStream);
    }

    @Override
    public void write(int arg0) throws IOException {
      teeOutputStream.write(arg0);
    }

    @Override
    public void flush() throws IOException {
      super.flush();
      teeOutputStream.flush();
    }

    @Override
    public void close() throws IOException {
      super.close();
      teeOutputStream.close();
    }

    @Override
    public boolean isReady() {
      return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
      throw new NotImplementedException("Not yet implemented");
    }
  }
}

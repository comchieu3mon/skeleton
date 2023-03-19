#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * This class used for web client config.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/08/18
 */

@Configuration
@Slf4j
public class WebClientConfig {

  @Bean
  public WebClient webClient() {
    SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
    HttpClient httpClient = new HttpClient(sslContextFactory) {
      @Override
      public Request newRequest(URI uri) {
        Request request = super.newRequest(uri);
        return enhance(request);
      }
    };

    return WebClient
        .builder()
        .clientConnector(new JettyClientHttpConnector(httpClient))
        .build();
  }


  /**
   * Method that adds logging to the incoming request at each interval.
   *
   * @param inboundRequest the request to get log
   * @return enhance request
   */
  private Request enhance(Request inboundRequest) {
    StringBuilder sb = new StringBuilder();
    // Request Logging
    inboundRequest.onRequestBegin(request ->
        sb.append("Request: \n")
            .append("URI: ")
            .append(request.getURI())
            .append("\n")
            .append("Method: ")
            .append(request.getMethod()));
    inboundRequest.onRequestHeaders(request -> {
      sb.append("\nHeaders:\n");
      for (HttpField header : request.getHeaders()) {
        sb.append("\t\t").append(header.getName()).append(" : ").append(header.getValue())
            .append("\n");
      }
    });
    inboundRequest.onRequestContent((request, content) ->
        sb.append("Body: \n\t")
            .append(content.toString()));
    sb.append("\n");

    // Response Logging
    inboundRequest.onResponseBegin(response ->
        sb.append("Response:\n")
            .append("Status: ")
            .append(response.getStatus())
            .append("\n"));
    inboundRequest.onResponseHeaders(response -> {
      sb.append("Headers:\n");
      for (HttpField header : response.getHeaders()) {
        sb.append("\t\t").append(header.getName()).append(" : ").append(header.getValue())
            .append("\n");
      }
    });
    inboundRequest.onResponseContent(((response, content) -> {
      String bufferAsString = StandardCharsets.UTF_8.decode(content).toString();
      sb.append("Response Body:\n").append(bufferAsString);
    }));

    // Add actual sb invocation
    inboundRequest.onRequestSuccess(request -> log.debug(sb.toString()));
    inboundRequest.onResponseSuccess(response -> log.debug(sb.toString()));

    // Return original request
    return inboundRequest;
  }

}

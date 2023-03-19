#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import static net.logstash.logback.argument.StructuredArguments.value;

import ${package}.util.Constant;
import ${package}.util.StringUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter all requests, responses.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/07/04
 */
@Component
@Slf4j
public class RequestFilter extends OncePerRequestFilter {

  private static final String[] skipUrls = {
      "/favicon.ico",
      "/swagger-resources/**",
      "/swagger-ui.html",
      "/swagger-ui/**",
      "/v2/api-docs",
      "/v3/**",
      "/actuator/**",
      "/duccao-swagger.yml"
  };

  @Value("${logging.request.include.headers}")
  private boolean includeHeaders;

  private final Predicate<String> queryStringPredicate = Objects::nonNull;

  @Value("${logging.x-correlation-id.excludes}")
  private String patterns;

  /**
   * Filter requests, responses and add Mapped Diagnostic Context (MDC) tracking unique ID.
   *
   * @param request     {@link HttpServletRequest}
   * @param response    {@link HttpServletResponse}
   * @param filterChain {@link FilterChain}
   * @throws ServletException {@link ServletException}
   * @throws IOException      {@link IOException}
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      boolean isCorrelationEmpty = ObjectUtils.isEmpty(
          request.getHeader(Constant.X_CORRELATION_ID));
      if (Objects.isNull(patterns)) {
        patterns = StringUtils.EMPTY;
      }
      boolean hasAnyMatched = StringUtil.hasAnyPathMatch(
          Arrays.asList(StringUtils.split(patterns, Constant.PATTERN_X_CORRELATION_DELIMITER)),
          request.getServletPath());
      if (isCorrelationEmpty && !hasAnyMatched) {
        final String correlationId = UUID.randomUUID().toString();
        response.setHeader(Constant.X_CORRELATION_ID, correlationId);
        MDC.put(Constant.X_CORRELATION_ID, correlationId);
      }
      final RequestWrapper requestWrapper = new RequestWrapper(request);
      final ResponseWrapper responseWrapper = new ResponseWrapper(response);
      long startTime = System.currentTimeMillis();
      logRequest(requestWrapper);
      filterChain.doFilter(requestWrapper, responseWrapper);
      logResponse(startTime, responseWrapper);
    } finally {
      MDC.clear();
    }
  }

  private void logResponse(final long startTime, final ResponseWrapper response)
      throws IOException {
    log.info(
        "Response:\n \tduration={}, \n\tstatus={}, \n\tpayload={}, \n\theaders={}, \n\taudit={}",
        value("duration", System.currentTimeMillis() - startTime),
        response.getStatus(),
        response.getResponseBody(),
        includeHeaders ? response.getResponseHeaders() : Collections.emptyMap(),
        value("audit", true)
    );
  }

  private void logRequest(final RequestWrapper request) throws IOException {
    log.info(
        "Request:\n\tmethod={},\n\turl={},\n\tquery={},\n\tpayload={},\n\theaders={}, \n\taudit={}",
        request.getMethod(),
        request.getRequestURI(),
        queryStringPredicate.test(request.getQueryString()) ? request.getQueryString()
            : StringUtils.EMPTY,
        request.getPayLoad(),
        includeHeaders ? request.getRequestHeaders() : Collections.emptyMap(),
        value("audit", true)
    );
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    AntPathMatcher pathMatcher = new AntPathMatcher();
    return Arrays.stream(skipUrls)
        .anyMatch(url -> pathMatcher.match(url, request.getServletPath()));
  }
}

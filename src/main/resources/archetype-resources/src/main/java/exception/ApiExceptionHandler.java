#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.exception;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The {@link ApiExceptionHandler ApiExceptionHandler} allows to handle exceptions across the whole
 * application in one global handling component.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/3/30
 */
@Order(HIGHEST_PRECEDENCE)
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String ERROR_LOG_FORMAT = "URI: {}, Message: {}";

  private static final String BUSINESS_ERROR_LOG_FORMAT =
      "Business Error: URI: {}, ErrorCode: {}, Message: {}";

  private static final String TECHNICAL_ERROR_LOG_FORMAT =
      "Technical Error: URI: {}, ErrorCode: {}, Message: {}";

  private final MessageSource messageSource;

  @Value("${default-locale}")
  private String defaultLocale;

  @Value("${default-language}")
  private String defaultLanguage;

  @Override
  public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
    apiError.setMessage("The parameter is in a wrong format.");
    String uri = this.getServletPath(request);
    apiError.setPath(uri);
    log.error(ERROR_LOG_FORMAT, uri, ex.getMessage(), ex);
    return buildResponse(apiError);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstrainViolation(ConstraintViolationException exception,
      WebRequest request) {
    String path = this.getServletPath(request);
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
    apiError.setMessage("Validation error.");
    apiError.setPath(path);
    Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
    apiError.addValidationErrors(constraintViolations);
    log.error(ERROR_LOG_FORMAT, path, exception.getMessage(), exception);
    return buildResponse(apiError);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<Object> handleBusinessException(BusinessException exception,
      WebRequest request) {
    BusinessError businessError = exception.getBusinessError();
    String error = businessError.getErrorCode();
    String messageKey = businessError.getMessageKey();
    Locale locale = request.getLocale();
    String message = messageSource.getMessage(messageKey, exception.getArgs(), locale);
    String path = this.getServletPath(request);
    int status = HttpStatus.BAD_REQUEST.value();
    log.error(BUSINESS_ERROR_LOG_FORMAT, path, error, message);
    return buildResponse(status, error, message, path, LocalDateTime.now());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleNotFoundException(NotFoundException exception,
      WebRequest request) {
    BusinessError businessError = exception.getBusinessError();
    String error = businessError.getErrorCode();
    String messageKey = businessError.getMessageKey();
    Locale locale = request.getLocale();
    String message = messageSource.getMessage(messageKey, exception.getArgs(), locale);
    String path = this.getServletPath(request);
    int status = HttpStatus.NOT_FOUND.value();
    log.error(BUSINESS_ERROR_LOG_FORMAT, path, error, message);
    return buildResponse(status, error, message, path, LocalDateTime.now());
  }

  @ExceptionHandler(TechnicalException.class)
  public ResponseEntity<Object> handleTechnicalException(TechnicalException exception,
      WebRequest request) {
    TechnicalError technicalError = exception.getTechnicalError();
    String error = technicalError.getErrorCode();
    String messageKey = technicalError.getMessageKey();
    Locale locale = request.getLocale();
    String message = messageSource.getMessage(messageKey, exception.getArgs(), locale);
    String path = this.getServletPath(request);
    int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    log.error(TECHNICAL_ERROR_LOG_FORMAT, this.getServletPath(request), error, message);
    return buildResponse(status, error, message, path, LocalDateTime.now());
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<Object> handleDataAccessException(DataAccessException ex,
      WebRequest request) {
    String message = ex.getMessage();
    String path = this.getServletPath(request);
    int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    log.error(TECHNICAL_ERROR_LOG_FORMAT, this.getServletPath(request), ex.getRootCause(), message);
    return buildResponse(status, ex.getMessage(), message, path, LocalDateTime.now());
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex, HttpHeaders httpHeaders, HttpStatus httpStatus,
      WebRequest request) {
    return buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex, request);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
      HttpHeaders headers, HttpStatus httpStatus, WebRequest request) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ex, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    return buildResponse(HttpStatus.NOT_ACCEPTABLE, ex, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex, request);
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(
      ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
    apiError.setMessage("Validation error.");
    String uri = this.getServletPath(request);
    apiError.addValidationErrors(ex.getFieldErrors());
    apiError.setPath(uri);
    log.error(ERROR_LOG_FORMAT, uri, ex.getMessage(), ex);
    return buildResponse(apiError);
  }

  private ResponseEntity<Object> buildResponse(ApiError apiError) {
    return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
  }

  private ResponseEntity<Object> buildResponse(HttpStatus status, Exception ex,
      WebRequest request) {
    String message = ex.getMessage();
    String uri = this.getServletPath(request);
    log.error(ERROR_LOG_FORMAT, uri, message, ex);
    final ApiError apiError = new ApiError(status, message, uri, ex);
    return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
  }

  private ResponseEntity<Object> buildResponse(int status, String error, String message,
      String path, LocalDateTime timeStamp) {
    ApiError apiError = new ApiError(status, error, message, path, timeStamp);
    return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
  }

  private String getServletPath(WebRequest webRequest) {
    ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
    return servletRequest.getRequest().getServletPath();
  }
}

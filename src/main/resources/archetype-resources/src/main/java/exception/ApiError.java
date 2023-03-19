#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.thg.subscriptions.util.Constant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintViolation;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

/**
 * Represents an API response entity, consisting of status, timeStamp, error, message, path,
 * subErrors.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/3/30
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ApiError {

  private int status;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_TIME_FORMAT)
  private LocalDateTime timeStamp;
  private String error;
  @JsonIgnore
  private String exception;
  private String message;
  private String path;
  private List<ValidationError> subErrors;

  private ApiError() {
    timeStamp = LocalDateTime.now();
  }

  public ApiError(HttpStatus status) {
    this();
    this.status = status.value();
    this.error = status.name();
  }

  public ApiError(HttpStatus status, String message) {
    this();
    this.status = status.value();
    this.message = message;
    this.error = status.name();
  }

  public ApiError(int status, String error, String message, String path, LocalDateTime timeStamp) {
    this();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
    this.timeStamp = timeStamp;
  }

  public ApiError(HttpStatus status, String message, String path) {
    this(status, message);
    this.path = path;
  }

  public ApiError(HttpStatus status, String message, String path, Throwable ex) {
    this(status, message, path);
    this.exception = ex.getClass().getName();
  }

  public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
    constraintViolations.forEach(this::addValidationError);
  }

  public void addValidationErrors(List<FieldError> fieldErrors) {
    fieldErrors.forEach(
        fieldError ->
            this.addValidationError(fieldError.getField(), fieldError.getDefaultMessage()));
  }

  private void addSubError(ValidationError validationError) {
    if (Objects.isNull(subErrors)) {
      subErrors = new ArrayList<>();
    }
    subErrors.add(validationError);
  }

  private void addValidationError(ConstraintViolation<?> constraintViolation) {
    this.addValidationError(
        ((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().asString(),
        constraintViolation.getMessage());
  }

  private void addValidationError(String field, String message) {
    this.addSubError(new ValidationError(field, message));
  }
}

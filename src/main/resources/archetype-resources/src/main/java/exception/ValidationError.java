#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Describes a validation violation.
 *
 * @author duccaom
 *
 * @version 1.0
 * @since 2022/3/30
 */

@Getter
@Setter
@RequiredArgsConstructor
public class ValidationError {

  private final String field;

  private final String message;
}

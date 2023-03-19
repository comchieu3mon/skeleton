#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Throw when business rule is violated.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/3/30
 */

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {

  private final BusinessError businessError;

}

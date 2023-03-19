#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The {@link TechnicalException TechnicalException} allows to handle technical exceptions.
 *
 * @author duccaom
 */

@Getter
@RequiredArgsConstructor
public class TechnicalException extends RuntimeException {

}

#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.exception;

import lombok.Getter;

/**
 * The class handles NotFoundException.
 *
 * @author duccaom
 */

@Getter
public class NotFoundException extends BusinessException {

}



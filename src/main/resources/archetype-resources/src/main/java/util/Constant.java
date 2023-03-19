#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import java.time.ZoneId;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

/**
 * The class defines all constants of application.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/04/22
 */
@UtilityClass
public class Constant {

  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT = "yyyy-MM-dd";
}

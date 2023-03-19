#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PatternMatchUtils;

/**
 * This class helps to provide string parsing utility.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/05/19
 */
@UtilityClass
public class StringUtil {

  /*
   * This function is to add escape for all special character in sql query.
   *
   * @param string to be parse
   *
   * @return string formatted with pattern
   */
  public static String replaceSpecialCharacter(@NonNull String inputString) {
    final String[] metaCharacters = {"\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+",
        "?", "|", "<", ">", "-", "&", "%", "_"};
    for (int i = 0; i < metaCharacters.length; i++) {
      inputString = inputString.replace(metaCharacters[i], "\\" + metaCharacters[i]);
    }
    return inputString;
  }

  /**
   * Process valid search key.
   *
   * @param searchKey searchKey
   * @return valid searchKey
   */
  public static String processValidSearchKey(String searchKey) {
    if (StringUtils.isEmpty(searchKey)) {
      return null;
    } else {
      String validSearchKey = StringUtil.replaceSpecialCharacter(searchKey);
      return validSearchKey.toLowerCase().trim();
    }
  }

  /**
   * String concat elements.
   *
   * @param strings strings list
   * @return String concat elements
   */
  public static String concat(String... strings) {
    if (ArrayUtils.isEmpty(strings)) {
      return StringUtils.EMPTY;
    } else {
      StringBuilder builder = new StringBuilder(strings[0]);
      for (int i = 1; i < strings.length; i++) {
        builder.append(strings[i]);
      }
      return builder.toString();
    }
  }

  /**
   * Checking the path has match with any provided patterns.
   *
   * @param patterns {@see List}
   * @param path     {@link String}
   * @return true if path matched with any pattern
   * @see AntPathMatcher
   */
  public static boolean hasAnyPathMatch(List<String> patterns, String path) {
    if (ObjectUtils.isNotEmpty(patterns)) {
      final List<String> stringList = patterns.stream().map(StringUtils::normalizeSpace)
          .collect(Collectors.toList());
      return PatternMatchUtils.simpleMatch(stringList.toArray(new String[0]), path);
    }
    return false;
  }
}

#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * This class uses for application config.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/4/20
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

//  @Value("${error-message-classpath.business}")
  private String businessErrorPath;

//  @Value("${error-message-classpath.technical}")
  private String technicalErrorPath;

//  @Value("${message-classpath.response}")
  private String responsePath;

//  @Value("${default-encoding}")
  private String defaultEncoding;

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = 
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames(businessErrorPath, technicalErrorPath, responsePath);
    messageSource.setDefaultEncoding(defaultEncoding);
    return messageSource;
  }

  @Bean
  public LocalValidatorFactoryBean getValidator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }
}

#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thg.support.testing.messaging.producer.JsonMessageProducer;
import com.thg.support.testing.messaging.producer.XmlMessageProducer;
import javax.jms.ConnectionFactory;
import javax.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/**
 * Message Queue config class.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/11/04
 */
@Configuration
@EnableJms
public class MessageQueueConfig {

  @Value("${activemq.topic.external.create.subscription}")
  private String topicExternalCreateSubscription;

  @Value("${activemq.topic.external.receive.order.invoice-event}")
  private String topicReceiveInvoiceSuccessEvent;

  @Value("${activemq.topic.external.receive.order.dispatch-event}")
  private String topicReceiveDespatchEvent;

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Bean("jmsTransactionalContainerFactory")
  @Autowired
  DefaultJmsListenerContainerFactory provideJmsTransactionalContainerFactory(
      ConnectionFactory connectionFactory) {

    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

    factory.setConnectionFactory(connectionFactory);
    factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
    factory.setPubSubDomain(true);

    return factory;
  }

  /**
   * Json message converter.
   *
   * @return MessageConverter for Json type.
   */
  @Bean
  public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    converter.setObjectMapper(objectMapper);
    return converter;
  }

  /**
   * Create jms Template for json type.
   *
   * @param connectionFactory          connectionFactory
   * @param jacksonJmsMessageConverter jacksonJmsMessageConverter
   * @return JmsTemplate
   */
  @Bean
  @Primary
  public JmsTemplate jmsTemplate(
      @Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory,
      MessageConverter jacksonJmsMessageConverter) {
    JmsTemplate jmsTemplate = new JmsTemplate();
    jmsTemplate.setMessageConverter(jacksonJmsMessageConverter);
    jmsTemplate.setConnectionFactory(connectionFactory);
    jmsTemplate.setPubSubDomain(true);
    return jmsTemplate;
  }

  @Bean("subscriptionMessageProducer")
  JsonMessageProducer<SubscriptionFromOrderManagerPayload> provideCreateSubscriptionMessageProducer(
      JmsTemplate jmsTemplate,
      ObjectMapper objectMapper) {
    return new JsonMessageProducer<>(topicExternalCreateSubscription, jmsTemplate, objectMapper);
  }

  @Bean("invoiceSuccessEventMessageProducer")
  XmlMessageProducer<InvoiceSuccessEvent> provideCreateInvoiceSuccessEventMessageProducer(
      JmsTemplate jmsTemplate,
      ObjectMapper objectMapper) {
    return new XmlMessageProducer<>(topicReceiveInvoiceSuccessEvent, jmsTemplate);
  }

  @Bean("despatchEventMessageProducer")
  XmlMessageProducer<DespatchEvent> provideDespatchEventMessageProducer(
      JmsTemplate jmsTemplate,
      ObjectMapper objectMapper) {
    return new XmlMessageProducer<>(topicReceiveDespatchEvent, jmsTemplate);
  }
}
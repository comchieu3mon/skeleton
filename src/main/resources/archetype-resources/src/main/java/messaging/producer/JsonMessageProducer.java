#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.messaging.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thg.support.testing.exception.TechnicalException;
import javax.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;

/**
 * Generic Message Producer to produce jms message.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/11/04
 */
@Slf4j
@RequiredArgsConstructor
public class JsonMessageProducer<T> implements AbstractMessageProducer<T> {

  private final String destination;

  private final JmsTemplate jmsTemplate;

  private final ObjectMapper objectMapper;


  @Override
  public void send(T message, String correlationId) {
    jmsTemplate.send(destination, session -> {
      try {
        TextMessage msg = session.createTextMessage(objectMapper.writeValueAsString(message));
        msg.setStringProperty("jms_correlation_id", correlationId);
        return msg;
      } catch (JsonProcessingException jsonProcessingException) {
        log.error("Cannot process json payload {}", jsonProcessingException.getMessage());
        throw new TechnicalException(jsonProcessingException, jsonProcessingException.getMessage());
      }
    });
  }
}

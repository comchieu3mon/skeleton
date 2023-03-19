#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.messaging.producer;

import com.thg.support.testing.exception.TechnicalException;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author duccaom
 * @version 1.0
 * @since 2022/11/16
 */
@Slf4j
@RequiredArgsConstructor
public class XmlMessageProducer<T> implements AbstractMessageProducer<T> {

  private final String destination;

  private final JmsTemplate jmsTemplate;

  @Override
  public void send(T message, String correlationId) {
    jmsTemplate.send(destination, session -> {
      try {
        TextMessage msg = session.createTextMessage((String) message);
        msg.setStringProperty("jms_correlation_id", correlationId);
        return msg;
      } catch (JMSException jmsException) {
        log.error("Cannot create xml payload {}", jmsException.getMessage());
        throw new TechnicalException(jmsException, jmsException.getMessage());
      }
    });
  }
}

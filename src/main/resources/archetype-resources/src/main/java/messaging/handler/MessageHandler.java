#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.messaging.handler;

import javax.jms.JMSException;
import org.springframework.stereotype.Component;

/**
 * Generic message handler.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/11/05
 */
@Component
public abstract class MessageHandler<T> {

  protected T message;

  public void setMessage(T t) {
    this.message = t;
  }

  public T getMessage() {
    return message;
  }

  protected abstract void handle(T message) throws JMSException;

  protected abstract <V> V convert() throws JMSException;

  protected abstract void validate(T t);
}
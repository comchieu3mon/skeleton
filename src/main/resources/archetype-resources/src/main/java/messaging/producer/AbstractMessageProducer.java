#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.messaging.producer;

public interface AbstractMessageProducer<T> {

  void send(T message, String correlationId);
}
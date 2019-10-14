package api;

import java.util.HashMap;
import jua.Interpreter;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import util.BufferedChannel;

public class REPLSocketHandler extends AbstractWebSocketHandler {

  HashMap<WebSocketSession, Interpreter> sessions = new HashMap<>();

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    var interpreter = sessions.get(session);
    message
        .getPayload()
        .chars()
        .forEach(
            ch -> {
              try {
                interpreter.getIn().add((char) ch);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    var interpreter = new Interpreter(new BufferedChannel<>(), new WebSocketOutputStream(session));
    sessions.put(session, interpreter);
    interpreter.start(true);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    super.afterConnectionClosed(session, status);
    var interpreter = sessions.get(session);
    interpreter.getIn().add('\0');
    sessions.remove(session);
  }
}

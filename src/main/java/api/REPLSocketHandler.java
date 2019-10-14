package api;

import jua.Interpreter;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import util.BufferedChannel;

import java.util.ArrayList;
import java.util.HashMap;

public class REPLSocketHandler extends AbstractWebSocketHandler {

  HashMap<WebSocketSession, Interpreter> sessions = new HashMap<>();

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    System.out.printf("handle text message: %s\n", message.getPayload());
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
    System.out.printf("new session: %s", session);
    var interpreter = new Interpreter(new BufferedChannel<>(), new WebSocketOutputStream(session));
    sessions.put(session, interpreter);
    interpreter.start(true);
    System.out.println("after connection end");
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    System.out.println("connection closed");
    super.afterConnectionClosed(session, status);
    var interpreter = sessions.get(session);
    interpreter.getIn().add('\0');
    sessions.remove(session);
    System.out.println("connection closed end");
  }
}

package api;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class WebSocketWriter {
  WebSocketSession session;

  public WebSocketWriter(WebSocketSession session) {
    this.session = session;
  }

  public void write(String payload) throws IOException {
    TextMessage message = new TextMessage(payload);
    synchronized (session) {
      session.sendMessage(message);
    }
  }
}

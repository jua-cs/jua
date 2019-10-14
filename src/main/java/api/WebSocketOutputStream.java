package api;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.OutputStream;

public class WebSocketOutputStream extends OutputStream {
  WebSocketSession session;
  StringBuilder buffer = new StringBuilder();

  public WebSocketOutputStream(WebSocketSession session) {
    this.session = session;
  }

  @Override
  public void write(int i) {
    buffer.append((char) i);
  }

  @Override
  public void flush() throws IOException {
    TextMessage message = new TextMessage(buffer);
    System.out.printf("send message %s\n", message.getPayload());
    session.sendMessage(message);
    System.out.println("message sent");
    buffer = new StringBuilder();
  }
}

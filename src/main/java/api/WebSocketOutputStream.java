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
    session.sendMessage(message);
    buffer = new StringBuilder();
  }
}

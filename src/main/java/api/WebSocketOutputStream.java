package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class WebSocketOutputStream extends OutputStream {
  WebSocketWriter writer;
  StringBuilder buffer = new StringBuilder();
  ObjectMapper objectMapper = new ObjectMapper();
  String prefix;

  public WebSocketOutputStream(WebSocketWriter writer, String prefix) {
    this.writer = writer;
    this.prefix = prefix;
  }

  @Override
  public void write(int i) {
    synchronized (buffer) {
      buffer.append((char) i);
    }
  }

  @Override
  public void write(byte[] b) {
    synchronized (buffer) {
      buffer.append(new String(b));
    }
  }

  @Override
  public void flush() throws IOException {
    String message;
    synchronized (buffer) {
      message = buffer.toString();
      buffer = new StringBuilder();
    }
    HashMap<String, String> payload = new HashMap<>();
    payload.put(prefix, message);
    String serialized = objectMapper.writeValueAsString(payload);
    writer.write(serialized);
  }
}

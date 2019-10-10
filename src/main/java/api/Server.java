package api;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {

  public void run(String args[], int port) {
    SpringApplication app = new SpringApplication(Server.class);
    app.setDefaultProperties(Collections.singletonMap("server.port", String.valueOf(port)));
    app.run(args);
  }
}

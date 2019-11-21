package api;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class Server {

  Logger logger = LoggerFactory.getLogger(Server.class);

  public void run(String[] args, int port) {
    logger.info("Starting the web server");
    SpringApplication app = new SpringApplication(Server.class);
    app.setDefaultProperties(Collections.singletonMap("server.port", String.valueOf(port)));
    app.run(args);
  }

  // TODO: replace the route selector by something that excludes all the routes starting with /api
  @RequestMapping(value = "/repl")
  public String redirect() {
    return "forward:/";
  }
}

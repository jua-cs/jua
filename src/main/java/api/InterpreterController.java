package api;

import java.util.Map;
import jua.Interpreter;
import jua.evaluator.LuaRuntimeException;
import jua.parser.IllegalParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class InterpreterController {

  private Logger logger = LoggerFactory.getLogger(InterpreterController.class);

  // Allow cors for dev
  // TODO: disable it on prod !
  @CrossOrigin("http://localhost:8080")
  @RequestMapping(value = "/api/v1/interpreter", method = RequestMethod.POST)
  public ResponseEntity<StreamingResponseBody> interpret(@RequestBody Map<String, String> payload) {
    logger.info("Received request !");
    StreamingResponseBody resp =
        outputStream -> {
          Interpreter interpreter = new Interpreter(payload.get("code"), outputStream);
          try {
            interpreter.run();
          } catch (IllegalParseException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error parsing the provided code:\n" + e.toString(),
                e);
          } catch (LuaRuntimeException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error evaluating the provided code:\n" + e.toString(),
                e);
          }
        };

    return new ResponseEntity<>(resp, HttpStatus.OK);
  }
}

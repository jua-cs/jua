package api;

import java.util.Map;
import jua.Interpreter;
import jua.evaluator.LuaRuntimeException;
import jua.parser.IllegalParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class InterpreterController {

  Logger logger = LoggerFactory.getLogger(InterpreterController.class);

  @CrossOrigin(origins = "*")
  @RequestMapping(value = "/interpreter", method = RequestMethod.POST)
  public String interpret(@RequestBody Map<String, String> payload) {
    logger.info("Received request !");
    try {
      return Interpreter.eval(payload.get("code"));
    } catch (IllegalParseException e) {
      logger.error("Error parsing !");
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing the provided code", e);
    } catch (LuaRuntimeException e) {
      logger.error("Error evaluating !");
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Error evaluating the provided code", e);
    }
  }
}

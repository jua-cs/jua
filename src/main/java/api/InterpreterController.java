package api;

import java.util.Map;
import jua.Interpreter;
import jua.parser.IllegalParseException;
import org.springframework.web.bind.annotation.*;

@RestController
public class InterpreterController {

  @CrossOrigin(origins = "*")
  @RequestMapping(value = "/interpreter", method = RequestMethod.POST)
  public String interpret(@RequestBody Map<String, String> payload) throws IllegalParseException {
    // TODO: handle exception
    return Interpreter.eval(payload.get("code"));
  }
}

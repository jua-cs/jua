package evaluator;

public class IllegalTypeException extends LuaRuntimeException {

  public IllegalTypeException(String message) {
    super(message);
  }
}

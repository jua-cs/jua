package evaluator;

import objects.LuaObject;

public class IllegalCastException extends LuaRuntimeException {

  IllegalCastException(String message) {
    super(message);
  }

  public static IllegalCastException create(LuaObject o1, String into) {
    return new IllegalCastException(
        String.format(
            "error casting LuaObject %s of type %s into LuaObject of type %s",
            o1, o1.getClass(), into));
  }
}

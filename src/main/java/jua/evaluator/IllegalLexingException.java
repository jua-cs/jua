package jua.evaluator;

import jua.objects.LuaObject;

public class IllegalLexingException extends LuaRuntimeException {

  public IllegalLexingException(String message) {
    super(message);
  }

  public static IllegalLexingException create(LuaObject o1, String into) {
    return new IllegalLexingException(
        String.format(
            "error casting LuaObject %s of type %s into LuaObject of type %s",
            o1, o1.getClass(), into));
  }
}

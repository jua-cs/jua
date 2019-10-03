package objects;

import evaluator.IllegalTypeException;

public interface LuaObject {
  public static void ensureSameType(LuaObject o1, LuaObject o2) throws IllegalTypeException {
    throw new IllegalTypeException(
        String.format(
            "Expected %s (type %s) and %s (type %s) to have same type",
            o1, o1.getClass(), o2, o2.getClass()));
  }

  // TODO casting functions

  String repr();
}

package jua.objects;

import jua.evaluator.IllegalTypeException;

public interface LuaObject {
  public static boolean areSameType(LuaObject o1, LuaObject o2) throws IllegalTypeException {
    return o1.getClass() == o2.getClass();
  }

  public static void ensureSameType(LuaObject o1, LuaObject o2) throws IllegalTypeException {
    if (!areSameType(o1, o2)) {
      throw new IllegalTypeException(
          String.format(
              "Expected %s (type %s) and %s (type %s) to have same type",
              o1, o1.getClass(), o2, o2.getClass()));
    }
  }

  // TODO casting functions

  String repr();
}

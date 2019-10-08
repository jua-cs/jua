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

  public static LuaTable toTable(LuaObject table) throws IllegalTypeException {
    if (!(table instanceof LuaTable)) {
      throw new IllegalTypeException(
          String.format("Can't index LuaObject %s of type %s", table, table.getClass()));
    }

    return (LuaTable) table;
  }

  // TODO casting functions

  String repr();
}

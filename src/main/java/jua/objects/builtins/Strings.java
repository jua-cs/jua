package jua.objects.builtins;

import jua.evaluator.Scope;
import jua.objects.LuaTable;

public class Strings {
  private static final String name = "string";

  public static void register(Scope scope) {
    LuaTable builtins = new LuaTable();
    scope.assignLocal(name, builtins);
  }
}

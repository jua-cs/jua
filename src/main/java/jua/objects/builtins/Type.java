package jua.objects.builtins;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaReturn;
import jua.objects.LuaString;

public class Type {
  private static final String name = "type";

  public static void register(Scope scope) {
    scope.assignLocal(
        name,
        Builtin.createFunction(
            args -> {
              if (args.size() == 0) {
                throw new LuaRuntimeException("type excepts one argument");
              }

              return new LuaReturn(new LuaString(args.get(0).getTypeName()));
            }));
  }
}

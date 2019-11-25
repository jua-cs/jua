package jua.objects.builtins;

import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.objects.LuaReturn;
import jua.objects.LuaString;

public class Cast {

  public static void register(Scope scope) {
    scope.assignLocal(
        "tostring",
        Builtin.createFunction(
            args -> {
              LuaObject arg = Builtin.arg(args, 0);
              return new LuaReturn(LuaString.valueOf(arg));
            }));
  }
}

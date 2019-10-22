package jua.objects.builtins;

import jua.evaluator.Scope;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.objects.LuaReturn;
import jua.objects.LuaString;

public class Cast {

  public static void register(Scope scope) {
    scope.assignLocal(
        "tostring",
        Builtin.createFunction(
            args -> {
              LuaObject arg = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              return new LuaReturn(LuaString.valueOf(arg));
            }));
  }
}

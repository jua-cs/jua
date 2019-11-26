package jua.objects.builtins;

import java.util.ArrayList;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;

public class Select {
  private static final String name = "select";

  public static void register(Scope scope) {
    scope.assignLocal(
        name,
        Builtin.createFunction(
            args -> {
              if (args.size() == 0) {
                throw new LuaRuntimeException("select expects at least one argument");
              }
              LuaObject index = args.get(0);
              if (LuaString.valueOf(index).getValue().equals("#")) {
                return new LuaReturn(new LuaNumber((double) args.size() - 1));
              }

              int i = LuaNumber.valueOf(index).getValue().intValue();
              ArrayList<LuaObject> results = new ArrayList<>();
              if (i < args.size()) {
                results =
                    util.Util.createArrayList(
                        args.subList(i, args.size()).toArray(new LuaObject[args.size() - i]));
              }

              return new LuaReturn(results);
            }));
  }
}

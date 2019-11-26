package jua.objects.builtins;

import java.util.ArrayList;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;

public class Next {
  private static final String name = "next";

  public static void register(Scope scope) {
    scope.assignLocal(
        name,
        Builtin.createFunction(
            args -> {
              LuaObject table = Builtin.arg(args, 0);
              LuaObject idx = Builtin.arg(args, 1);

              if (!(table instanceof LuaTable)) {
                throw new LuaRuntimeException(
                    String.format(
                        "Can't call next on %s of type %s (should be table)",
                        table, table.getClass()));
              }

              LuaTable t = (LuaTable) table;

              ArrayList<LuaObject> result = new ArrayList<>();
              if (idx.equals(LuaNil.getInstance())) {
                result.add(new LuaNumber((double) 1));
                result.add(t.getList(1));
              } else {

                if (!(idx instanceof LuaNumber)) {
                  throw new LuaRuntimeException(
                      String.format(
                          "Can't call next with %s of type %s (should be number)",
                          idx, idx.getClass()));
                }
                int i = ((LuaNumber) idx).getIntValue();

                int sz = t.size();
                if (i >= sz) {
                  result.add(LuaNil.getInstance());
                  result.add(LuaNil.getInstance());
                } else {
                  result.add(new LuaNumber((double) (i + 1)));
                  result.add(t.getList(i + 1));
                }
              }

              return new LuaReturn(result);
            }));
  }
}

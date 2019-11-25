package jua.objects.builtins;

import java.util.stream.Collectors;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;

class Tables {
  private static final String name = "table";

  static void register(Scope scope) {
    LuaTable builtins = new LuaTable();

    builtins.put(
        "remove",
        Builtin.createFunction(
            args -> {
              LuaObject table = Builtin.arg(args, 0);
              LuaObject key = Builtin.arg(args, 1);

              if (!(table instanceof LuaTable)) {
                throw new LuaRuntimeException(
                    String.format(
                        "Can't remove key %s from non table object %s of type %s",
                        key, table, table.getClass()));
              }

              return new LuaReturn(((LuaTable) table).remove(key));
            }));

    builtins.put(
        "insert",
        Builtin.createFunction(
            args -> {
              LuaObject table = Builtin.arg(args, 0);
              LuaObject value = Builtin.arg(args, 1);

              if (!(table instanceof LuaTable)) {
                throw new LuaRuntimeException(
                    String.format(
                        "Can't insert value %s into non table object %s of type %s",
                        value, table, table.getClass()));
              }

              ((LuaTable) table).insertList(value);
              return new LuaReturn(LuaNil.getInstance());
            }));

    builtins.put(
        "concat",
        Builtin.createFunction(
            args -> {
              LuaObject table = Builtin.arg(args, 0);
              LuaObject sep = Builtin.arg(args, 1);

              if (!(table instanceof LuaTable)) {
                throw new LuaRuntimeException(
                    String.format(
                        "Can't concat values of non table object %s of type %s",
                        table, table.getClass()));
              }

              LuaString strSep = LuaString.valueOf(sep);

              String res =
                  ((LuaTable) table)
                      .listValues().stream()
                          .map(LuaObject::repr)
                          // Join using the provided separator
                          .collect(Collectors.joining(strSep.getValue()));

              return new LuaReturn(new LuaString(res));
            }));

    scope.assignLocal(name, builtins);
  }
}

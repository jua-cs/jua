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
              LuaObject table = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              LuaObject key = args.size() > 1 ? args.get(1) : LuaNil.getInstance();

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
              LuaObject table = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              LuaObject value = args.size() > 1 ? args.get(1) : LuaNil.getInstance();

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
              LuaObject table = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              LuaObject sep = args.size() > 1 ? args.get(1) : LuaNil.getInstance();

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

    scope.assign(name, builtins);
  }
}

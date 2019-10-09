package jua.objects.builtins;

import java.util.ArrayList;
import java.util.stream.Collectors;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;

public class Tables {
  public static final String name = "table";

  public static void register(Scope scope) {
    LuaTable table = new LuaTable();

    table.put(new LuaString(Remove.name), new Remove());
    table.put(new LuaString(Insert.name), new Insert());
    table.put(new LuaString(Concat.name), new Concat());
    scope.assign(name, table);
  }

  private static class Concat extends LuaFunction {
    public static final String name = "concat";

    Concat() {
      super(null, null, null);
    }

    @Override
    public LuaReturn evaluate(ArrayList<LuaObject> args) throws LuaRuntimeException {
      LuaObject table = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
      LuaObject sep = args.size() > 1 ? args.get(1) : LuaNil.getInstance();

      if (!(table instanceof LuaTable)) {
        throw new LuaRuntimeException(
            String.format(
                "Can't concat values of non table object %s of type %s", table, table.getClass()));
      }

      LuaString strSep = LuaString.valueOf(sep);

      String res =
          ((LuaTable) table)
              .listValues().stream()
                  .map(LuaObject::repr)
                  // Join using the provided separator
                  .collect(Collectors.joining(strSep.getValue()));

      return new LuaReturn(new LuaString(res));
    }
  }

  private static class Insert extends LuaFunction {
    public static final String name = "insert";

    Insert() {
      super(null, null, null);
    }

    @Override
    public LuaReturn evaluate(ArrayList<LuaObject> args) throws LuaRuntimeException {
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
    }
  }

  private static class Remove extends LuaFunction {
    public static final String name = "remove";

    Remove() {
      super(null, null, null);
    }

    @Override
    public LuaReturn evaluate(ArrayList<LuaObject> args) throws LuaRuntimeException {
      LuaObject table = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
      LuaObject key = args.size() > 1 ? args.get(1) : LuaNil.getInstance();

      if (!(table instanceof LuaTable)) {
        throw new LuaRuntimeException(
            String.format(
                "Can't remove key %s from non table object %s of type %s",
                key, table, table.getClass()));
      }

      return new LuaReturn(((LuaTable) table).remove(key));
    }
  }
}

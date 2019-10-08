package jua.objects.builtins;

import java.util.ArrayList;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;

public class Tables {
  public static final String name = "table";

  public static LuaTable create(Scope scope) {
    LuaTable table = new LuaTable();

    table.put(new LuaString("remove"), new Remove());
    return table;
  }

  private static class Remove extends LuaFunction {
    Remove() {
      super(null, null, null);
    }

    @Override
    public LuaReturn evaluate(ArrayList<LuaObject> args) throws LuaRuntimeException {
      // TODO: testme
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

package jua.objects.builtins;

import java.util.ArrayList;
import jua.ast.Expression;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;

public class Tables {

  public static LuaTable create(Scope scope) {
    LuaTable table = new LuaTable();

    table.put(new LuaString("remove"), new Remove());
    return new LuaTable();
  }

  private static class Remove extends LuaFunction {
    Remove() {
      super(null, null, null);
    }

    public LuaObject evaluate(Scope scope, ArrayList<Expression> args) throws LuaRuntimeException {
      // Remove takes 2 arguments
      LuaObject table = args.size() > 0 ? args.get(0).evaluate(scope) : LuaNil.getInstance();
      // We have toe evaluate the key even if table is not really a table, because that's how it
      // works
      LuaObject key = args.size() > 1 ? args.get(1).evaluate(scope) : LuaNil.getInstance();

      if (!(table instanceof LuaTable)) {
        throw new LuaRuntimeException(
            String.format(
                "Can't remove key %s from non table object %s of type %s",
                key, table, table.getClass()));
      }

      return ((LuaTable) table).remove(key);
    }
  }
}

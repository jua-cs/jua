package jua.objects.builtins;

import java.util.ArrayList;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;

public class Cast {

  public static void register(Scope scope) {
    scope.assign(ToString.name, new ToString());
  }

  public static class ToString extends LuaFunction {
    public static final String name = "tostring";

    public ToString() {
      super(null, null, null);
    }

    @Override
    public LuaReturn evaluate(ArrayList<LuaObject> args) throws LuaRuntimeException {
      LuaObject arg = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
      return new LuaReturn(LuaString.valueOf(arg));
    }
  }
}

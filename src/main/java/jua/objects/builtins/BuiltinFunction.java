package jua.objects.builtins;

import java.util.ArrayList;
import jua.evaluator.LuaRuntimeException;
import jua.objects.Function;
import jua.objects.LuaFunction;
import jua.objects.LuaObject;
import jua.objects.LuaReturn;

public class BuiltinFunction extends LuaFunction {
  private final Function f;

  BuiltinFunction(Function f) {
    super(null, null, null);
    this.f = f;
  }

  @Override
  public LuaReturn evaluate(ArrayList<LuaObject> args) throws LuaRuntimeException {
    return f.evaluate(args);
  }
}

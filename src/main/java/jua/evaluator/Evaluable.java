package jua.evaluator;

import jua.objects.LuaObject;

public interface Evaluable {
  LuaObject evaluate(Evaluator evaluator) throws LuaRuntimeException;
}

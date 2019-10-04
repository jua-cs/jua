package jua.evaluator;

import jua.objects.LuaObject;

public interface Evaluable {
  LuaObject evaluate(Scope scope) throws LuaRuntimeException;
}

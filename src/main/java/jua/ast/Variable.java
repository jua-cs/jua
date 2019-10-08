package jua.ast;

import jua.evaluator.Evaluable;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;

public interface Variable extends Evaluable {
  void assign(Scope scope, LuaObject value, boolean isLocal) throws LuaRuntimeException;

  String name();
}

package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaObject;

public interface Evaluatable {
  LuaObject evaluate(Evaluator evaluator) throws LuaRuntimeException;
}

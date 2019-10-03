package ast;

import evaluator.Evaluator;
import evaluator.LuaRuntimeException;
import objects.LuaObject;

public interface Evaluatable {
  LuaObject evaluate(Evaluator evaluator) throws LuaRuntimeException;
}

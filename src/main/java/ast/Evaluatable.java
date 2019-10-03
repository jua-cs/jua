package ast;

import evaluator.Evaluator;
import evaluator.IllegalCastException;
import objects.LuaObject;

public interface Evaluatable {
  LuaObject evaluate(Evaluator evaluator) throws IllegalCastException;
}

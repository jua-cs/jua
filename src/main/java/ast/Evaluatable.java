package ast;

import evaluator.Evaluator;
import objects.LuaObject;

public interface Evaluatable {
  LuaObject evaluate(Evaluator evaluator);
}

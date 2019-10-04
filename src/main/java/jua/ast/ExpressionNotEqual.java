package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaBoolean;
import jua.objects.LuaObject;
import jua.token.TokenOperator;

public class ExpressionNotEqual extends ExpressionBinary {
  ExpressionNotEqual(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaBoolean evaluate(Evaluator evaluator) throws LuaRuntimeException {
    LuaObject o1 = lhs.evaluate(evaluator);
    LuaObject o2 = rhs.evaluate(evaluator);
    return new LuaBoolean(!o1.equals(o2));
  }
}

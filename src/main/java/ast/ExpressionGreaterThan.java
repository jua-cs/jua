package ast;

import evaluator.Evaluator;
import evaluator.LuaRuntimeException;
import objects.LuaBoolean;
import objects.LuaObject;
import token.TokenOperator;

public class ExpressionGreaterThan extends ExpressionBinary {
  ExpressionGreaterThan(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaBoolean evaluate(Evaluator evaluator) throws LuaRuntimeException {
    LuaObject o1 = lhs.evaluate(evaluator);
    LuaObject o2 = rhs.evaluate(evaluator);
    LuaObject.ensureSameType(o1, o2);

    // TODO
    return null;
  }
}

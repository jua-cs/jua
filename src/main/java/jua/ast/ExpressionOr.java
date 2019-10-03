package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.IllegalCastException;
import jua.objects.LuaBoolean;
import jua.token.TokenOperator;

public class ExpressionOr extends ExpressionBinary {
  ExpressionOr(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaBoolean evaluate(Evaluator evaluator) throws IllegalCastException {
    return new LuaBoolean(
        LuaBoolean.valueOf(lhs.evaluate(evaluator)).getValue()
            || LuaBoolean.valueOf(rhs.evaluate(evaluator)).getValue());
  }
}

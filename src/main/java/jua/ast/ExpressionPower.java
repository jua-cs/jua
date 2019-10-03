package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaNumber;
import jua.token.TokenOperator;

public class ExpressionPower extends ExpressionBinary {
  ExpressionPower(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaNumber evaluate(Evaluator evaluator) throws LuaRuntimeException {
    return new LuaNumber(
        Math.pow(
            LuaNumber.valueOf(lhs.evaluate(evaluator)).getValue(),
            LuaNumber.valueOf(rhs.evaluate(evaluator)).getValue()));
  }
}

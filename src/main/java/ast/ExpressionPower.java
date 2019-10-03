package ast;

import evaluator.Evaluator;
import evaluator.IllegalCastException;
import objects.LuaNumber;
import token.TokenOperator;

public class ExpressionPower extends ExpressionBinary {
  ExpressionPower(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaNumber evaluate(Evaluator evaluator) throws IllegalCastException {
    return new LuaNumber(
        Math.pow(
            LuaNumber.valueOf(lhs.evaluate(evaluator)).getValue(),
            LuaNumber.valueOf(rhs.evaluate(evaluator)).getValue()));
  }
}

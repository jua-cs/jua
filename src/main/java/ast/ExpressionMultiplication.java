package ast;

import evaluator.Evaluator;
import evaluator.IllegalCastException;
import objects.LuaNumber;
import objects.LuaObject;
import token.TokenOperator;

public class ExpressionMultiplication extends ExpressionBinary {

  ExpressionMultiplication(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaObject evaluate(Evaluator evaluator) throws IllegalCastException {
    return new LuaNumber(
        LuaNumber.valueOf(lhs.evaluate(evaluator)).getValue()
            * LuaNumber.valueOf(rhs.evaluate(evaluator)).getValue());
  }
}

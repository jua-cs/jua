package ast;

import evaluator.Evaluator;
import evaluator.IllegalCastException;
import objects.LuaNumber;
import token.TokenOperator;

public class ExpressionSubtraction extends ExpressionBinary {

  ExpressionSubtraction(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaNumber evaluate(Evaluator evaluator) throws IllegalCastException {
    Double number = LuaNumber.valueOf(lhs.evaluate(evaluator)).getValue() - LuaNumber.valueOf(rhs.evaluate(evaluator)).getValue();
    return new LuaNumber(number);
  }
}


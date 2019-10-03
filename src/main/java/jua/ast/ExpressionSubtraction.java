package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.IllegalCastException;
import jua.objects.LuaNumber;
import jua.token.TokenOperator;

public class ExpressionSubtraction extends ExpressionBinary {

  ExpressionSubtraction(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaNumber evaluate(Evaluator evaluator) throws IllegalCastException {
    Double number =
        LuaNumber.valueOf(lhs.evaluate(evaluator)).getValue()
            - LuaNumber.valueOf(rhs.evaluate(evaluator)).getValue();
    return new LuaNumber(number);
  }
}

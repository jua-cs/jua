package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaNumber;
import jua.token.TokenOperator;

public class ExpressionAddition extends ExpressionBinary {

  ExpressionAddition(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaNumber evaluate(Evaluator evaluator) throws LuaRuntimeException {
    Double number =
        LuaNumber.valueOf(lhs.evaluate(evaluator)).getValue()
            + LuaNumber.valueOf(rhs.evaluate(evaluator)).getValue();
    return new LuaNumber(number);
    }
}

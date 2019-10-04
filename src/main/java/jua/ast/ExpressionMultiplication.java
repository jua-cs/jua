package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaNumber;
import jua.token.TokenOperator;

public class ExpressionMultiplication extends ExpressionBinary {

  ExpressionMultiplication(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaNumber evaluate(Scope scope) throws LuaRuntimeException {
    return new LuaNumber(
        LuaNumber.valueOf(lhs.evaluate(scope)).getValue()
            * LuaNumber.valueOf(rhs.evaluate(scope)).getValue());
  }
}

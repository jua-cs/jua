package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaNumber;
import jua.objects.LuaObject;
import jua.token.TokenOperator;

public class ExpressionLeftShift extends ExpressionBinary {
  public ExpressionLeftShift(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    // We cast Double to Long as java doesn't support bitwise operations on floating numbers
    Long number =
            LuaNumber.valueOf(lhs.evaluate(scope)).getValue().longValue()
                    << LuaNumber.valueOf(rhs.evaluate(scope)).getValue().longValue();
    return new LuaNumber(number.doubleValue());
  }
}

package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaNumber;
import jua.objects.LuaObject;
import jua.token.TokenOperator;

public class ExpressionBinaryNot extends ExpressionUnary {
  ExpressionBinaryNot(TokenOperator token, Expression value) {
    super(token, value);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    // We cast Double to Long as java doesn't support bitwise operations on floating numbers
    return LuaNumber.valueOf(value.evaluate(scope)).bNot();
  }
}

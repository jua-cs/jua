package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaNumber;
import jua.objects.LuaObject;
import jua.token.TokenOperator;

public class ExpressionBitwiseXor extends ExpressionBinary {
  public ExpressionBitwiseXor(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    // We cast Double to Long as java doesn't support bitwise operations on floating numbers
    return LuaNumber.valueOf(lhs.evaluate(scope)).bXor(LuaNumber.valueOf(rhs.evaluate(scope)));
  }
}

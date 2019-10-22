package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.token.TokenOperator;

public class ExpressionBinaryNot extends ExpressionUnary {
  ExpressionBinaryNot(TokenOperator token, Expression value) {
    super(token, value);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    return null;
  }
}

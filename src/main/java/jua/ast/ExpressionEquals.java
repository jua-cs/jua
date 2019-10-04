package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaBoolean;
import jua.objects.LuaObject;
import jua.token.TokenOperator;

public class ExpressionEquals extends ExpressionBinary {
  ExpressionEquals(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaBoolean evaluate(Scope scope) throws LuaRuntimeException {
    LuaObject o1 = lhs.evaluate(scope);
    LuaObject o2 = rhs.evaluate(scope);
    return LuaBoolean.getLuaBool(o1.equals(o2));
  }
}

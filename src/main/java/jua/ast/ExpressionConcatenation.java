package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.objects.LuaString;
import jua.token.TokenOperator;

public class ExpressionConcatenation extends ExpressionBinary {
  ExpressionConcatenation(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    LuaString res = LuaString.valueOf(lhs.evaluate(scope));
    res.append(LuaString.valueOf(rhs.evaluate(scope)));
    return res;
  }
}

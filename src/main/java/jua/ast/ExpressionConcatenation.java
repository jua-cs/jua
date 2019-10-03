package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaObject;
import jua.objects.LuaString;
import jua.token.TokenOperator;

public class ExpressionConcatenation extends ExpressionBinary {
  ExpressionConcatenation(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaObject evaluate(Evaluator evaluator) throws LuaRuntimeException {
    LuaString res = LuaString.valueOf(lhs.evaluate(evaluator));
    res.append(LuaString.valueOf(rhs.evaluate(evaluator)));
    return res;
  }
}

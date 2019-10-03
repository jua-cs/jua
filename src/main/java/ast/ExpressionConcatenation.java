package ast;

import evaluator.Evaluator;
import evaluator.IllegalCastException;
import objects.LuaObject;
import objects.LuaString;
import token.TokenOperator;

public class ExpressionConcatenation extends ExpressionBinary {
  ExpressionConcatenation(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaObject evaluate(Evaluator evaluator) throws IllegalCastException {
    LuaString res = LuaString.valueOf(lhs.evaluate(evaluator));
    res.append(LuaString.valueOf(rhs.evaluate(evaluator)));
    return res;
  }
}

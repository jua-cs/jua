package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaBoolean;
import jua.token.Operator;
import jua.token.TokenFactory;
import jua.token.TokenOperator;

public class ExpressionNot extends ExpressionUnary {

  ExpressionNot(TokenOperator token, Expression value) {
    super(TokenFactory.create(Operator.NOT, token.getLine(), token.getPosition()), value);
  }

  @Override
  public LuaBoolean evaluate(Scope scope) throws LuaRuntimeException {
    return LuaBoolean.getLuaBool(!LuaBoolean.valueOf(value.evaluate(scope)).getValue());
  }
}

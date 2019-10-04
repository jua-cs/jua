package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaNumber;
import jua.token.Operator;
import jua.token.Token;
import jua.token.TokenFactory;

public class ExpressionNegative extends ExpressionUnary {

  ExpressionNegative(Token token, Expression value) {
    super(TokenFactory.create(Operator.NEGATIVE, token.getLine(), token.getPosition()), value);
  }

  @Override
  public LuaNumber evaluate(Scope scope) throws LuaRuntimeException {
    Double number = LuaNumber.valueOf(value.evaluate(scope)).getValue();
    return new LuaNumber(-number);
  }
}

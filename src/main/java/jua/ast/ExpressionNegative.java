package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.IllegalCastException;
import jua.objects.LuaNumber;
import jua.token.Operator;
import jua.token.Token;
import jua.token.TokenFactory;

public class ExpressionNegative extends ExpressionUnary {

  ExpressionNegative(Token token, Expression value) {
    super(TokenFactory.create(Operator.NEGATIVE, token.getLine(), token.getPosition()), value);
  }

  @Override
  public LuaNumber evaluate(Evaluator evaluator) throws IllegalCastException {
    Double number = LuaNumber.valueOf(value.evaluate(evaluator)).getValue();
    return new LuaNumber(-number);
  }
}

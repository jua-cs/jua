package ast;

import evaluator.Evaluator;
import evaluator.IllegalCastException;
import objects.LuaNumber;
import objects.LuaObject;
import token.Operator;
import token.Token;
import token.TokenFactory;

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

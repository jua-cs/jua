package ast;

import evaluator.Evaluator;
import evaluator.LuaRuntimeException;
import objects.LuaNil;
import objects.LuaObject;
import token.Operator;
import token.Token;
import token.TokenFactory;

public class ExpressionIndex extends ExpressionBinary {
  public ExpressionIndex(Token token, Expression lhs, Expression rhs) {
    super(TokenFactory.create(Operator.INDEX, token.getLine(), token.getPosition()), lhs, rhs);
  }

  @Override
  public String toString() {
    return String.format("%s[%s]", lhs, rhs);
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public LuaObject evaluate(Evaluator evaluator) throws LuaRuntimeException {
    // TODO: later on
    return new LuaNil();
  }
}

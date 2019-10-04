package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.token.Operator;
import jua.token.Token;
import jua.token.TokenFactory;

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
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    // TODO: later on
    return LuaNil.getInstance();
  }
}

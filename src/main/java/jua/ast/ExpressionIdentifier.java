package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.IllegalCastException;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.token.Token;

public class ExpressionIdentifier extends Expression {
  private String identifier;

  ExpressionIdentifier(Token token) {
    super(token);
    this.identifier = token.getLiteral();
  }

  public String getIdentifier() {
    return identifier;
  }

  @Override
  public LuaObject evaluate(Evaluator evaluator) throws IllegalCastException {
    return new LuaNil();
  }
}

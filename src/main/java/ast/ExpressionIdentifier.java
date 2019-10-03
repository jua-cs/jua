package ast;

import evaluator.Evaluator;
import evaluator.IllegalCastException;
import objects.LuaNil;
import objects.LuaObject;
import token.Token;

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

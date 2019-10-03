package ast;

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
}

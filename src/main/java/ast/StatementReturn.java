package ast;

import token.Token;

public class StatementReturn extends Statement {
  private Expression value;
  public StatementReturn(Token token, Expression value) {
    super(token);
    this.value = value;
  }
}

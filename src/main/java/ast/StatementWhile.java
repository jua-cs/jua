package ast;

import token.Token;

public class StatementWhile extends Statement {

  private Expression condition;
  private Statement consequence;


  public StatementWhile(Token token, Expression condition, Statement consequence) {
    super(token);
    this.condition = condition;
    this.consequence = consequence;
  }
}

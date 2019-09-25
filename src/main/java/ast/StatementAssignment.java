package ast;

import token.Token;

public class StatementAssignment extends Statement {
  private ExpressionIdentifier lhs;
  private Expression rhs;

  public StatementAssignment(Token token, ExpressionIdentifier lhs, Expression rhs) {
    super(token);
    this.lhs = lhs;
    this.rhs = rhs;
  }
}

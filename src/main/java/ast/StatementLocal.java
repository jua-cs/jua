package ast;

import token.Token;

public class StatementLocal extends Statement {
  private StatementAssignment assignment;

  public StatementLocal(Token token, StatementAssignment assignment) {
    super(token);
    this.assignment = assignment;
  }
}

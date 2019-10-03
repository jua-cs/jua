package jua.ast;

import jua.token.Token;

public class StatementLocal extends Statement {
  private StatementAssignment assignment;

  public StatementLocal(Token token, StatementAssignment assignment) {
    super(token);
    this.assignment = assignment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    StatementLocal that = (StatementLocal) o;

    return assignment.equals(that.assignment);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + assignment.hashCode();
    return result;
  }
}

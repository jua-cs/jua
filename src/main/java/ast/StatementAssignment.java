package ast;

import java.util.Objects;
import token.Token;

public class StatementAssignment extends Statement {
  private ExpressionIdentifier lhs;
  private Expression rhs;

  public StatementAssignment(Token token, ExpressionIdentifier lhs, Expression rhs) {
    super(token);
    this.lhs = lhs;
    this.rhs = rhs;
  }

  @Override
  public String toString() {
    return String.format("%s = %s", lhs, rhs);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StatementAssignment that = (StatementAssignment) o;
    return Objects.equals(lhs, that.lhs) && Objects.equals(rhs, that.rhs);
  }

  public ExpressionIdentifier getLhs() {
    return lhs;
  }

  public Expression getRhs() {
    return rhs;
  }
}

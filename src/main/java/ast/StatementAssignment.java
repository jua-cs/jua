package ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import token.Token;

public class StatementAssignment extends Statement {
  private ArrayList<ExpressionIdentifier> lhs = new ArrayList<>();
  private ArrayList<Expression> rhs = new ArrayList<>();

  public StatementAssignment(Token token, ExpressionIdentifier lhs, Expression rhs) {
    super(token);
    this.lhs.add(lhs);
    this.rhs.add(rhs);
  }

  public StatementAssignment(
      Token token, ArrayList<ExpressionIdentifier> lhs, ArrayList<Expression> rhs) {
    super(token);
    this.lhs = lhs;
    this.rhs = rhs;
  }

  @Override
  public String toString() {
    return String.format(
        "%s = %s",
        lhs.stream().map(Object::toString).collect(Collectors.joining(", ")),
        rhs.stream().map(Objects::toString).collect(Collectors.joining(", ")));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StatementAssignment that = (StatementAssignment) o;
    return Objects.equals(lhs, that.lhs) && Objects.equals(rhs, that.rhs);
  }

  public ArrayList<ExpressionIdentifier> getLhs() {
    return lhs;
  }

  public ArrayList<Expression> getRhs() {
    return rhs;
  }
}

package ast;

import java.util.Objects;
import token.Token;

public class StatementWhile extends Statement {

  private Expression condition;
  private Statement consequence;

  public StatementWhile(Token token, Expression condition, Statement consequence) {
    super(token);
    this.condition = condition;
    this.consequence = consequence;
  }

  @Override
  public String toString() {
    return String.format("while %s do\n%s end", condition, consequence);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementWhile that = (StatementWhile) o;
    return Objects.equals(condition, that.condition)
        && Objects.equals(consequence, that.consequence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), condition, consequence);
  }

  public Expression getCondition() {
    return condition;
  }

  public Statement getConsequence() {
    return consequence;
  }
}

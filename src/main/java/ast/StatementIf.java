package ast;

import java.util.Objects;
import token.Token;

public class StatementIf extends Statement {
  private Expression condition;
  private Statement consequence;
  private Statement alternative;

  public StatementIf(Token token, Expression condition, Statement consequence) {
    super(token);
    this.condition = condition;
    this.consequence = consequence;
  }

  public StatementIf(
      Token token, Expression condition, Statement consequence, Statement alternative) {
    super(token);
    this.condition = condition;
    this.consequence = consequence;
    this.alternative = alternative;
  }

  @Override
  public String toString() {
    if (alternative != null) {
      return String.format("if %s\nthen %s\nelse\n%s\nend", condition, consequence, alternative);
    }
    return String.format("if %s\nthen %s\nend", condition, consequence);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementIf that = (StatementIf) o;
    return Objects.equals(condition, that.condition)
        && Objects.equals(consequence, that.consequence)
        && Objects.equals(alternative, that.alternative);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), condition, consequence, alternative);
  }

  public Expression getCondition() {
    return condition;
  }

  public Statement getConsequence() {
    return consequence;
  }

  public Statement getAlternative() {
    return alternative;
  }
}

package ast;

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
}

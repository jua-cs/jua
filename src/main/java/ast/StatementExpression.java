package ast;

public class StatementExpression extends Statement {

  private final Expression expr;

  public StatementExpression(Expression expr) {
    super(null);
    this.expr = expr;
  }

  public Expression getExpr() {
    return expr;
  }

  @Override
  public String toString() {
    return expr.toString();
  }
}

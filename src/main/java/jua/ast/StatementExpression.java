package jua.ast;

import java.util.Objects;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.token.TokenFactory;

public class StatementExpression extends Statement {

  private final Expression expr;

  public StatementExpression(Expression expr) {
    super(TokenFactory.create(expr.getLiteral()));
    this.expr = expr;
  }

  public Expression getExpr() {
    return expr;
  }

  @Override
  public String toString() {
    return expr.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementExpression that = (StatementExpression) o;
    return Objects.equals(expr, that.expr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), expr);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    return expr.evaluate(scope);
  }
}

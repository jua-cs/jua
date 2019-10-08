package jua.ast;

import java.util.ArrayList;
import java.util.Objects;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.token.TokenFactory;

public class StatementExpression extends Statement {

  private ArrayList<Expression> exprs = new ArrayList<>();

  public StatementExpression(Expression expr) {
    super(TokenFactory.create(expr.getLiteral()));
    this.exprs.add(expr);
  }

  public StatementExpression(ArrayList<Expression> exprs) {
    super(TokenFactory.create(exprs.get(0).getLiteral()));
    this.exprs = exprs;
  }

  public Expression getExpr() {
    return exprs.get(0);
  }

  public ArrayList<Expression> getExprs() {
    return exprs;
  }

  @Override
  public String toString() {
    return exprs.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementExpression that = (StatementExpression) o;
    return Objects.equals(exprs, that.exprs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), exprs);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    // TODO: FIXME  we should evaluate everything
    return exprs.get(0).evaluate(scope);
  }
}

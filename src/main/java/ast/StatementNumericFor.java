package ast;

import java.util.Objects;
import token.Token;

public class StatementNumericFor extends StatementFor {

  Expression var;
  Expression limit;
  Expression step;

  public StatementNumericFor(
      Token token,
      ExpressionIdentifier variable,
      Statement block,
      Expression var,
      Expression limit,
      Expression step) {

    super(token, util.Util.createArrayList(variable), block);
    this.var = var;
    this.limit = limit;
    this.step = step;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementNumericFor that = (StatementNumericFor) o;
    return Objects.equals(var, that.var)
        && Objects.equals(limit, that.limit)
        && Objects.equals(step, that.step);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), var, limit, step);
  }
}

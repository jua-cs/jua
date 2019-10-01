package ast;

import java.util.Objects;
import token.TokenOperator;

public abstract class ExpressionBinary extends Expression {

  protected Expression lhs;
  protected Expression rhs;

  protected ExpressionBinary(TokenOperator token, Expression lhs, Expression rhs) {
    super(token);
    this.lhs = lhs;
    this.rhs = rhs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ExpressionBinary that = (ExpressionBinary) o;
    return Objects.equals(token, that.token)
        && Objects.equals(lhs, that.lhs)
        && Objects.equals(rhs, that.rhs);
  }

  @Override
  public String toString() {
    return String.format("(%s %s %s)", lhs, ((TokenOperator) token).getOperator(), rhs);
  }
}

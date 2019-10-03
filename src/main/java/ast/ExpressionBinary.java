package ast;

import token.TokenOperator;

public abstract class ExpressionBinary extends Expression {

  protected Expression lhs;
  protected Expression rhs;

  ExpressionBinary(TokenOperator token, Expression lhs, Expression rhs) {
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

    if (!lhs.equals(that.lhs)) return false;
    return rhs.equals(that.rhs);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + lhs.hashCode();
    result = 31 * result + rhs.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("(%s %s %s)", lhs, getLiteral(), rhs);
  }
}

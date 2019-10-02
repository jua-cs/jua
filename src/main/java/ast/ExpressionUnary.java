package ast;

import java.util.Objects;
import token.TokenOperator;

public abstract class ExpressionUnary extends Expression {

  protected Expression value;

  ExpressionUnary(TokenOperator token, Expression value) {
    super(token);
    this.value = value;
  }

  public Expression getValue() {
    return value;
  }

//  public String toString() {
//    return String.format("(%s %s)", ((TokenOperator) this.token).getOperator(), value);
//  }
@Override
public String toString() {
    return String.format("(%s %s)", this.getLiteral(), value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExpressionUnary that = (ExpressionUnary) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}

package jua.token;

public class TokenOperator extends Token {

  private Operator operator;

  protected TokenOperator(Operator operator, int line, int position) {
    super(operator.toString(), line, position);
    this.operator = operator;
  }

  public Operator getOperator() {
    return operator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    TokenOperator that = (TokenOperator) o;

    return operator == that.operator;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + operator.hashCode();
    return result;
  }
}

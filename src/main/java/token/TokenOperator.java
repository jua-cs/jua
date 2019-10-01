package token;

public class TokenOperator extends Token {

  private Operator operator;

  protected TokenOperator(Operator operator, int line, int position) {
    super(TokenType.OPERATOR, operator.toString(), line, position);
    this.operator = operator;
  }

  public Operator getOperator() {
    return operator;
  }

}

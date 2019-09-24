package token;

public class TokenOperator extends Token {

  private Operator operator;

  TokenOperator(int line, int position, Operator operator) {
    super(TokenType.OPERATOR, line, position, operator.toString());
    this.operator = operator;
  }

  public Operator getOperator() {
    return operator;
  }
}

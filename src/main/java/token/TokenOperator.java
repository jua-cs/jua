package token;

public class TokenOperator extends Token {

  TokenOperator(int line, int position, Operator operator) {
    super(TokenType.OPERATOR, line, position, operator.toString());
  }
}

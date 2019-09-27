package parser;

import ast.Expression;
import token.Delimiter;
import token.Token;
import token.TokenDelimiter;

public class ParenthesisParser implements PrefixParser {
  private final int precedence;

  public ParenthesisParser(int precedence) {
    this.precedence = precedence;
  }

  @Override
  public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
    Expression inner = parser.parseExpression(precedence);
    TokenDelimiter delim = (TokenDelimiter) parser.currentToken();
    if (delim.getDelimiter() != Delimiter.RPAREN) {
      throw new IllegalParseException(
          String.format("unexpected delimiter: %s, expected ')'", delim.getDelimiter()));
    }
    parser.advanceTokens();
    return inner;
  }
}

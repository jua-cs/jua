package jua.parser;

import jua.ast.Expression;
import jua.token.Delimiter;
import jua.token.Token;
import jua.token.TokenDelimiter;

public class ParenthesisParser implements PrefixParser {
  private final int precedence;

  public ParenthesisParser(int precedence) {
    this.precedence = precedence;
  }

  @Override
  public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
    Expression inner = parser.parseExpression(0);
    TokenDelimiter delim = (TokenDelimiter) parser.currentToken();
    if (delim.getDelimiter() != Delimiter.RPAREN) {
      throw new IllegalParseException(
          String.format("unexpected delimiter: %s, expected ')'", delim.getDelimiter()));
    }
    parser.advanceTokens();
    return inner;
  }
}

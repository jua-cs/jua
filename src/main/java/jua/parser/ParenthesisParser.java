package jua.parser;

import jua.ast.Expression;
import jua.token.Delimiter;
import jua.token.Token;

public class ParenthesisParser implements PrefixParser {
  private final int precedence;

  public ParenthesisParser(int precedence) {
    this.precedence = precedence;
  }

  @Override
  public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
    Expression inner = parser.parseExpression(0);
    parser.consume(Delimiter.RPAREN);
    return inner;
  }
}

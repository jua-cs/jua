package jua.parser;

import jua.ast.Expression;
import jua.ast.ExpressionIndex;
import jua.token.Delimiter;
import jua.token.Token;

public class BracketParser implements InfixParser {
  private final int precedence;

  public BracketParser(int precedence) {
    this.precedence = precedence;
  }

  @Override
  public ExpressionIndex parseInfix(Parser parser, Token tok, Expression lhs)
      throws IllegalParseException {
    ExpressionIndex expr = new ExpressionIndex(tok, lhs, parser.parseExpression(0));

    parser.consume(Delimiter.RBRACK);
    return expr;
  }

  @Override
  public int getPrecedence() {
    return precedence;
  }
}

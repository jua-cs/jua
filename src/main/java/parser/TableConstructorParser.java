package parser;

import ast.Expression;
import token.Token;

public class TableConstructorParser implements PrefixParser {
  private final int precedence;

  public TableConstructorParser(int precedence) {
    this.precedence = precedence;
  }

  @Override
  public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
    // TODO
    return null;
  }
}

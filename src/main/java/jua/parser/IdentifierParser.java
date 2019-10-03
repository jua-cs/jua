package jua.parser;

import jua.ast.Expression;
import jua.ast.ExpressionFactory;
import jua.token.Token;

public class IdentifierParser implements PrefixParser {
  @Override
  public Expression parsePrefix(Parser parser, Token tok) {
    return ExpressionFactory.create(tok);
  }
}

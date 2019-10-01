package parser;

import ast.Expression;
import ast.ExpressionFactory;
import ast.ExpressionIdentifier;
import token.Token;

public class IdentifierParser implements PrefixParser {
  @Override
  public Expression parsePrefix(Parser parser, Token tok) {
    return ExpressionFactory.create(tok);
  }
}

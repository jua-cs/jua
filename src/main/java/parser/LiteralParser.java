package parser;

import ast.Expression;
import ast.ExpressionFactory;
import ast.ExpressionLiteral;
import token.Token;

public class LiteralParser implements PrefixParser {
  @Override
  public Expression parsePrefix(Parser parser, Token tok) {
    return ExpressionFactory.create(tok);
  }
}

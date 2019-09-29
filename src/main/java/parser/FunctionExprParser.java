package parser;

import ast.Expression;
import ast.ExpressionFunction;
import ast.ExpressionIdentifier;
import java.util.ArrayList;
import token.Token;

public class FunctionExprParser implements PrefixParser {
  @Override
  public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
    ArrayList<ExpressionIdentifier> args = parser.parseFuncArgs();
    return new ExpressionFunction(tok, args, parser.parseBlockStatement());
  }
}

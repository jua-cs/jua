package parser;

import ast.*;
import java.util.ArrayList;
import token.Keyword;
import token.Token;

public class FunctionExprParser implements PrefixParser {
  @Override
  public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
    ArrayList<ExpressionIdentifier> args = parser.parseFuncArgs();
    StatementList body = parser.parseListStatement();

    // consume END of function statement
    parser.consume(Keyword.END);
    return ExpressionFactory.createExpressionFunction(tok, args, body);
  }
}

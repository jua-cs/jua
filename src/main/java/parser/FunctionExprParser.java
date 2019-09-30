package parser;

import ast.*;
import java.util.ArrayList;
import token.Token;

public class FunctionExprParser implements PrefixParser {
  @Override
  public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
    ArrayList<ExpressionIdentifier> args = parser.parseFuncArgs();
    StatementList body = parser.parseListStatement();

    // consume END of function statement
    parser.advanceTokens();
    return new ExpressionFunction(tok, args, body);
  }
}

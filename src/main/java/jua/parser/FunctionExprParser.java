package jua.parser;

import java.util.ArrayList;
import jua.ast.*;
import jua.token.Keyword;
import jua.token.Token;

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

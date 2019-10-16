package jua.parser;

import java.util.ArrayList;
import jua.ast.Expression;
import jua.ast.ExpressionFactory;
import jua.ast.ExpressionIdentifier;
import jua.ast.StatementList;
import jua.token.Delimiter;
import jua.token.Keyword;
import jua.token.Token;

public class FunctionExprParser implements PrefixParser {

  static ArrayList<ExpressionIdentifier> parseFuncArgs(Parser parser) throws IllegalParseException {
    // consume the left parenthesis
    parser.consume(Delimiter.LPAREN);
    ArrayList<ExpressionIdentifier> args = new ArrayList<>();

    // if there is no args, we look for a ')'
    if (parser.currentToken().isSubtype(Delimiter.RPAREN)) {
      parser.consume(Delimiter.RPAREN);
      return args;
    }
    args = parser.parseCommaSeparatedExpressions(0);

    // Consume ')'
    parser.consume(Delimiter.RPAREN);

    return args;
  }

  @Override
  public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
    ArrayList<ExpressionIdentifier> args = parseFuncArgs(parser);
    StatementList body = parser.parseListStatement();

    // consume END of function statement
    parser.consume(Keyword.END);
    return ExpressionFactory.createExpressionFunction(tok, args, body);
  }
}

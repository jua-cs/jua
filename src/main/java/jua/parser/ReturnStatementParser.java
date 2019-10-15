package jua.parser;

import jua.ast.Statement;
import jua.ast.StatementReturn;
import jua.token.Keyword;
import jua.token.Token;

public class ReturnStatementParser implements StatementParser {
  @Override
  public Statement parse(Parser parser) throws IllegalParseException {
    Token tok = parser.currentToken();
    parser.consume(Keyword.RETURN);
    return new StatementReturn(tok, parser.parseCommaSeparatedExpressions(0));
  }

  @Override
  public boolean matches(Parser parser) {
    return parser.currentToken().isSubtype(Keyword.RETURN);
  }
}

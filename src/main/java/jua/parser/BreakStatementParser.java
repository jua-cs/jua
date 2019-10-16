package jua.parser;

import jua.ast.Statement;
import jua.ast.StatementBreak;
import jua.token.Keyword;
import jua.token.Token;

public class BreakStatementParser implements StatementParser {

  @Override
  public Statement parse(Parser parser) throws IllegalParseException {
    // consume BREAK keyword
    Token tok = parser.currentToken();
    parser.consume(Keyword.BREAK);

    return new StatementBreak(tok);
  }

  @Override
  public boolean matches(Parser parser) {
    return parser.currentToken().isSubtype(Keyword.BREAK);
  }
}

package jua.parser;

import jua.ast.Statement;
import jua.token.Keyword;
import jua.token.Token;

public class ForStatementParser implements StatementParser {
  @Override
  public Statement parse(Parser parser) throws IllegalParseException {

    // consume FOR keyword
    Token tok = parser.currentToken();
    parser.consume(Keyword.FOR);

    if (parser.isAssignmentStatement()) {
      return parser.parseNumericForStatement(tok);
    }

    return parser.parseGenericForStatement(tok);
  }

  @Override
  public boolean matches(Parser parser) {
    return parser.currentToken().isSubtype(Keyword.FOR);
  }
}

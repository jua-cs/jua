package jua.parser;

import jua.ast.Statement;
import jua.ast.StatementList;
import jua.token.Keyword;

public class BlockStatementParser implements StatementParser {
  @Override
  public Statement parse(Parser parser) throws IllegalParseException {

    // consume the DO keyword
    parser.consume(Keyword.DO);

    StatementList list = parser.parseListStatement();

    // consume the END keyword
    parser.consume(Keyword.END);

    return list;
  }

  @Override
  public boolean matches(Parser parser) {
    return parser.currentToken().isSubtype(Keyword.DO);
  }
}

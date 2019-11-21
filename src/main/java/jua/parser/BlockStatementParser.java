package jua.parser;

import jua.ast.Statement;
import jua.ast.StatementBlock;
import jua.token.Keyword;
import jua.token.Token;

public class BlockStatementParser implements StatementParser {
  @Override
  public Statement parse(Parser parser) throws IllegalParseException {

    // consume the DO keyword
    Token tok = parser.currentToken();
    parser.consume(Keyword.DO);

    StatementBlock block = new StatementBlock(tok, parser.parseListStatement());

    // consume the END keyword
    parser.consume(Keyword.END);

    return block;
  }

  @Override
  public boolean matches(Parser parser) {
    return parser.currentToken().isSubtype(Keyword.DO);
  }
}

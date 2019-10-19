package jua.parser;

import jua.ast.Expression;
import jua.ast.Statement;
import jua.ast.StatementWhile;
import jua.token.Keyword;
import jua.token.Token;

public class WhileStatementParser implements StatementParser {
  @Override
  public Statement parse(Parser parser) throws IllegalParseException {
    Token tok = parser.currentToken();
    parser.consume(Keyword.WHILE);
    Expression condition = parser.parseExpression();

    // TODO: maybe reuse the parser's instance ?
    BlockStatementParser blockParser = new BlockStatementParser();

    Statement consequence = blockParser.parse(parser);

    return new StatementWhile(tok, condition, consequence);
  }

  @Override
  public boolean matches(Parser parser) {
    return parser.currentToken().isSubtype(Keyword.WHILE);
  }
}

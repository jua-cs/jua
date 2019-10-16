package jua.parser;

import jua.ast.Expression;
import jua.ast.Statement;
import jua.ast.StatementRepeatUntil;
import jua.token.Keyword;
import jua.token.Token;

public class RepeatStatementParser implements StatementParser {
  @Override
  public Statement parse(Parser parser) throws IllegalParseException {
    Token tok = parser.currentToken();
    parser.consume(Keyword.REPEAT);

    Statement action = parser.parseListStatement();

    // expecting an until
    parser.consume(Keyword.UNTIL);

    Expression condition = parser.parseExpression();

    return new StatementRepeatUntil(tok, condition, action);
  }

  @Override
  public boolean matches(Parser parser) {
    return parser.currentToken().isSubtype(Keyword.REPEAT);
  }
}

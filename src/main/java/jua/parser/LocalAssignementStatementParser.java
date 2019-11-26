package jua.parser;

import jua.token.Keyword;

public class LocalAssignementStatementParser extends AssignmentStatementParser {

  @Override
  public boolean matches(Parser parser) {
    return parser.currentToken().isSubtype(Keyword.LOCAL)
        && !parser.nextToken().isSubtype(Keyword.FUNCTION);
  }
}

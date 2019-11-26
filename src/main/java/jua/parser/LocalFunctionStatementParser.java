package jua.parser;

import jua.token.Keyword;

public class LocalFunctionStatementParser extends FunctionStatementParser {

  @Override
  public boolean matches(Parser parser) {
    return parser.currentToken().isSubtype(Keyword.LOCAL)
        && parser.nextToken().isSubtype(Keyword.FUNCTION);
  }
}

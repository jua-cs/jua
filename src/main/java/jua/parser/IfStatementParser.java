package jua.parser;

import jua.ast.Expression;
import jua.ast.Statement;
import jua.ast.StatementIf;
import jua.token.Keyword;
import jua.token.Token;
import jua.token.TokenKeyword;

public class IfStatementParser implements StatementParser {

  // nested should only be true when parsing an elseif statement inside a main if
  // this argument tells parseIfStatement not to consume the only END keyword
  @Override
  public Statement parse(Parser parser) throws IllegalParseException {
    return parseIfStatement(parser, false);
  }

  private Statement parseIfStatement(Parser parser, boolean nested) throws IllegalParseException {
    // consumeKeyword.IF or Keyword.ELSEIF;
    try {
      parser.consume(Keyword.IF);
    } catch (IllegalParseException e) {
      parser.consume(Keyword.ELSEIF);
    }

    Expression condition = parser.parseExpression();

    parser.consume(Keyword.THEN);
    Statement consequence = parser.parseListStatement();
    Token tok = parser.currentToken();
    if (!parser.currentToken().isBlockEnd()) {
      throw new IllegalParseException(
          String.format("unexpected jua.token %s, expected end, else or elseif", tok));
    }
    Statement alternative = null;
    TokenKeyword keyword = (TokenKeyword) tok;
    switch (keyword.getKeyword()) {
      case ELSEIF:
        alternative = parseIfStatement(parser, true);
        break;
      case ELSE:
        parser.consume(Keyword.ELSE);
        alternative = parser.parseListStatement();
        break;
    }

    tok = parser.currentToken();
    if (!tok.isSubtype(Keyword.END)) {
      throw new IllegalParseException(
          String.format("unexpected jua.token %s, expected end", keyword));
    }

    if (!nested) {
      // consume the END keyword
      parser.consume(Keyword.END);
    }

    return new StatementIf(tok, condition, consequence, alternative);
  }

  @Override
  public boolean matches(Parser parser) {
    return parser.currentToken().isSubtype(Keyword.IF);
  }
}

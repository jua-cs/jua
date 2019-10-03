package jua.parser;

import jua.ast.Expression;
import jua.token.Token;

public interface InfixParser {
  Expression parseInfix(Parser parser, Token tok, Expression lhs) throws IllegalParseException;

  int getPrecedence();
}

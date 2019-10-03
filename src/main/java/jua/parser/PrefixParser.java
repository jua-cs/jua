package jua.parser;

import jua.ast.Expression;
import jua.token.Token;

public interface PrefixParser {
  Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException;
}

package parser;

import ast.Expression;
import token.Token;

public interface PrefixParser {
  Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException;
}

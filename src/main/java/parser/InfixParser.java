package parser;

import ast.Expression;
import token.Token;

public interface InfixParser {
  Expression parseInfix(Parser parser, Token tok, Expression lhs) throws IllegalParseException;
}

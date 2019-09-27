package parser;

import ast.Expression;
import token.Token;

public interface PrefixParser {
    Expression parsePrefix(Token tok);
}

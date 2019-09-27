package parser;

import ast.Expression;
import token.Token;

public interface InfixParser {
    Expression parseInfix(Token tok, Expression lhs);
}

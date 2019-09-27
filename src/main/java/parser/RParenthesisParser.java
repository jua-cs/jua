package parser;

import ast.Expression;
import token.Token;

public class RParenthesisParser implements InfixParser{
    @Override
    public Expression parseInfix(Parser parser, Token tok, Expression lhs) {
        return lhs;
    }
}

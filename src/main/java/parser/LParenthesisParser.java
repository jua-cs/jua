package parser;

import ast.Expression;
import token.Token;

public class LParenthesisParser implements PrefixParser {
    @Override
    public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
        Expression inner = parser.parseExpression();
        return inner;
    }
}

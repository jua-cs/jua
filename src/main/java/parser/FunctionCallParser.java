package parser;

import ast.Expression;
import ast.ExpressionFunctionCall;
import ast.ExpressionIdentifier;
import token.*;

public class FunctionCallParser implements InfixParser {
    @Override
    public Expression parseInfix(Parser parser, Token tok, Expression lhs) throws IllegalParseException {
        // Parser is on the token nxt "(", lhs is the function identifier
        if (!(lhs instanceof ExpressionIdentifier)) {
            throw new IllegalParseException("lhs is not an ExpressionIdentifier but a " + lhs.getClass());
        }
        ExpressionFunctionCall exp = new ExpressionFunctionCall((ExpressionIdentifier) lhs);

        // if there is no args, we look for a ')'
        if (parser.currentToken().isSubtype(Delimiter.RPAREN)) {
                return exp;
        } else {
            exp.addArgument(parser.parseExpression());

            while (parser.currentToken().isSubtype(Delimiter.COMMA)) {
                // Consume ','
                parser.advanceTokens();
                exp.addArgument(parser.parseExpression());
            }
        }

        if (!(parser.currentToken().isSubtype(Delimiter.RPAREN))) {
            throw new IllegalParseException("Function should be closed with a ')'");
        }

        // Consume ')'
        parser.advanceTokens();

        return exp;
    }
}

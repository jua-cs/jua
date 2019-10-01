package parser;

import ast.Expression;
import ast.ExpressionFunctionCall;
import ast.ExpressionIdentifier;
import java.util.ArrayList;
import token.Delimiter;
import token.Token;

public class FunctionCallParser implements InfixParser {

  private final int precedence;

  public FunctionCallParser(int precedence) {
    this.precedence = precedence;
  }

  @Override
  public Expression parseInfix(Parser parser, Token tok, Expression lhs)
      throws IllegalParseException {
    // Parser is on the token nxt "(", lhs is the function identifier
    if (!(lhs instanceof ExpressionIdentifier)) {
      throw new IllegalParseException("lhs is not an ExpressionIdentifier but a " + lhs.getClass());
    }
    ExpressionFunctionCall exp = new ExpressionFunctionCall((ExpressionIdentifier) lhs);

    // if there is no args, we look for a ')'
    if (parser.currentToken().isSubtype(Delimiter.RPAREN)) {
      return exp;
    }

    ArrayList<Expression> args = parser.parseCommaSeparatedExpressions(precedence);
    exp.setArgs(args);

    // Consume ')'
    parser.consume(Delimiter.RPAREN);

    return exp;
  }

  @Override
  public int getPrecedence() {
    return this.precedence;
  }
}

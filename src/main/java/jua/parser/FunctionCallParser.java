package jua.parser;

import java.util.ArrayList;
import jua.ast.Expression;
import jua.ast.ExpressionFactory;
import jua.ast.ExpressionFunctionCall;
import jua.ast.ExpressionIdentifier;
import jua.token.Delimiter;
import jua.token.Token;

public class FunctionCallParser implements InfixParser {

  private final int precedence;

  public FunctionCallParser(int precedence) {
    this.precedence = precedence;
  }

  @Override
  public Expression parseInfix(Parser parser, Token tok, Expression lhs)
      throws IllegalParseException {
    // Parser is on the jua.token nxt "(", lhs is the function identifier
    if (!(lhs instanceof ExpressionIdentifier)) {
      throw new IllegalParseException("lhs is not an ExpressionIdentifier but a " + lhs.getClass());
    }

    ExpressionFunctionCall exp = ExpressionFactory.create((ExpressionIdentifier) lhs);

    // if there is no args, we look for a ')'
    if (parser.currentToken().isSubtype(Delimiter.RPAREN)) {
      parser.consume(Delimiter.RPAREN);
      return exp;
    }

    ArrayList<ExpressionIdentifier> args = parser.parseCommaSeparatedExpressions(0);
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

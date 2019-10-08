package jua.parser;

import java.util.ArrayList;
import jua.ast.*;
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
    if (!(lhs instanceof Variable)) {
      throw new IllegalParseException("lhs is not a Variable but a " + lhs.getClass());
    }

    ExpressionFunctionCall exp =
        ExpressionFactory.create((Variable) lhs, tok.getLine(), tok.getPosition());

    // if there is no args, we look for a ')'
    if (parser.currentToken().isSubtype(Delimiter.RPAREN)) {
      parser.consume(Delimiter.RPAREN);
      return exp;
    }

    ArrayList<Expression> args = parser.parseCommaSeparatedExpressions(0);
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

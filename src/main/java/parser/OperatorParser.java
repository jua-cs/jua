package parser;

import ast.Expression;
import ast.ExpressionFactory;
import token.Token;
import token.TokenOperator;

public class OperatorParser implements PrefixParser, InfixParser {

  private final int precedence;

  public OperatorParser(int precedence) {
    this.precedence = precedence;
  }

  public int getPrecedence() {
    return precedence;
  }

  @Override
  public Expression parseInfix(Parser parser, Token tok, Expression lhs)
      throws IllegalParseException {
    Expression rhs = parser.parseExpression();
    return ExpressionFactory.create((TokenOperator) tok, lhs, rhs);
  }

  @Override
  public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
    Expression rhs = parser.parseExpression();
    return ExpressionFactory.create((TokenOperator) tok, rhs);
  }
}

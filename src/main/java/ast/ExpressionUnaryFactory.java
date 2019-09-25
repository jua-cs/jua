package ast;

import token.TokenOperator;

public class ExpressionUnaryFactory {
  public static ExpressionUnary create(TokenOperator token, Expression value) {
    switch (token.getOperator()) {
      case NEGATIVE:
        return new ExpressionNegative(token, value);
      case HASH:
        return new ExpressionHash(token, value);
      case NOT:
        return new ExpressionNot(token, value);
      default:
        return null;
    }
  }
}

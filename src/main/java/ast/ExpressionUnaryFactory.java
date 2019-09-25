package ast;

import token.TokenOperator;

public class ExpressionUnaryFactory {
  public static ExpressionUnary create(TokenOperator token, Expression value) {
    switch (token.getOperator()) {
      case NEGATIVE:
        // TODO implement
        return null;
      case HASH:
        // TODO implement
        return null;
      case NOT:
        // TODO
        return new ExpressionNot(token, value);
      default:
        return null;
    }
  }
}

package ast;

import token.TokenOperator;

public class ExpressionBinaryFactory {
  public static ExpressionBinary create(
      TokenOperator token, Expression lefValue, Expression rightValue) {
    switch (token.getOperator()) {
      case PLUS:
        return new ExpressionAddition(token, lefValue, rightValue);
      case SLASH:
        return new ExpressionDivision(token, lefValue, rightValue);
      case ASTERISK:
        return new ExpressionMultiplication(token, lefValue, rightValue);
      case MINUS:
        return new ExpressionSubtraction(token, lefValue, rightValue);
      default:
        return null;
    }
  }
}

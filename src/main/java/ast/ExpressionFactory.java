package ast;

import token.Token;
import token.TokenOperator;

public class ExpressionFactory {
  public static ExpressionBinary create(TokenOperator token, Expression lhs, Expression rhs) {
    switch (token.getOperator()) {
      case PLUS:
        return new ExpressionAddition(token, lhs, rhs);
      case MINUS:
        return new ExpressionSubtraction(token, lhs, rhs);
      case ASTERISK:
        return new ExpressionMultiplication(token, lhs, rhs);
      case SLASH:
        return new ExpressionDivision(token, lhs, rhs);
      case CARAT:
        // TODO finish this
        break;
      case PERCENT:
        break;
      case DOT:
        break;
      case ASSIGN:
        break;
      case LT:
        break;
      case GT:
        break;
      case EQUALS:
        break;
      case LTE:
        break;
      case GTE:
        break;
      case NOT_EQUAL:
        break;
      case AND:
        break;
      case OR:
        break;
      case NEGATIVE:
        break;
      case HASH:
        break;
      case NOT:
        break;
      default:
        return null;
    }
    // TODO: removeme
    return null;
  }

  public static ExpressionUnary create(TokenOperator token, Expression value) {
    switch (token.getOperator()) {
      case HASH:
        return new ExpressionHash(token, value);
      case NOT:
        return new ExpressionNot(token, value);
      case MINUS:
        return new ExpressionNegative(token, value);
      default:
        return null;
    }
  }

  public static Expression create(Token token) {
    switch (token.getType()) {
      case LITERAL:
        return new ExpressionLiteral(token);
      case IDENTIFIER:
        return new ExpressionIdentifier(token);
      default:
        return null;
    }
  }
}

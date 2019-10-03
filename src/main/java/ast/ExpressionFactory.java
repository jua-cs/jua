package ast;

import java.util.ArrayList;
import token.*;

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
        return new ExpressionPower(token, lhs, rhs);
      case PERCENT:
        return new ExpressionModulo(token, lhs, rhs);
      case DOT:
        break;
      case ASSIGN:
        // TODO: figure out
        break;
      case LT:
        return new ExpressionLessThan(token, lhs, rhs);
      case GT:
        return new ExpressionGreaterThan(token, lhs, rhs);
      case EQUALS:
        return new ExpressionEquals(token, lhs, rhs);
      case LTE:
        return new ExpressionLessThanOrEqual(token, lhs, rhs);
      case GTE:
        return new ExpressionGreaterThanOrEqual(token, lhs, rhs);
      case NOT_EQUAL:
        return new ExpressionNotEqual(token, lhs, rhs);
      case AND:
        return new ExpressionAnd(token, lhs, rhs);
      case OR:
        return new ExpressionOr(token, lhs, rhs);

        // TODO: exception for those ?
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
    if (token instanceof TokenLiteral) {
      return new ExpressionLiteral(token);
    }
    return new ExpressionIdentifier(token);
  }

  public static ExpressionIdentifier create(TokenIdentifier token) {
    return new ExpressionIdentifier(token);
  }

  public static ExpressionLiteral create(TokenLiteral token) {
    return new ExpressionLiteral(token);
  }

  public static ExpressionFunctionCall create(ExpressionIdentifier identifier) {
    return new ExpressionFunctionCall(TokenFactory.create(identifier.getIdentifier()));
  }

  public static ExpressionFunctionCall create(
      ExpressionIdentifier identifier, ArrayList<Expression> args) {
    return new ExpressionFunctionCall(TokenFactory.create(identifier.getIdentifier()), args);
  }

  public static ExpressionFunction createExpressionFunction(
      Token token, ArrayList<Expression> args, StatementList statements) {
    return new ExpressionFunction(token, args, statements);
  }
}

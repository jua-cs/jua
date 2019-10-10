package jua.ast;

import java.util.ArrayList;
import jua.token.Token;
import jua.token.TokenIdentifier;
import jua.token.TokenLiteral;
import jua.token.TokenOperator;

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
        return new ExpressionAccess(token, lhs, rhs);
      case ASSIGN:
        // TODO: figure out
        break;
      case CONCAT:
        return new ExpressionConcatenation(token, lhs, rhs);
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

  public static ExpressionFunctionCall create(Variable var, int line, int position) {
    return new ExpressionFunctionCall(var, line, position);
  }

  public static ExpressionFunctionCall create(
      Variable var, int line, int position, ArrayList<Expression> args) {
    return new ExpressionFunctionCall(var, line, position, args);
  }

  public static ExpressionFunction createExpressionFunction(
      Token token, ArrayList<ExpressionIdentifier> args, StatementList statements) {
    return new ExpressionFunction(token, args, statements);
  }
}

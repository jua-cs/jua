package jua.ast;

import java.util.ArrayList;
import jua.token.*;

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
      case B_AND:
        return new ExpressionBitwiseAnd(token, lhs, rhs);
      case B_OR:
        return new ExpressionBitwiseOr(token, lhs, rhs);
      case B_XOR:
        return new ExpressionBitwiseXor(token, lhs, rhs);
      case LEFT_SHIFT:
        return new ExpressionLeftShift(token, lhs, rhs);
      case RIGHT_SHIFT:
        return new ExpressionRightShift(token, lhs, rhs);

        // TODO: exception for those ?
      case NEGATIVE:
        break;
      case HASH:
        break;
      case NOT:
        break;
      case B_NOT:
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
      case MINUS: // Correspond to Operator.NEGATIVE
        return new ExpressionNegative(
            TokenFactory.create(Operator.NEGATIVE, token.getLine(), token.getPosition()), value);
      case B_XOR: // Correspond to Operator.B_NOT
        return new ExpressionBinaryNot(
            TokenFactory.create(Operator.B_NOT, token.getLine(), token.getPosition()), value);
      default:
        return null;
    }
  }

  public static Expression create(Token token) {
    if (token instanceof TokenLiteral) {
      return new ExpressionLiteral(token);
    }
    if (token.getLiteral() == TokenIdentifier.VariadicToken) {
      return new ExpressionVararg(token);
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

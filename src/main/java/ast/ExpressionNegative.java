package ast;

import token.Token;
import token.TokenOperator;

public class ExpressionNegative extends ExpressionUnary {

  public ExpressionNegative(TokenOperator tokOp, Expression value) {
    super(tokOp, value);
  }
}

package ast;

import token.Token;
import token.TokenOperator;

public class ExpressionHash extends ExpressionUnary {

  public ExpressionHash(TokenOperator tokOp, Expression value) {
    super(tokOp, value);
  }
}

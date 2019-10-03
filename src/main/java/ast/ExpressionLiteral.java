package ast;

import token.Literal;
import token.Token;
import token.TokenLiteral;

public class ExpressionLiteral extends Expression {
  Literal type;

  ExpressionLiteral(Token token) {
    super(token);
    this.type = ((TokenLiteral) token).getLiteralType();
  }
}

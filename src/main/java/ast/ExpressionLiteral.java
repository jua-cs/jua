package ast;

import evaluator.Evaluator;
import objects.LuaObject;
import token.Literal;
import token.Token;
import token.TokenLiteral;

public class ExpressionLiteral extends Expression {
  Literal type;

  ExpressionLiteral(Token token) {
    super(token);
    this.type = ((TokenLiteral) token).getLiteralType();
  }

  public LuaObject evaluate(Evaluator evaluator) {
    switch (type) {
    }
    return null;
  }
}

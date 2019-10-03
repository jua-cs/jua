package ast;

import evaluator.Evaluator;
import objects.LuaNil;
import objects.LuaNumber;
import objects.LuaObject;
import objects.LuaString;
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
      case NIL:
        return new LuaNil();
      case NUMBER:
        return new LuaNumber(Double.valueOf(this.getLiteral()));
      case STRING:
        return new LuaString(this.getLiteral());
      default:
        return new LuaNil();
    }
  }
}

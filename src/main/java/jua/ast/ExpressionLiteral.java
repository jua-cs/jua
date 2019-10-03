package jua.ast;

import jua.evaluator.Evaluator;
import jua.objects.LuaNil;
import jua.objects.LuaNumber;
import jua.objects.LuaObject;
import jua.objects.LuaString;
import jua.token.Literal;
import jua.token.Token;
import jua.token.TokenLiteral;

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

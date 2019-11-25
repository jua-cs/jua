package jua.ast;

import jua.evaluator.IllegalTypeException;
import jua.evaluator.Scope;
import jua.objects.*;
import jua.token.Literal;
import jua.token.Token;
import jua.token.TokenLiteral;

public class ExpressionLiteral extends Expression {
  Literal type;

  ExpressionLiteral(Token token) {
    super(token);
    this.type = ((TokenLiteral) token).getLiteralType();
  }

  public LuaObject evaluate(Scope scope) throws IllegalTypeException {
    switch (type) {
      case NIL:
        return LuaNil.getInstance();
      case NUMBER:
        return new LuaNumber(Double.valueOf(this.getLiteral()));
      case HEX_NUMBER:
        if (this.getLiteral() == "0x") {
          throw new IllegalTypeException("0x is not a correct hex number.");
        }
        try {
          Long hexAsLong = Long.decode(this.getLiteral());
          return new LuaNumber(hexAsLong.doubleValue());
        } catch (NumberFormatException e) {
          throw new IllegalTypeException(e.getMessage());
        }
      case STRING:
        return new LuaString(this.getLiteral());
      case BOOLEAN:
        String raw = getLiteral();
        if (raw.equals("true")) {
          return LuaBoolean.getLuaBool(true);
        } else if (raw.equals("false")) {
          return LuaBoolean.getLuaBool(false);
        }
        throw new IllegalTypeException(
            String.format("Expected %s to be boolean but isn't true or false", raw));
      default:
        return LuaNil.getInstance();
    }
  }
}

package jua.ast;

import jua.evaluator.IllegalTypeException;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.objects.LuaString;
import jua.objects.LuaTable;
import jua.token.TokenOperator;

public class ExpressionAccess extends ExpressionBinary {
  ExpressionAccess(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    LuaObject table = lhs.evaluate(scope);
    if (!(table instanceof LuaTable)) {
      throw new IllegalTypeException(
          String.format("Can't index LuaObject %s of type %s", table, table.getClass()));
    }

    return ((LuaTable) table).get(new LuaString(rhs.getLiteral()));
  }
}

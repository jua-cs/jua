package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.objects.LuaString;
import jua.objects.LuaTable;
import jua.token.TokenOperator;

public class ExpressionAccess extends ExpressionBinary implements Variable {
  ExpressionAccess(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    LuaObject var = lhs.evaluate(scope);
    LuaTable table = LuaObject.toTable(var);

    return table.get(new LuaString(rhs.getLiteral()));
  }

  @Override
  public void assign(Scope scope, LuaObject value, boolean isLocal) throws LuaRuntimeException {
    LuaObject var = lhs.evaluate(scope);
    LuaTable table = LuaObject.toTable(var);
    table.put(new LuaString(rhs.getLiteral()), value);
  }

  @Override
  public String name() {
    return toString();
  }

  @Override
  public String toString() {
    return String.format("%s.%s", lhs, rhs);
  }
}

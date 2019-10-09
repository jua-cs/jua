package jua.ast;

import jua.evaluator.IllegalTypeException;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaNumber;
import jua.objects.LuaObject;
import jua.objects.LuaString;
import jua.objects.LuaTable;
import jua.token.Operator;
import jua.token.TokenFactory;
import jua.token.TokenOperator;

public class ExpressionHash extends ExpressionUnary {

  ExpressionHash(TokenOperator token, Expression value) {
    super(TokenFactory.create(Operator.HASH, token.getLine(), token.getPosition()), value);
  }

  @Override
  public LuaNumber evaluate(Scope scope) throws LuaRuntimeException {
    LuaObject o = value.evaluate(scope);

    if (o instanceof LuaString) {
      return new LuaNumber((double) ((LuaString) o).getValue().length());
    }

    if (o instanceof LuaTable) {
      return new LuaNumber((double) ((LuaTable) o).size());
    }

    throw new IllegalTypeException(
        String.format("Can't apply operator # on %s of type %s", o, o.getClass()));
  }
}

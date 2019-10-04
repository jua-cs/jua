package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaBoolean;
import jua.objects.LuaNumber;
import jua.objects.LuaObject;
import jua.objects.LuaString;
import jua.token.TokenOperator;

public class ExpressionGreaterThanOrEqual extends ExpressionBinary {
  ExpressionGreaterThanOrEqual(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaBoolean evaluate(Evaluator evaluator) throws LuaRuntimeException {
    LuaObject o1 = lhs.evaluate(evaluator);
    LuaObject o2 = rhs.evaluate(evaluator);
    LuaObject.ensureSameType(o1, o2);

    if (o1 instanceof LuaNumber) {
      return LuaBoolean.getLuaBool(((LuaNumber) o1).getValue() >= ((LuaNumber) o2).getValue());
    }

    if (o1 instanceof LuaString) {
      boolean val = ((LuaString) o1).getValue().compareTo(((LuaString) o2).getValue()) >= 0;
      return LuaBoolean.getLuaBool(val);
    }

    throw new LuaRuntimeException(String.format("Could not evaluate %s >= %s", o1, o2));
  }
}

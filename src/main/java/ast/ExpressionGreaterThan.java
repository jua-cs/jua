package ast;

import evaluator.Evaluator;
import evaluator.LuaRuntimeException;
import objects.LuaBoolean;
import objects.LuaNumber;
import objects.LuaObject;
import objects.LuaString;
import token.TokenOperator;

public class ExpressionGreaterThan extends ExpressionBinary {
  ExpressionGreaterThan(TokenOperator token, Expression lhs, Expression rhs) {
    super(token, lhs, rhs);
  }

  @Override
  public LuaBoolean evaluate(Evaluator evaluator) throws LuaRuntimeException {
    LuaObject o1 = lhs.evaluate(evaluator);
    LuaObject o2 = rhs.evaluate(evaluator);
    LuaObject.ensureSameType(o1, o2);

    if (o1 instanceof LuaNumber) {
      return new LuaBoolean(((LuaNumber) o1).getValue() > ((LuaNumber) o2).getValue());
    }

    if (o1 instanceof LuaString) {
      boolean val = ((LuaString) o1).getValue().compareTo(((LuaString) o2).getValue()) > 0;
      return new LuaBoolean(val);
    }

    throw new LuaRuntimeException(String.format("Could not evaluate %s > %s", o1, o2));
  }
}

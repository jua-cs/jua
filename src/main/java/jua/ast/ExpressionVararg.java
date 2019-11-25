package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.token.Token;

public class ExpressionVararg extends ExpressionIdentifier {

  ExpressionVararg(Token token) {
    super(token);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {

    // when evaluating an ArrayList<Expression>, use util.Util.evaluateExprs to ensure this method
    // is never called
    throw new LuaRuntimeException(String.format("evaluate on ... should never be called"));
  }
}

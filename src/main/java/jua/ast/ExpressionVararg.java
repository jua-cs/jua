package jua.ast;

import jua.evaluator.IllegalCastException;
import jua.evaluator.Scope;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.token.Token;

public class ExpressionVararg extends ExpressionIdentifier {

  ExpressionVararg(Token token) {
    super(token);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws IllegalCastException {
    // TODO
    return LuaNil.getInstance();
  }
}

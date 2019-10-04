package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.objects.LuaReturn;
import jua.token.Token;

public class StatementBreak extends Statement {
  public StatementBreak(Token token) {
    super(token);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    return new LuaReturn(LuaNil.getInstance());
  }
}

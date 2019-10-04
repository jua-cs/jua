package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaBreak;
import jua.objects.LuaObject;
import jua.token.Token;

public class StatementBreak extends Statement {
  public StatementBreak(Token token) {
    super(token);
  }

  @Override
  public LuaObject evaluate(Evaluator evaluator) throws LuaRuntimeException {
    return new LuaBreak();
  }
}

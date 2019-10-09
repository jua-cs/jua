package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.token.Literal;
import jua.token.TokenFactory;

public class StatementEOP extends Statement {
  public StatementEOP() {
    super(TokenFactory.create(Literal.NIL, "nil"));
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    throw new LuaRuntimeException(String.format("Statement EOD should not be evaluated"));
  }
}

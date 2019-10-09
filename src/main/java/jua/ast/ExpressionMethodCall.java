package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;

public class ExpressionMethodCall extends ExpressionFunctionCall {
  private final ExpressionFunctionCall call;

  public ExpressionMethodCall(ExpressionFunctionCall expr) {
    super(expr.func, expr.getLine(), expr.getPosition(), expr.args);
    this.call = expr;
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    // TODO
    return null;
  }
}

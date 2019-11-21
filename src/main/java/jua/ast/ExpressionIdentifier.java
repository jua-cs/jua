package jua.ast;

import jua.evaluator.IllegalCastException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.token.Token;

public class ExpressionIdentifier extends Expression implements Variable {
  private String identifier;

  ExpressionIdentifier(Token token) {
    super(token);
    this.identifier = token.getLiteral();
  }

  public String getIdentifier() {
    return identifier;
  }

  @Override
  public LuaObject evaluate(Scope scope) throws IllegalCastException {
    return scope.getVariable(identifier);
  }

  @Override
  public void assign(Scope scope, LuaObject value, boolean isLocal) {
    if (isLocal) {
      scope.assignLocal(getLiteral(), value);
    } else {
      scope.assign(getLiteral(), value);
    }
  }

  @Override
  public String name() {
    return identifier;
  }
}

package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;
import jua.token.Token;

public class StatementRepeatUntil extends Statement {

  private Expression condition;
  private Statement action;

  public StatementRepeatUntil(Token token, Expression condition, Statement action) {
    super(token);
    this.condition = condition;
    this.action = action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    StatementRepeatUntil that = (StatementRepeatUntil) o;

    if (!condition.equals(that.condition)) return false;
    return action.equals(that.action);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + condition.hashCode();
    result = 31 * result + action.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("repeat\n%s\nuntil %s", util.Util.indent(action.toString()), condition);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    Scope repeatScope = scope.createChild();

    LuaObject ret = LuaNil.getInstance();

    do {
      ret = action.evaluate(repeatScope.createChild());

      if (ret instanceof LuaReturn) {
        return ret;
      }
      if (ret instanceof LuaBreak) {
        // when we encounter a break, "unwrap" it to prevent it from propagating
        return LuaNil.getInstance();
      }
      // until is !while
    } while (!LuaBoolean.valueOf(condition.evaluate(repeatScope)).getValue());

    return ret;
  }
}

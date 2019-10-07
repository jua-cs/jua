package jua.ast;

import java.util.Objects;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;
import jua.token.Token;

public class StatementNumericFor extends StatementFor {

  Expression var;
  Expression limit;
  Expression step;

  public StatementNumericFor(
      Token token,
      ExpressionIdentifier variable,
      Expression var,
      Expression limit,
      Expression step,
      Statement block) {

    super(token, util.Util.createArrayList(variable), block);
    this.var = var;
    this.limit = limit;
    this.step = step;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementNumericFor that = (StatementNumericFor) o;
    return Objects.equals(var, that.var)
        && Objects.equals(limit, that.limit)
        && Objects.equals(step, that.step);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), var, limit, step);
  }

  @Override
  public String toString() {
    return String.format(
        "for %s = %s, %s, %s do\n %s\nend", variables.get(0), var, limit, step, block);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    LuaNumber varValue = LuaNumber.valueOf(var.evaluate(scope));
    LuaNumber limitValue = LuaNumber.valueOf(limit.evaluate(scope));
    LuaNumber stepValue = LuaNumber.valueOf(step.evaluate(scope));

    LuaObject ret = LuaNil.getInstance();

    while ((stepValue.getValue() > 0 && varValue.getValue() <= limitValue.getValue())
        || (stepValue.getValue() <= 0 && varValue.getValue() >= limitValue.getValue())) {
      // TODO: local v = var
      ret = block.evaluate(scope);
      // TODO: local var = var + step

      if (ret instanceof LuaReturn) {
        return ret;
      }
      if (ret instanceof LuaBreak) {
        return LuaNil.getInstance();
      }
    }

    return ret;
  }
}

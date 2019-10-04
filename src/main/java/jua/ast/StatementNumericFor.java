package jua.ast;

import java.util.Objects;
import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaBreak;
import jua.objects.LuaNil;
import jua.objects.LuaNumber;
import jua.objects.LuaObject;
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
  public LuaObject evaluate(Evaluator evaluator) throws LuaRuntimeException {
    LuaNumber varValue = LuaNumber.valueOf(var.evaluate(evaluator));
    LuaNumber limitValue = LuaNumber.valueOf(limit.evaluate(evaluator));
    LuaNumber stepValue = LuaNumber.valueOf(step.evaluate(evaluator));

    LuaObject ret = new LuaNil();

    while ((stepValue.getValue() > 0 && varValue.getValue() <= limitValue.getValue())
        || (stepValue.getValue() <= 0 && varValue.getValue() >= limitValue.getValue())) {
      // TODO: local v = var
      ret = block.evaluate(evaluator);
      // TODO: local var = var + step

      if (ret instanceof LuaBreak) {
        break;
      }
    }

    return ret;
  }
}

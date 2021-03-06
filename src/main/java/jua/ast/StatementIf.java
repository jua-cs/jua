package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaBoolean;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.token.Token;

public class StatementIf extends Statement {
  private Expression condition;
  private Statement consequence;
  private Statement alternative;

  public StatementIf(Token token, Expression condition, Statement consequence) {
    super(token);
    this.condition = condition;
    this.consequence = consequence;
  }

  public StatementIf(
      Token token, Expression condition, Statement consequence, Statement alternative) {
    super(token);
    this.condition = condition;
    this.consequence = consequence;
    this.alternative = alternative;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    StatementIf that = (StatementIf) o;

    if (!condition.equals(that.condition)) return false;
    if (!consequence.equals(that.consequence)) return false;
    return alternative.equals(that.alternative);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + condition.hashCode();
    result = 31 * result + consequence.hashCode();
    result = 31 * result + alternative.hashCode();
    return result;
  }

  @Override
  public String toString() {
    if (alternative != null) {
      return String.format(
          "if %s then\n%s\nelse\n%s\nend",
          condition,
          util.Util.indent(consequence.toString()),
          util.Util.indent(alternative.toString()));
    }
    return String.format(
        "if %s then\n%s\nend", condition, util.Util.indent(consequence.toString()));
  }

  public Expression getCondition() {
    return condition;
  }

  public Statement getConsequence() {
    return consequence;
  }

  public Statement getAlternative() {
    return alternative;
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    Scope ifScope = scope.createChild();

    if (LuaBoolean.valueOf(condition.evaluate(ifScope)).getValue()) {
      return consequence.evaluate(ifScope);
    }
    if (alternative != null) {
      return alternative.evaluate(ifScope);
    }

    return LuaNil.getInstance();
  }
}

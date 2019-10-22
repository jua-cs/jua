package jua.ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;
import jua.token.Token;

public class StatementGenericFor extends StatementFor {
  Expression iterator;
  Expression state;
  Expression var;

  public StatementGenericFor(
      Token token,
      ArrayList<ExpressionIdentifier> variables,
      Expression iterator,
      Expression state,
      Expression var,
      Statement block) {
    super(token, variables, block);
    this.iterator = iterator;
    this.state = state;
    this.var = var;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementGenericFor that = (StatementGenericFor) o;
    return Objects.equals(iterator, that.iterator)
        && Objects.equals(state, that.state)
        && Objects.equals(var, that.var);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), iterator, state, var);
  }

  @Override
  public String toString() {
    return String.format(
        "for %s in %s, %s, %s do\n %s\nend",
        variables.stream().map(Object::toString).collect(Collectors.joining(",")),
        iterator,
        state,
        var,
        block);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    Scope forScope = scope.createChild();

    LuaFunction iteratorValue = LuaFunction.valueOf(iterator.evaluate(forScope));
    LuaObject stateValue = state.evaluate(forScope);
    LuaObject varValue = var.evaluate(forScope);

    LuaObject ret = LuaNil.getInstance();
    while (true) {
      ArrayList<LuaObject> values =
          iteratorValue.evaluate(util.Util.createArrayList(stateValue, varValue)).getValues();

      for (int i = 0; i < variables.size(); i++) {
        String ident = variables.get(i).getIdentifier();
        LuaObject value = LuaNil.getInstance();
        if (i < values.size()) {
          value = values.get(i);
        }
        forScope.assignLocal(ident, value);
      }

      varValue = values.get(0);
      if (varValue == LuaNil.getInstance()) {
        return ret;
      }

      ret = block.evaluate(forScope);

      if (ret instanceof LuaReturn) {
        return ret;
      }
      if (ret instanceof LuaBreak) {
        return LuaNil.getInstance();
      }
    }
  }
}

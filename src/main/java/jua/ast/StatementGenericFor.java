package jua.ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.objects.LuaReturn;
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
    LuaObject iteratorValue = iterator.evaluate(scope);
    LuaObject stateValue = state.evaluate(scope);
    LuaObject varValue = var.evaluate(scope);

    LuaObject ret = LuaNil.getInstance();
    while (true) {
      // TODO:          local var_1, ···, var_n = f(s, var)
      //         var = var_1

      if (varValue instanceof LuaNil) {
        break;
      }

      ret = block.evaluate(scope);

      if (ret instanceof LuaReturn) {
        break;
      }
    }

    return ret;
  }
}

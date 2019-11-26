package jua.ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;
import jua.token.Token;

public class StatementGenericFor extends StatementFor {
  // iterator state var
  ArrayList<Expression> iteratorStateVar;

  public StatementGenericFor(
      Token token,
      ArrayList<ExpressionIdentifier> variables,
      ArrayList<Expression> iteratorStateVar,
      Statement block) {
    super(token, variables, block);
    this.iteratorStateVar = iteratorStateVar;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementGenericFor that = (StatementGenericFor) o;
    return Objects.equals(iteratorStateVar, that.iteratorStateVar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }

  @Override
  public String toString() {
    return String.format(
        "for %s in %s, %s, %s do\n %s\nend",
        variables.stream().map(Object::toString).collect(Collectors.joining(",")), block);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    Scope forScope = scope.createChild();

    ArrayList<LuaObject> evaluatedIteratorStateVar;
    if (iteratorStateVar.size() == 1 && iteratorStateVar.get(0) instanceof ExpressionFunctionCall) {
      evaluatedIteratorStateVar =
          ((ExpressionFunctionCall) iteratorStateVar.get(0)).evaluateNoUnwrap(scope).getValues();
    } else {
      evaluatedIteratorStateVar = util.Util.evaluateExprs(scope, iteratorStateVar);
    }

    LuaFunction iteratorValue = LuaFunction.valueOf(evaluatedIteratorStateVar.get(0));

    LuaObject stateValue = LuaNil.getInstance();
    if (evaluatedIteratorStateVar.size() >= 2) {
      stateValue = evaluatedIteratorStateVar.get(1);
    }
    LuaObject varValue = LuaNil.getInstance();
    if (evaluatedIteratorStateVar.size() >= 3) {
      varValue = evaluatedIteratorStateVar.get(2);
    }

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

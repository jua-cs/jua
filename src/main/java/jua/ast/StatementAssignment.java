package jua.ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.token.Token;

public class StatementAssignment extends Statement {
  boolean isLocal;
  private ArrayList<ExpressionIdentifier> lhs = new ArrayList<>();
  private ArrayList<Expression> rhs = new ArrayList<>();

  public StatementAssignment(Token token, ExpressionIdentifier lhs, Expression rhs) {
    super(token);
    this.lhs.add(lhs);
    this.rhs.add(rhs);
  }

  public StatementAssignment(
      Token token,
      ArrayList<ExpressionIdentifier> lhs,
      ArrayList<Expression> rhs,
      boolean isLocal) {
    super(token);
    this.lhs = lhs;
    this.rhs = rhs;
    this.isLocal = isLocal;
  }

  @Override
  public String toString() {
    return String.format(
        "%s = %s",
        lhs.stream().map(Object::toString).collect(Collectors.joining(", ")),
        rhs.stream().map(Objects::toString).collect(Collectors.joining(", ")));
  }

  public ArrayList<ExpressionIdentifier> getLhs() {
    return lhs;
  }

  public ArrayList<Expression> getRhs() {
    return rhs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementAssignment that = (StatementAssignment) o;
    return isLocal == that.isLocal
        && Objects.equals(lhs, that.lhs)
        && Objects.equals(rhs, that.rhs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), isLocal, lhs, rhs);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    ArrayList<LuaObject> values;
    if (rhs.size() == 1 && rhs.get(0) instanceof ExpressionFunctionCall) {
      values = ((ExpressionFunctionCall) rhs.get(0)).evaluateNoUnwrap(scope).getValues();
    } else {
      values = new ArrayList<>();
      for (Expression expr : rhs) {
        values.add(expr.evaluate(scope));
      }
    }

    for (int i = 0; i < lhs.size(); i++) {
      String ident = lhs.get(i).getIdentifier();
      LuaObject value = LuaNil.getInstance();
      if (i < values.size()) {
        value = values.get(i);
      }
      if (isLocal) {
        scope.assign(ident, value);
      } else {
        scope.assignGlobal(ident, value);
      }
    }
    return LuaNil.getInstance();
  }
}

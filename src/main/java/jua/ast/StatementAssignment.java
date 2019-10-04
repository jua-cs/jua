package jua.ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.token.Token;

public class StatementAssignment extends Statement {
  private ArrayList<ExpressionIdentifier> lhs = new ArrayList<>();
  private ArrayList<Expression> rhs = new ArrayList<>();

  public StatementAssignment(Token token, ExpressionIdentifier lhs, Expression rhs) {
    super(token);
    this.lhs.add(lhs);
    this.rhs.add(rhs);
  }

  public StatementAssignment(
      Token token, ArrayList<ExpressionIdentifier> lhs, ArrayList<Expression> rhs) {
    super(token);
    this.lhs = lhs;
    this.rhs = rhs;
  }

  @Override
  public String toString() {
    return String.format(
        "%s = %s",
        lhs.stream().map(Object::toString).collect(Collectors.joining(", ")),
        rhs.stream().map(Objects::toString).collect(Collectors.joining(", ")));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    StatementAssignment that = (StatementAssignment) o;
    if (!lhs.equals(that.lhs)) return false;
    return rhs.equals(that.rhs);
  }

  public ArrayList<ExpressionIdentifier> getLhs() {
    return lhs;
  }

  public ArrayList<Expression> getRhs() {
    return rhs;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + lhs.hashCode();
    result = 31 * result + rhs.hashCode();
    return result;
  }

  @Override
  public LuaObject evaluate(Evaluator evaluator) throws LuaRuntimeException {
    ArrayList<LuaObject> values = new ArrayList<>();
    for (Expression expr : rhs) {
      values.add(expr.evaluate(evaluator));
    }

    for (int i = 0; i < lhs.size() && i < rhs.size(); i++) {
      evaluator.assignGlobal(lhs.get(i).getIdentifier(), values.get(i));
    }
    return LuaNil.getInstance();
  }
}

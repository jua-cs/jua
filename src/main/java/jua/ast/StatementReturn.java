package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.objects.LuaReturn;
import jua.token.Token;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class StatementReturn extends Statement {
  private ArrayList<Expression> values = new ArrayList<>();

  public StatementReturn(Token token) {
    super(token);
  }

  public StatementReturn(Token token, ArrayList<Expression> values) {
    super(token);
    this.values = values;
  }

  public StatementReturn(Token token, Expression value) {
    super(token);
    this.values = util.Util.createArrayList(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    StatementReturn that = (StatementReturn) o;

    return values.equals(that.values);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + values.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format(
        "return %s", values.stream().map(Objects::toString).collect(Collectors.joining(",")));
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    ArrayList<LuaObject> returnValues = new ArrayList<>();
    for (Expression value : values) {
        returnValues.add(value.evaluate(scope));
    }
    return new LuaReturn(returnValues);
  }
}

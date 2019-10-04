package jua.ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.token.Token;
import util.Tuple;

public class ExpressionTableConstructor extends Expression {

  private ArrayList<Tuple<Expression, Expression>> tuples;

  public ExpressionTableConstructor(Token token, ArrayList<Tuple<Expression, Expression>> tuples) {
    super(token);
    this.tuples = tuples;
  }

  @Override
  public String toString() {
    return "{ " + tuples.stream().map(Objects::toString).collect(Collectors.joining(", ")) + " }";
  }

  public ArrayList<Tuple<Expression, Expression>> getTuples() {
    return tuples;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ExpressionTableConstructor that = (ExpressionTableConstructor) o;
    return Objects.equals(tuples, that.tuples);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), tuples);
  }

  @Override
  public LuaObject evaluate(Evaluator evaluator) throws LuaRuntimeException {
    // TODO: later
    return LuaNil.getInstance();
  }
}

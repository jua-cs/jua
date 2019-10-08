package jua.ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaFunction;
import jua.objects.LuaObject;
import jua.objects.LuaReturn;
import jua.token.TokenFactory;

public class ExpressionFunctionCall extends Expression {

  private Variable func;
  private ArrayList<Expression> args = new ArrayList<>();

  ExpressionFunctionCall(Variable var) {
    // TODO we loose line and position doing this
    super(TokenFactory.create(var.name()));
    func = var;
  }

  ExpressionFunctionCall(Variable var, ArrayList<Expression> args) {
    // TODO we loose line and position doing this
    super(TokenFactory.create(var.name()));
    func = var;
    this.args = args;
  }

  public void addArgument(Expression arg) {
    args.add(arg);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    ExpressionFunctionCall that = (ExpressionFunctionCall) o;
    return func.equals(that.func);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + func.hashCode();
    return result;
  }

  public void setArgs(ArrayList<Expression> args) {
    this.args = args;
  }

  @Override
  public String toString() {
    return func.name()
        + "("
        + args.stream().map(Objects::toString).collect(Collectors.joining(", "))
        + ")";
  }

  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    LuaFunction function = (LuaFunction) func.evaluate(scope);
    return function.evaluateUnwrap(scope, args);
  }

  // This is used publicly only in assignment to support multiple return values
  LuaReturn evaluateNoUnwrap(Scope scope) throws LuaRuntimeException {
    LuaFunction function = (LuaFunction) func.evaluate(scope);
    return function.evaluate(scope, args);
  }
}

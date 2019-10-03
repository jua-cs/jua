package jua.ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.token.Token;

public class ExpressionFunctionCall extends Expression {

  private String functionName;
  private ArrayList<Expression> args;

  ExpressionFunctionCall(Token token) {
    super(token);
    functionName = token.getLiteral();
  }

  ExpressionFunctionCall(Token token, ArrayList<Expression> args) {
    super(token);
    functionName = token.getLiteral();
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
    return functionName.equals(that.functionName);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + functionName.hashCode();
    return result;
  }

  public void setArgs(ArrayList<Expression> args) {
    this.args = args;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append(functionName);
    str.append("(");
    str.append(args.stream().map(Objects::toString).collect(Collectors.joining(",")));
    str.append(")");
    return str.toString();
  }

  @Override
  public LuaObject evaluate(Evaluator evaluator) throws LuaRuntimeException {
    ArrayList<LuaObject> argValues = new ArrayList<>();
    for (Expression arg : this.args) {
      argValues.add(arg.evaluate(evaluator));
    }
    // TODO
    return new LuaNil();
  }
}

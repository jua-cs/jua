package ast;

import token.Token;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExpressionFunctionCall extends ExpressionFunction {

  private String functionName;

  public ExpressionFunctionCall(Token token) {
    super(token);
    functionName = token.getLiteral();
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

}

package ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExpressionFunctionCall extends Expression {

  private ArrayList<Expression> args = new ArrayList<>();

  public ExpressionFunctionCall(ExpressionIdentifier functionName) {
    super(functionName.getToken());
  }

  public ExpressionFunctionCall(ExpressionIdentifier functionName, ArrayList<Expression> args) {
    super(functionName.getToken());
    this.args = args;
  }

  public void addArgument(Expression arg) {
    args.add(arg);
  }

  public void setArgs(ArrayList<Expression> args) {
    this.args = args;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append(token.getLiteral());
    str.append("(");
    str.append(args.stream().map(Objects::toString).collect(Collectors.joining(",")));
    str.append(")");
    return str.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ExpressionFunctionCall that = (ExpressionFunctionCall) o;
    return Objects.equals(args, that.args);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), args);
  }
}

package ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExpressionFunctionCall extends Expression {

  private ArrayList<Expression> args = new ArrayList<>();

  public ExpressionFunctionCall(ExpressionIdentifier functionName) {
    super(functionName.getToken());
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
}

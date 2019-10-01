package ast;

import java.util.ArrayList;

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
    for (Expression arg : args) {
      str.append(arg);
      str.append(",");
    }

    str.deleteCharAt(str.lastIndexOf(","));
    str.append(")");
    return str.toString();
  }
}

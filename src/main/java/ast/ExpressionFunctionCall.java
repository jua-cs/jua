package ast;

import java.util.ArrayList;
import token.TokenIdentifier;

public class ExpressionFunctionCall extends Expression {

  private TokenIdentifier functionName;
  private ArrayList<Expression> args = new ArrayList<>();

  public ExpressionFunctionCall(ExpressionIdentifier functionName) {
    super(functionName.getToken());
  }

  public void addArgument(Expression arg) {
    args.add(arg);
  }
}

package ast;

import token.Token;
import token.TokenIdentifier;

import java.util.ArrayList;

public class ExpressionFunctionCall extends Expression {

  private TokenIdentifier functionName;
  private ArrayList<Expression> args = new ArrayList<>();

  public ExpressionFunctionCall(TokenIdentifier functionName) {
    super(functionName);
  }

  public void addArgument(Expression arg) {
    args.add(arg);
  }
}

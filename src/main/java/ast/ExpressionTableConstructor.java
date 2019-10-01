package ast;

import java.util.ArrayList;
import token.Token;
import util.Tuple;

public class ExpressionTableConstructor extends Expression {

  private ArrayList<Tuple<Expression, Expression>> tuples;

  public ExpressionTableConstructor(Token token, ArrayList<Tuple<Expression, Expression>> tuples) {
    super(token);
    this.tuples = tuples;
  }
}

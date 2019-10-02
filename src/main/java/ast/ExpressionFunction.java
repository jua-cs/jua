package ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import token.Token;

public class ExpressionFunction extends Expression {
  protected ArrayList<Expression> args;
  private StatementList statements;

  ExpressionFunction(Token token) {
    super(token);
    this.args = new ArrayList<>();
    this.statements = new StatementList(token);
  }

  ExpressionFunction(
      Token token, ArrayList<Expression> args, StatementList statements) {
    super(token);
    this.args = args;
    this.statements = statements;
  }

  @Override
  public String toString() {
    return String.format(
        "function(%s)\n%s\nend",
        args.stream().map(Object::toString).collect(Collectors.joining("\n")), statements);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    ExpressionFunction that = (ExpressionFunction) o;

    if (!args.equals(that.args)) return false;
    return statements.equals(that.statements);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + args.hashCode();
    result = 31 * result + statements.hashCode();
    return result;
  }

  public ArrayList<Expression> getArgs() {
    return args;
  }

  public StatementList getStatements() {
    return statements;
  }

  public void addArgument(Expression exp) {
    args.add(exp);
  }

}

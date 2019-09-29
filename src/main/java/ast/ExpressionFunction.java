package ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import token.Token;

public class ExpressionFunction extends Expression {
  private ArrayList<ExpressionIdentifier> args;
  private StatementList statements;

  public ExpressionFunction(
      Token token, ArrayList<ExpressionIdentifier> args, StatementList statements) {
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
    return Objects.equals(args, that.args) && Objects.equals(statements, that.statements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), args, statements);
  }

  public ArrayList<ExpressionIdentifier> getArgs() {
    return args;
  }

  public StatementList getStatements() {
    return statements;
  }
}

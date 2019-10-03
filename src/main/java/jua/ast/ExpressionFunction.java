package jua.ast;

import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.token.Token;

public class ExpressionFunction extends Expression {
  protected ArrayList<ExpressionIdentifier> args;
  private StatementList statements;

  ExpressionFunction(Token token) {
    super(token);
    this.args = new ArrayList<ExpressionIdentifier>();
    this.statements = new StatementList(token);
  }

  ExpressionFunction(Token token, ArrayList<ExpressionIdentifier> args, StatementList statements) {
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

  public ArrayList<ExpressionIdentifier> getArgs() {
    return args;
  }

  public StatementList getStatements() {
    return statements;
  }

  public void addArgument(ExpressionIdentifier exp) {
    args.add(exp);
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

package jua.ast;

import java.util.ArrayList;
import java.util.stream.Collectors;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaFunction;
import jua.objects.LuaObject;
import jua.token.Token;

public class ExpressionFunction extends Expression implements Variable {
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

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    ArrayList<String> argNames = new ArrayList<>();
    for (ExpressionIdentifier arg : this.args) {
      argNames.add(arg.getIdentifier());
    }

    return new LuaFunction(argNames, scope, statements);
  }

  @Override
  public void assign(Scope scope, LuaObject value, boolean isLocal) throws LuaRuntimeException {
    // nothing
  }

  @Override
  public String name() {
    return String.format("function %s", hashCode());
  }
}

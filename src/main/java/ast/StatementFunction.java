package ast;

import java.util.Objects;
import java.util.stream.Collectors;
import token.Token;

public class StatementFunction extends Statement {
  private ExpressionIdentifier name;
  private ExpressionFunction func;

  public StatementFunction(Token token, ExpressionIdentifier name, ExpressionFunction func) {
    super(token);
    this.name = name;
    this.func = func;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementFunction that = (StatementFunction) o;
    return Objects.equals(name, that.name) && Objects.equals(func, that.func);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name, func);
  }

  @Override
  public String toString() {
    return String.format(
        "function %s(%s)\n%s\nend",
        name,
        func.getArgs().stream().map(Object::toString).collect(Collectors.joining("\n")),
        func.getStatements());
  }
}

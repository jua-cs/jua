package jua.ast;

import java.util.Objects;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.token.Token;

public class StatementBlock extends Statement {

  private StatementList list;

  public StatementBlock(Token token, StatementList statement) {
    super(token);
    this.list = statement;
  }

  @Override
  public String toString() {
    return String.format("do\n%s\nend", util.Util.indent(list.toString()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementBlock that = (StatementBlock) o;
    return Objects.equals(list, that.list);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), list);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    return list.evaluate(scope.createChild());
  }
}

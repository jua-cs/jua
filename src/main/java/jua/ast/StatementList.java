package jua.ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaBreak;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.objects.LuaReturn;
import jua.token.Token;

public class StatementList extends Statement {

  private ArrayList<Statement> children = new ArrayList<>();

  public StatementList(Token token) {
    super(token);
  }

  public StatementList(Token token, Statement statement) {
    super(token);

    children.add(statement);
  }

  public void addChild(Statement statement) {
    children.add(statement);
  }

  public ArrayList<Statement> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    return children.stream().map(Object::toString).collect(Collectors.joining("\n"));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementList list = (StatementList) o;
    return Objects.equals(children, list.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), children);
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    LuaObject ret = LuaNil.getInstance();
    for (Statement statement : children) {
      ret = statement.evaluate(scope);

      if (ret instanceof LuaReturn || ret instanceof LuaBreak) {
        //TODO: if we are not actually in a loop here, the behavior is undefined, maybe crash instead in the future.
        return ret;
      }
    }

    return ret;
  }
}

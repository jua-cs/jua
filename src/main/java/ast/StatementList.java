package ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import token.Token;

public class StatementList extends Statement {

  private ArrayList<Statement> children = new ArrayList<>();

  public StatementList() {
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
}

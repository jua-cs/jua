package ast;

import java.util.ArrayList;
import token.Token;

public class StatementList extends Statement {

  private ArrayList<Statement> children = new ArrayList<>();

  public StatementList(Token token) {
    super(token);
  }

  public void addChild(Statement statement) {
    children.add(statement);
  }

  public ArrayList<Statement> getChildren() {
    return children;
  }
}

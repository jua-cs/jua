package ast;

import java.util.ArrayList;
import token.Token;

public abstract class Node {
  private Token token;

  private ArrayList<Node> children = new ArrayList<>();

  public Node(Token token) {
    this.token = token;
  }

  public Token getToken() {
    return token;
  }

  public void addChild(Node node) {
    children.add(node);
  }
}

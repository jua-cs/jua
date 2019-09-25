package ast;

import token.Token;

public abstract class Node {
  private Token token;

  public Node(Token token) {
    this.token = token;
  }

  public Token getToken() {
    return token;
  }

  @Override
  public String toString() {
    return String.format("Node with token: %s", token);
  }
}

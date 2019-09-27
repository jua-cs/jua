package ast;

import java.util.Objects;
import token.Token;

public abstract class Node {
  protected Token token;

  public Node(Token token) {
    this.token = token;
  }

  public Token getToken() {
    return token;
  }

  @Override
  public String toString() {
    return token.getLiteral();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node node = (Node) o;
    return Objects.equals(token, node.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }
}

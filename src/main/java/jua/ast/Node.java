package jua.ast;

import java.util.Objects;
import jua.token.Token;

public abstract class Node {
  private int line;
  private int position;
  private String literal;

  Node() {}

  Node(Token token) {
    this.literal = token.getLiteral();
    this.line = token.getLine();
    this.position = token.getPosition();
  }

  @Override
  public String toString() {
    return this.literal;
  }

  public String getLiteral() {
    return literal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Node node = (Node) o;

    return Objects.equals(literal, node.literal);
  }

  public int getLine() {
    return line;
  }

  public int getPosition() {
    return position;
  }

  @Override
  public int hashCode() {
    return literal != null ? literal.hashCode() : 0;
  }
}

package ast;

import token.Token;

public abstract class Statement extends Node {
  public Statement(Token token) {
    super(token);
  }
}

package ast;

import token.Token;

public abstract class Expression extends Node {

  public Expression(Token token) {
    super(token);
  }
}

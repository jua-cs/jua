package ast;

import token.Token;

public abstract class Statement extends Node {
  Statement(Token token) {
    super(token);
  }
}

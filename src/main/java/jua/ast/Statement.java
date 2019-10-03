package jua.ast;

import jua.token.Token;

public abstract class Statement extends Node {
  Statement(Token token) {
    super(token);
  }
}

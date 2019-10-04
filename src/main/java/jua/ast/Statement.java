package jua.ast;

import jua.token.Token;

public abstract class Statement extends Node implements Evaluatable {
  Statement(Token token) {
    super(token);
  }
}

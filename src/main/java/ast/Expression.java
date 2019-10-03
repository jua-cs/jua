package ast;

import token.Token;

public abstract class Expression extends Node implements Evaluatable {

  Expression(Token token) {
    super(token);
  }
}

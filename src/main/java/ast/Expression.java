package ast;

import token.Token;

public abstract class Expression extends Node {

  Expression(Token token) {
    super(token);
  }

}

package jua.ast;

import jua.evaluator.Evaluable;
import jua.token.Token;

public abstract class Expression extends Node implements Evaluable {

  Expression(Token token) {
    super(token);
  }
}

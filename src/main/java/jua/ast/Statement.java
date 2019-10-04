package jua.ast;

import jua.evaluator.Evaluable;
import jua.token.Token;

public abstract class Statement extends Node implements Evaluable {
  Statement(Token token) {
    super(token);
  }
}

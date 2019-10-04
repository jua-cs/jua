package jua.ast;

import java.util.ArrayList;
import java.util.Objects;
import jua.token.Token;

public abstract class StatementFor extends Statement {
  ArrayList<ExpressionIdentifier> variables;
  Statement block;

  public StatementFor(Token token, ArrayList<ExpressionIdentifier> variables, Statement block) {
    super(token);
    this.variables = variables;
    this.block = block;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementFor that = (StatementFor) o;
    return Objects.equals(variables, that.variables) && Objects.equals(block, that.block);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), variables, block);
  }
}

package ast;

import java.util.ArrayList;
import java.util.Objects;
import token.Token;

public class StatementGenericFor extends StatementFor {
  Expression iterator;
  Expression state;
  Expression var;

  public StatementGenericFor(
      Token token,
      ArrayList<ExpressionIdentifier> variables,
      Statement block,
      Expression iterator,
      Expression state,
      Expression var) {
    super(token, variables, block);
    this.iterator = iterator;
    this.state = state;
    this.var = var;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StatementGenericFor that = (StatementGenericFor) o;
    return Objects.equals(iterator, that.iterator)
        && Objects.equals(state, that.state)
        && Objects.equals(var, that.var);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), iterator, state, var);
  }
}

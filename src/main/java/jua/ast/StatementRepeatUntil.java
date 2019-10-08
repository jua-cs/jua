package jua.ast;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.token.Token;


public class StatementRepeatUntil extends Statement {

    private Expression condition;
    private Statement consequence;

    public StatementRepeatUntil(Token token, Expression condition, Statement consequence) {
        super(token);
        this.condition = condition;
        this.consequence = consequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StatementRepeatUntil that = (StatementRepeatUntil) o;

        if (!condition.equals(that.condition)) return false;
        return consequence.equals(that.consequence);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + condition.hashCode();
        result = 31 * result + consequence.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "repeat\n%s\nuntil %s", util.Util.indent(consequence.toString()), condition);
    }

    @Override
    public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
        return null;
    }
}

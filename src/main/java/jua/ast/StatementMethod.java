package jua.ast;

import java.util.stream.Collectors;
import jua.evaluator.IllegalTypeException;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaFunction;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.objects.LuaTable;
import jua.token.Token;

public class StatementMethod extends Statement {
  private Variable funcVar;
  private String name;
  private ExpressionFunction func;

  public StatementMethod(Token token, Variable funcVar, String name, ExpressionFunction func) {
    super(token);
    this.name = name;
    this.funcVar = funcVar;
    this.func = func;
  }

  @Override
  public String toString() {
    return String.format(
        "function %s:%s(%s)\n%s\nend",
        funcVar.name(),
        name,
        func.getArgs().stream().map(Object::toString).collect(Collectors.joining("\n")),
        util.Util.indent(func.getStatements().toString()));
  }

  @Override
  public LuaObject evaluate(Scope scope) throws LuaRuntimeException {
    LuaObject table = funcVar.evaluate(scope);
    if (!(table instanceof LuaTable)) {
      throw new IllegalTypeException(
          String.format(
              "Can't index LuaObject %s of type %s in method execution", table, table.getClass()));
    }

    LuaTable self = (LuaTable) table;

    LuaFunction method = func.evaluate(scope);
    method.setSelf(self);
    self.put(name, method);

    return LuaNil.getInstance();
  }
}

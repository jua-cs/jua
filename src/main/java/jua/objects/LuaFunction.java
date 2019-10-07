package jua.objects;

import java.util.ArrayList;
import jua.ast.Expression;
import jua.ast.StatementList;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;

public class LuaFunction implements LuaObject {
  protected ArrayList<String> argNames;
  protected Scope environment;
  StatementList block;

  public LuaFunction(ArrayList<String> argNames, Scope environment, StatementList block) {
    this.argNames = argNames;
    this.environment = environment;
    this.block = block;
  }

  @Override
  public String repr() {
    return String.format("function(%s) %s", argNames, block);
  }

  public LuaObject evaluate(Scope scope, ArrayList<Expression> args) throws LuaRuntimeException {
    LuaReturn ret = evaluateNoUnwrap(scope, args);

    // only unwrap a LuaReturn if we reach a function call
    LuaObject value = ret.getValues().get(0);
    return value;
  }

  public LuaReturn evaluateNoUnwrap(Scope scope, ArrayList<Expression> args) throws LuaRuntimeException {
    Scope funcScope = this.environment.createChild();

    // Init args to nil
    argNames.forEach(arg -> funcScope.assign(arg, LuaNil.getInstance()));

    // Evaluate each arg
    ArrayList<LuaObject> evaluatedArgs = new ArrayList<>();
    for (Expression arg : args) {
      evaluatedArgs.add(arg.evaluate(scope));
    }

    // Assign evaluated args to arg names
    for (int i = 0; i < Math.min(argNames.size(), args.size()); i++) {
      funcScope.assign(argNames.get(i), evaluatedArgs.get(i));
    }

    LuaObject ret = block.evaluate(funcScope);
    if (ret instanceof LuaReturn) {
      return (LuaReturn) ret;
    }

    return new LuaReturn(util.Util.createArrayList(ret));
  }
}

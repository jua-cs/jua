package jua.objects;

import java.util.ArrayList;
import jua.ast.Expression;
import jua.ast.StatementList;
import jua.evaluator.IllegalCastException;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;

public class LuaFunction implements LuaObject, Function {
  private ArrayList<String> argNames;
  private Scope environment;
  private StatementList block;

  public LuaFunction(ArrayList<String> argNames, Scope environment, StatementList block) {
    this.argNames = argNames;
    this.environment = environment;
    this.block = block;
  }

  public static LuaFunction valueOf(LuaObject o) throws IllegalCastException {
    if (o instanceof LuaFunction) {
      return (LuaFunction) o;
    }
    throw new IllegalCastException(String.format("%s is not a function", o.repr()));
  }

  @Override
  public String repr() {
    return String.format("function(%s) %s", argNames, block);
  }

  public LuaObject evaluateUnwrap(Scope scope, ArrayList<Expression> args)
      throws LuaRuntimeException {
    LuaReturn ret = evaluate(scope, args);

    // only unwrap a LuaReturn if we reach a function call
    return ret.getValues().get(0);
  }

  public LuaReturn evaluate(Scope scope, ArrayList<Expression> args) throws LuaRuntimeException {
    // Evaluate each arg
    ArrayList<LuaObject> evaluatedArgs = new ArrayList<>();
    for (Expression arg : args) {
      evaluatedArgs.add(arg.evaluate(scope));
    }

    return evaluate(evaluatedArgs);
  }

  public LuaReturn evaluate(ArrayList<LuaObject> args) throws LuaRuntimeException {
    Scope funcScope = this.environment.createChild();

    // Init args to nil
    argNames.forEach(arg -> funcScope.assignLocal(arg, LuaNil.getInstance()));

    // Assign evaluated args to arg names
    for (int i = 0; i < Math.min(argNames.size(), args.size()); i++) {
      funcScope.assignLocal(argNames.get(i), args.get(i));
    }

    LuaObject ret = block.evaluate(funcScope);
    if (ret instanceof LuaReturn) {
      return (LuaReturn) ret;
    }

    return new LuaReturn(ret);
  }
}

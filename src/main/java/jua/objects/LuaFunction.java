package jua.objects;

import java.util.ArrayList;
import jua.ast.StatementList;
import jua.evaluator.Scope;

public class LuaFunction implements LuaObject {
  ArrayList<String> argNames;
  Scope environment;
  StatementList block;

  public LuaFunction(ArrayList<String> argNames, Scope environment, StatementList block) {
    this.argNames = argNames;
    this.environment = environment;
    this.block = block;
  }

  public ArrayList<String> getArgNames() {
    return argNames;
  }

  public Scope getEnvironment() {
    return environment;
  }

  public StatementList getBlock() {
    return block;
  }

  @Override
  public String repr() {
    return String.format("function(%s) %s", argNames, block);
  }
}

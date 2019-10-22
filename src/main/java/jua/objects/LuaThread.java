package jua.objects;

import jua.evaluator.LuaRuntimeException;

import java.util.ArrayList;
import java.util.function.ToDoubleBiFunction;

public class LuaThread implements LuaObject {

  private LuaFunction f;
  private String status;

  public LuaThread(LuaFunction f) {
    this.f = f;
  }

  @Override
  public String repr() {
    return "";
  }

  public String getStatus() {
    return status;
  }

  public LuaReturn resume(ArrayList<LuaObject> args) throws LuaRuntimeException {
    // TODO
    return new LuaReturn(LuaNil.getInstance());
  }

  public LuaReturn yield(ArrayList<LuaObject> args) throws LuaRuntimeException {
    // TODO
    return new LuaReturn(LuaNil.getInstance());
  }
}

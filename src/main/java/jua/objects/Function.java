package jua.objects;

import java.util.ArrayList;
import jua.evaluator.LuaRuntimeException;

public interface Function {
  public LuaReturn evaluate(ArrayList<LuaObject> args) throws LuaRuntimeException;
}

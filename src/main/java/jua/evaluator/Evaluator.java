package jua.evaluator;

import java.util.HashMap;
import jua.objects.LuaNil;
import jua.objects.LuaObject;

public class Evaluator {
  HashMap<String, LuaObject> scope = new HashMap<>();

  Evaluator parent;

  public Evaluator() {}

  public Evaluator(Evaluator parent) {
    this.parent = parent;
  }

  public LuaObject getVariable(String identifier) {
    LuaObject variable = scope.getOrDefault(identifier, LuaNil.getInstance());
    if (variable == LuaNil.getInstance() && parent != null) {
      variable = parent.getVariable(identifier);
    }

    return variable;
  }

  public void assign(String identifier, LuaObject value) {
    scope.put(identifier, value);
  }

  public void assignGlobal(String identifer, LuaObject value) {
    if (parent != null) {
      parent.assignGlobal(identifer, value);
    }
    assign(identifer, value);
  }
}

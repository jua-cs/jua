package jua.evaluator;

import java.util.HashMap;
import jua.objects.LuaNil;
import jua.objects.LuaObject;

public class Scope {
  HashMap<String, LuaObject> scope = new HashMap<>();

  Scope parent;

  public Scope() {}

  public Scope(Scope parent) {
    this.parent = parent;
  }

  public Scope createChild() {
    return new Scope(this);
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

  public void assignGlobal(String identifier, LuaObject value) {
    if (parent != null) {
      parent.assignGlobal(identifier, value);
    }
    assign(identifier, value);
  }
}

package jua.evaluator;

import java.io.OutputStream;
import java.util.HashMap;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.objects.builtins.Print;
import jua.objects.builtins.Tables;

public class Scope {
  HashMap<String, LuaObject> scope = new HashMap<>();

  Scope parent;

  public Scope() {
    registerBuiltins(System.out);
  }

  public Scope(OutputStream out) {
    registerBuiltins(out);
  }

  public Scope(Scope parent) {
    this.parent = parent;
  }

  private void registerBuiltins(OutputStream out) {
    scope.put("print", new Print(this, out));
    scope.put("table", Tables.create(this));
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

  @Override
  public String toString() {
    return "Scope{" + "scope=" + scope + ", parent=" + parent + '}';
  }
}

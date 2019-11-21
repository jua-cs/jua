package jua.evaluator;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.objects.builtins.Builtin;

public class Scope {
  public static final String SELF = "self";
  private HashMap<String, LuaObject> scope = new HashMap<>();
  private HashSet<String> locals = new HashSet<>();

  private Scope parent;

  public Scope() {
    this.parent = Builtin.createScope();
  }

  public Scope(boolean withBuiltins) {
    if (withBuiltins) {
      this.parent = Builtin.createScope();
    }
  }

  public Scope(OutputStream out) {
    this.parent = Builtin.createScope(out);
  }

  public Scope(Scope parent) {
    this.parent = parent;
  }

  public Scope createChild() {
    return new Scope(this);
  }

  public LuaObject getVariable(String identifier) {
    LuaObject variable = scope.get(identifier);

    // Don't check if variable is nil here because it could be nil in an inner scope in a legitimate
    // way
    if (variable == null && parent != null) {
      variable = parent.getVariable(identifier);
    }

    return variable == null ? LuaNil.getInstance() : variable;
  }

  public void assignSelf(LuaObject value) {
    assignLocal(SELF, value);
  }

  public void assignLocal(String identifier, LuaObject value) {
    scope.put(identifier, value);
    locals.add(identifier);
  }

  public void assign(String identifier, LuaObject value) {
    // Check if the variable isn't local first
    if (locals.contains(identifier) || parent == null) {
      assignLocal(identifier, value);
    } else {
      parent.assign(identifier, value);
    }
  }

  @Override
  public String toString() {
    return "Scope{" + "scope=" + scope + '}';
  }
}

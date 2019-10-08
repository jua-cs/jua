package jua.objects.builtins;

import java.io.OutputStream;
import jua.evaluator.Scope;

public class Builtin {
  public static Scope createScope() {
    return createScope(System.out);
  }

  public static Scope createScope(OutputStream out) {
    Scope scope = new Scope(false);
    scope.assign(Print.name, new Print(scope, out));
    scope.assign(Tables.name, Tables.create(scope));
    return scope;
  }
}

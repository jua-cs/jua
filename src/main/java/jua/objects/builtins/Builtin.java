package jua.objects.builtins;

import java.io.OutputStream;
import jua.evaluator.Scope;

public class Builtin {
  public static Scope createScope() {
    return createScope(System.out);
  }

  public static Scope createScope(OutputStream out) {
    Scope scope = new Scope(false);
    Print.register(scope, out);
    Tables.register(scope);
    Cast.register(scope);
    return scope;
  }
}

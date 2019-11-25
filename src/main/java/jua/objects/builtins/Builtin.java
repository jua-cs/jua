package jua.objects.builtins;

import java.io.OutputStream;
import java.util.ArrayList;
import jua.evaluator.Scope;
import jua.objects.Function;
import jua.objects.LuaNil;
import jua.objects.LuaObject;

public class Builtin {
  public static Scope createScope() {
    return createScope(System.out);
  }

  public static LuaObject arg(ArrayList<LuaObject> args, int idx) {
    return args.size() > idx ? args.get(idx) : LuaNil.getInstance();
  }

  public static Scope createScope(OutputStream out) {
    Scope scope = new Scope(false);
    Print.register(scope, out);
    Tables.register(scope);
    Cast.register(scope);
    Bits.register(scope);
    Next.register(scope);
    Strings.register(scope);
    return scope;
  }

  public static BuiltinFunction createFunction(Function f) {
    return new BuiltinFunction(f);
  }
}

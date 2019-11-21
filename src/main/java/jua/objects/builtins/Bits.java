package jua.objects.builtins;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;

class Bits {
  private static final String name = "bit";

  static void register(Scope scope) {
    LuaTable builtins = new LuaTable();

    // Reference: https://bitop.luajit.org/api.html
    builtins.put(
        "bnot",
        Builtin.createFunction(
            args -> {
              LuaObject x = args.size() > 0 ? args.get(0) : LuaNil.getInstance();

              if (!(x instanceof LuaNumber)) {
                throw new LuaRuntimeException(
                    String.format(
                        "%s.bnot need LuaNumber as first argument, not %s of type %s",
                        name, x, x.getClass()));
              }

              return new LuaReturn(((LuaNumber) x).bNot());
            }));

    builtins.put(
        "bor",
        Builtin.createFunction(
            args -> {
              LuaObject x = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              LuaObject y = args.size() > 1 ? args.get(1) : LuaNil.getInstance();

              if (!(x instanceof LuaNumber)) {
                throw new LuaRuntimeException(
                    String.format(
                        "%s.bor need LuaNumber as first argument, not %s of type %s",
                        name, x, x.getClass()));
              }
              if (!(y instanceof LuaNumber)) {
                throw new LuaRuntimeException(
                    String.format(
                        "%s.bor need LuaNumber as second argument, not %s of type %s",
                        name, y, y.getClass()));
              }

              return new LuaReturn(((LuaNumber) x).bOr((LuaNumber) y));
            }));

    builtins.put(
        "band",
        Builtin.createFunction(
            args -> {
              LuaObject x = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              LuaObject y = args.size() > 1 ? args.get(1) : LuaNil.getInstance();

              if (!(x instanceof LuaNumber)) {
                throw new LuaRuntimeException(
                    String.format(
                        "%s.band need LuaNumber as first argument, not %s of type %s",
                        name, x, x.getClass()));
              }
              if (!(y instanceof LuaNumber)) {
                throw new LuaRuntimeException(
                    String.format(
                        "%s.band need LuaNumber as second argument, not %s of type %s",
                        name, y, y.getClass()));
              }

              return new LuaReturn(((LuaNumber) x).bAnd((LuaNumber) y));
            }));

    builtins.put(
        "bxor",
        Builtin.createFunction(
            args -> {
              LuaObject x = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              LuaObject y = args.size() > 1 ? args.get(1) : LuaNil.getInstance();

              if (!(x instanceof LuaNumber)) {
                throw new LuaRuntimeException(
                    String.format(
                        "%s.bxor need LuaNumber as first argument, not %s of type %s",
                        name, x, x.getClass()));
              }
              if (!(y instanceof LuaNumber)) {
                throw new LuaRuntimeException(
                    String.format(
                        "%s.bxor need LuaNumber as second argument, not %s of type %s",
                        name, y, y.getClass()));
              }

              return new LuaReturn(((LuaNumber) x).bXor((LuaNumber) y));
            }));

    builtins.put(
        "lshift",
        Builtin.createFunction(
            args -> {
              LuaObject x = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              LuaObject y = args.size() > 1 ? args.get(1) : LuaNil.getInstance();

              if (!(x instanceof LuaNumber)) {
                throw new LuaRuntimeException(
                    String.format(
                        "%s.lshift need LuaNumber as first argument, not %s of type %s",
                        name, x, x.getClass()));
              }
              if (!(y instanceof LuaNumber)) {
                throw new LuaRuntimeException(
                    String.format(
                        "%s.lshift need LuaNumber as second argument, not %s of type %s",
                        name, y, y.getClass()));
              }

              return new LuaReturn(((LuaNumber) x).leftShift((LuaNumber) y));
            }));

    builtins.put(
        "rshift",
        Builtin.createFunction(
            args -> {
              LuaObject x = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              LuaObject y = args.size() > 1 ? args.get(1) : LuaNil.getInstance();

              if (!(x instanceof LuaNumber)) {
                throw new LuaRuntimeException(
                    String.format(
                        "%s.rshift need LuaNumber as first argument, not %s of type %s",
                        name, x, x.getClass()));
              }
              if (!(y instanceof LuaNumber)) {
                throw new LuaRuntimeException(
                    String.format(
                        "%s.rshift need LuaNumber as second argument, not %s of type %s",
                        name, y, y.getClass()));
              }

              return new LuaReturn(((LuaNumber) x).rightShift((LuaNumber) y));
            }));

    scope.assign(name, builtins);
  }
}

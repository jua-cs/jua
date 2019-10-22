package jua.objects.builtins;

import java.util.ArrayList;
import java.util.List;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;

public class Coroutine {
  private static final String name = "coroutine";

  static void register(Scope scope) {
    LuaTable builtins = new LuaTable();

    builtins.put(
        "create",
        Builtin.createFunction(
            args -> {
              LuaObject f = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              if (!(f instanceof LuaFunction)) {
                throw new LuaRuntimeException(
                    String.format(
                        "Cannot create coroutine: expected object of type LuaFunction, got %s of type %s",
                        f, f.getClass()));
              }
              // TODO
              return new LuaReturn(LuaNil.getInstance());
            }));

    builtins.put(
        "resume",
        Builtin.createFunction(
            args -> {
              LuaObject co = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              List<LuaObject> values =
                  args.size() > 0 ? args.subList(1, args.size()) : new ArrayList<LuaObject>();
              if (!(co instanceof LuaThread)) {
                throw new LuaRuntimeException(
                    String.format(
                        "Cannot resume thread: expected object of type LuaThread, got %s of type %s",
                        co, co.getClass()));
              }
              // TODO
              return new LuaReturn(LuaNil.getInstance());
            }));

    builtins.put(
        "running",
        Builtin.createFunction(
            args -> {
              // TODO
              return new LuaReturn(LuaNil.getInstance());
            }));

    builtins.put(
        "status",
        Builtin.createFunction(
            args -> {
              LuaObject co = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              if (!(co instanceof LuaThread)) {
                throw new LuaRuntimeException(
                    String.format(
                        "Cannot get thread status: expected object of type LuaThread, got %s of type %s",
                        co, co.getClass()));
              }
              // TODO
              return new LuaReturn(LuaNil.getInstance());
            }));

    builtins.put(
        "wrap",
        Builtin.createFunction(
            args -> {
              LuaObject f = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              if (!(f instanceof LuaFunction)) {
                throw new LuaRuntimeException(
                    String.format(
                        "Cannot wrap create coroutine: expected object of type LuaFunction, got %s of type %s",
                        f, f.getClass()));
              }
              // TODO
              return new LuaReturn(LuaNil.getInstance());
            }));

    builtins.put(
        "yield",
        Builtin.createFunction(
            args -> {
              // TODO
              return new LuaReturn(LuaNil.getInstance());
            }));

    scope.assign(name, builtins);
  }
}

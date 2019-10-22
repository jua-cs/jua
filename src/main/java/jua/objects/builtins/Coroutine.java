package jua.objects.builtins;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.*;

public class Coroutine {
  private static final String name = "coroutine";

  private static final Stack<LuaThread> running = new Stack<>();

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
              return new LuaReturn(new LuaThread((LuaFunction) f));
            }));

    builtins.put(
        "resume",
        Builtin.createFunction(
            args -> {
              LuaObject co = args.size() > 0 ? args.get(0) : LuaNil.getInstance();
              List<LuaObject> values =
                  args.size() > 0 ? args.subList(1, args.size()) : new ArrayList<>();

              if (!(co instanceof LuaThread)) {
                throw new LuaRuntimeException(
                    String.format(
                        "Cannot resume thread: expected object of type LuaThread, got %s of type %s",
                        co, co.getClass()));
              }
              running.push((LuaThread) co);
              return ((LuaThread) co)
                  .resume(util.Util.createArrayList((LuaObject[]) values.toArray()));
            }));

    builtins.put(
        "running",
        Builtin.createFunction(
            (args) -> new LuaReturn(running.empty() ? LuaNil.getInstance() : running.peek())));

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
              return new LuaReturn(new LuaString(((LuaThread) co).getStatus()));
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
              return new LuaReturn(new CoroutineWrap(new LuaThread((LuaFunction) f)));
            }));

    builtins.put(
        "yield",
        Builtin.createFunction(
            args -> {
              if (running.isEmpty()) {
                throw new LuaRuntimeException(
                    String.format("cannot yield from outside a coroutine"));
              }
              return running.pop().yield(args);
            }));

    scope.assign(name, builtins);
  }
}

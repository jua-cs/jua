package jua.objects.builtins;

import java.io.IOException;
import java.io.OutputStream;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.objects.LuaReturn;

class Print extends BuiltinFunction {

  private Print(OutputStream out) {
    super(
        args -> {
          for (LuaObject arg : args) {
            try {
              out.write(arg.repr().getBytes());
              out.write('\n');
              out.flush();
            } catch (IOException e) {
              throw new LuaRuntimeException("could not write to output stream in `print` builtin");
            }
          }

          return new LuaReturn();
        });
  }

  static void register(Scope scope, OutputStream out) {
    scope.assignLocal("print", new Print(out));
  }
}

package jua.objects.builtins;

import jua.objects.Function;
import jua.objects.LuaThread;

public class CoroutineWrap extends BuiltinFunction {
  CoroutineWrap(LuaThread co) {
    super(args -> co.resume(args));
  }
}

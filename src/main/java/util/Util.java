package util;

import jua.ast.Expression;
import jua.ast.ExpressionVararg;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.objects.LuaObject;
import jua.objects.LuaTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Util {

  @SafeVarargs
  public static <T> ArrayList<T> createArrayList(T... elements) {
    ArrayList<T> a = new ArrayList<>();
    Collections.addAll(a, elements);

    return a;
  }

  public static String indent(String str) {
    return new ArrayList<String>(Arrays.asList(str.split("\n")))
        .stream().map(s -> "\t" + s).collect(Collectors.joining("\n"));
  }


  public static ArrayList<LuaObject> evaluateExprs(Scope scope, ArrayList<Expression> exprs) throws LuaRuntimeException {
    ArrayList<LuaObject> evaluateExprs = new ArrayList<>();
    for (Expression expr : exprs) {
      if (expr instanceof ExpressionVararg) {
        LuaObject obj = scope.getVariable("...");
        if (!(obj instanceof LuaTable)) {
          throw new LuaRuntimeException(String.format("vararg argument should be a lua table"));
        }
        LuaTable vararg = (LuaTable) obj;
        evaluateExprs.addAll(vararg.listValues());
        continue;
      }
      evaluateExprs.add(expr.evaluate(scope));
    }
    
    return evaluateExprs;
  }

}
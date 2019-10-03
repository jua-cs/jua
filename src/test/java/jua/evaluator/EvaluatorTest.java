package jua.evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import jua.ast.Statement;
import jua.ast.StatementExpression;
import jua.lexer.Lexer;
import jua.objects.LuaObject;
import jua.parser.IllegalParseException;
import jua.parser.Parser;
import org.junit.jupiter.api.Test;
import util.Tuple;

public class EvaluatorTest {
  private static ArrayList<Statement> setup(String in) throws IllegalParseException {
    return new Parser((new Lexer(in)).getNTokens(0)).parse();
  }

  private LuaObject setupExpr(String in) throws LuaRuntimeException, IllegalParseException {
    return setupExpr(in, new Evaluator());
  }

  private LuaObject setupExpr(String in, Evaluator evaluator)
      throws LuaRuntimeException, IllegalParseException {
    var expr =
        ((StatementExpression) new Parser((new Lexer(in)).getNTokens(0)).parse().get(0)).getExpr();
    return expr.evaluate(evaluator);
  }

  @Test
  void testConcatExpr() throws IllegalParseException, LuaRuntimeException {
    // TODO: test with identifiers

    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("'abc' .. 'def'", "abcdef"));
    tests.add(new Tuple<>("'' .. ''", ""));
    tests.add(new Tuple<>("'a' .. \"b\"", "ab"));

    for (Tuple<String, String> t : tests) {
      var obj = setupExpr(t.x);
      assertEquals(t.y, obj.toString());
    }
  }
}

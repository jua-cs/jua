package jua.evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import jua.ast.Statement;
import jua.ast.StatementExpression;
import jua.lexer.Lexer;
import jua.objects.LuaBoolean;
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
    tests.add(new Tuple<>("5 .. \"b\"", "5b"));
    tests.add(new Tuple<>("100 .. 60", "10060"));
    // TODO: this should be 3.0m and not 3m
    tests.add(new Tuple<>("3.0 .. 'm'", "3m"));
    tests.add(new Tuple<>("'a' .. ('b' .. 'c')", "abc"));
    tests.add(new Tuple<>("'a' .. 50 * 30 / 2 % 4 ^ 7.2 / 3", "a250"));

    for (Tuple<String, String> t : tests) {
      var obj = setupExpr(t.x);
      assertEquals(t.y, obj.repr(), t.x);
    }

    assertThrows(LuaRuntimeException.class, () -> setupExpr("'abc' .. nil"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("5 .. {}"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("'abc' .. {}"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("'abc' .. (function() end)"));
  }

  @Test
  void testArithmeticExpr() throws IllegalParseException, LuaRuntimeException {
    // TODO: test with identifiers

    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("1 + 1", "2"));
    tests.add(new Tuple<>("1 + '1'", "2"));
    tests.add(new Tuple<>("1 - '1'", "0"));
    tests.add(new Tuple<>("2 * '2'", "4"));
    tests.add(new Tuple<>("4 / 2", "2"));
    tests.add(new Tuple<>("3 / '2'", "1.5"));
    tests.add(new Tuple<>("3 % 2", "1"));
    tests.add(new Tuple<>("3.7 % 1.5", "0.7"));
    tests.add(new Tuple<>("-3", "-3"));

    for (Tuple<String, String> t : tests) {
      var obj = setupExpr(t.x);
      assertEquals(t.y, obj.repr());
    }

    assertThrows(LuaRuntimeException.class, () -> setupExpr("'a' + 3"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("nil + 3"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("'a' - 3"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("nil - 3"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("'a' * 3"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("nil * 3"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("'a' / 3"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("nil / 3"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("'a' % 3"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("nil % 3"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("-'a'"));
    assertThrows(LuaRuntimeException.class, () -> setupExpr("-nil"));
  }

  @Test
  void testEqualAndNotEquals() throws IllegalParseException, LuaRuntimeException {
    // TODO: test with identifiers

    ArrayList<Tuple<String, Boolean>> tests = new ArrayList<>();
    tests.add(new Tuple<>("'abc' == 'def'", false));
    tests.add(new Tuple<>("'abc' ~= 'def'", true));
    tests.add(new Tuple<>("'' == ''", true));
    tests.add(new Tuple<>("'a' == \"a\"", true));
    tests.add(new Tuple<>("'a' == \"b\"", false));
    tests.add(new Tuple<>("100 == 60", false));
    tests.add(new Tuple<>("1.0 == 1", true));
    tests.add(new Tuple<>("100 ~= 60", true));
    tests.add(new Tuple<>("0 == false", false));
    tests.add(new Tuple<>("0 == nil", false));
    tests.add(new Tuple<>("nil == nil", true));
    tests.add(new Tuple<>("3 * 2 == 12 / 2", true));
    tests.add(new Tuple<>("true == true", true));
    tests.add(new Tuple<>("not true == false", true));
    tests.add(new Tuple<>("not true ~= not false", true));
    tests.add(new Tuple<>("not not not true", false));

    for (Tuple<String, Boolean> t : tests) {
      var obj = setupExpr(t.x);
      assertEquals(t.y, ((LuaBoolean) obj).getValue(), t.x);
    }
  }
}

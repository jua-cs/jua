package jua.evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import jua.ast.Statement;
import jua.lexer.Lexer;
import jua.objects.LuaBoolean;
import jua.objects.LuaObject;
import jua.parser.IllegalParseException;
import jua.parser.Parser;
import org.junit.jupiter.api.Test;
import util.Tuple;

public class EvaluatorTest {

  private static ArrayList<Statement> setup(String in) throws IllegalParseException {
    return new Parser((new Lexer(in)).getNTokens(0)).parse().getChildren();
  }

  private LuaObject setupEval(String in) throws LuaRuntimeException, IllegalParseException {
    return setupEval(in, new Evaluator());
  }

  private LuaObject setupEval(String in, Evaluator evaluator)
      throws LuaRuntimeException, IllegalParseException {
    Evaluable program = new Parser((new Lexer(in)).getNTokens(0)).parse();
    return program.evaluate(evaluator);
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
    // TODO: this should be a250.0
    tests.add(new Tuple<>("'a' .. 50 * 30 / 2 % 4 ^ 7.2 / 3", "a250"));

    for (Tuple<String, String> t : tests) {
      var obj = setupEval(t.x);
      assertEquals(t.y, obj.repr(), t.x);
    }

    assertThrows(LuaRuntimeException.class, () -> setupEval("'abc' .. nil"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("5 .. {}"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("'abc' .. {}"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("'abc' .. (function() end)"));
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
      var obj = setupEval(t.x);
      assertEquals(t.y, obj.repr());
    }

    assertThrows(LuaRuntimeException.class, () -> setupEval("'a' + 3"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("nil + 3"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("'a' - 3"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("nil - 3"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("'a' * 3"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("nil * 3"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("'a' / 3"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("nil / 3"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("'a' % 3"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("nil % 3"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("-'a'"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("-nil"));
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
      var obj = setupEval(t.x);
      assertEquals(t.y, ((LuaBoolean) obj).getValue(), t.x);
    }
  }

  @Test
  void testIf() throws LuaRuntimeException, IllegalParseException {
    // TODO: test with identifiers

    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("if 1 then 1 else 2 end", "1"));
    tests.add(new Tuple<>("if false then 1 else 2 end", "2"));
    tests.add(new Tuple<>("if 1 == '1' then 1 elseif 1 == 1 then  2 end", "2"));
    tests.add(new Tuple<>("if false then 1 end", "nil"));

    for (Tuple<String, String> t : tests) {
      var obj = setupEval(t.x);
      assertEquals(t.y, obj.repr());
    }
  }

  @Test
  void testWhile() throws LuaRuntimeException, IllegalParseException {
    // TODO: test with identifiers

    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("while true do break end", "nil"));
    tests.add(new Tuple<>("while 1 < 2 do return 1 end", "1"));
    tests.add(new Tuple<>("while 1 == 2 do return 1 end", "nil"));

    for (Tuple<String, String> t : tests) {
      var obj = setupEval(t.x);
      assertEquals(t.y, obj.repr());
    }
  }

  @Test
  void testAssignment() throws LuaRuntimeException, IllegalParseException {
    // TODO: test with identifiers

    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("n = 0 while n < 10 do n = n + 1 end n", "10"));

    for (Tuple<String, String> t : tests) {
      var obj = setupEval(t.x);
      assertEquals(t.y, obj.repr());
    }
  }
}

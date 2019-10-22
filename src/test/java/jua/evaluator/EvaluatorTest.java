package jua.evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jua.Interpreter;
import jua.lexer.Lexer;
import jua.objects.LuaBoolean;
import jua.objects.LuaNumber;
import jua.objects.LuaObject;
import jua.objects.LuaString;
import jua.parser.IllegalParseException;
import jua.parser.Parser;
import org.junit.jupiter.api.Test;
import util.Tuple;

class EvaluatorTest {

  private static final Path testdata =
      Paths.get("src", "test", "java", "jua", "evaluator", "testdata");

  private LuaObject setupEval(String in) throws LuaRuntimeException, IllegalParseException {
    return setupEval(in, new Scope());
  }

  private LuaObject setupEval(String in, Scope scope)
      throws LuaRuntimeException, IllegalParseException {
    Evaluable program = new Parser((new Lexer(in)).getNTokens(0)).parse();
    return program.evaluate(scope);
  }

  @Test
  void testConcatExpr() throws IllegalParseException, LuaRuntimeException {

    Scope scope = new Scope();
    scope.assign("x", new LuaString("hi"));

    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("'abc' .. 'def'", "abcdef"));
    tests.add(new Tuple<>("x .. 'def'", "hidef"));
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
      var obj = setupEval(t.x, scope);
      assertEquals(t.y, obj.repr(), t.x);
    }

    assertThrows(LuaRuntimeException.class, () -> setupEval("'abc' .. nil"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("5 .. {}"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("'abc' .. {}"));
    assertThrows(LuaRuntimeException.class, () -> setupEval("'abc' .. (function() end)"));
  }

  @Test
  void testArithmeticExpr() throws IllegalParseException, LuaRuntimeException {

    Scope scope = new Scope();
    scope.assign("x", new LuaNumber(5.0));

    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("x + 9", "14"));
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
      var obj = setupEval(t.x, scope);
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
  void testHexExpr() throws IllegalParseException, LuaRuntimeException {

    Scope scope = new Scope();
    scope.assign("x", new LuaNumber(5.0));

    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("x + 0x9", "14"));
    tests.add(new Tuple<>("0xff", "255"));

    for (Tuple<String, String> t : tests) {
      var obj = setupEval(t.x, scope);
      assertEquals(t.y, obj.repr());
    }

    assertThrows(IllegalTypeException.class, () -> setupEval("0xfgh"));
    assertThrows(IllegalTypeException.class, () -> setupEval("0x"));
}

  @Test
  void testEqualAndNotEquals() throws IllegalParseException, LuaRuntimeException {
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
    // Undefined variable
    tests.add(new Tuple<>("undef == nil", true));
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
    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("n = 0 while n < 10 do n = n + 1 end n", "10"));

    for (Tuple<String, String> t : tests) {
      var obj = setupEval(t.x);
      assertEquals(t.y, obj.repr());
    }
  }

  @Test
  void testFunction() throws LuaRuntimeException, IllegalParseException {
    // TODO: test with identifiers

    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("function f() return 3 end f()", "3"));
    tests.add(new Tuple<>("f = function() return 3 end f()", "3"));
    tests.add(new Tuple<>("f = function(n) return n end f(3)", "3"));
    tests.add(new Tuple<>("f = function(n,m) return n*m end f(3,5)", "15"));
    tests.add(new Tuple<>("f = function(n,m) return n,m end a,b = f(3,5) a*b", "15"));
    tests.add(
        new Tuple<>(
            "function fact(n) if n == 0 then return 1 else return n * fact(n-1) end end fact(4)",
            "24"));

    for (Tuple<String, String> t : tests) {
      var obj = setupEval(t.x);
      assertEquals(t.y, obj.repr());
    }
  }

  @Test
  void testLocalAssignment() throws LuaRuntimeException, IllegalParseException {

    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("a = 1 if true then local a = 0 end a", "1"));
    // Test local scope in while
    tests.add(new Tuple<>("a = 1 while (a == 1) do local b = 2 a = 0 end b", "nil"));
    // Test local scope in while expression
    tests.add(
        new Tuple<>(
            "a = 1\n"
                + "while (b == nil) do\n"
                + "    if (b == nil) then\n"
                + "        a = 3\n"
                + "    end\n"
                + "    local b = 2\n"
                + "    if (b == 2) then\n"
                + "        break\n"
                + "    end\n"
                + "end\n"
                + "a",
            "3"));

    var s = new Scope();

    for (Tuple<String, String> t : tests) {
      var obj = setupEval(t.x, s);
      assertEquals(t.y, obj.repr());
    }
  }

  @Test
  void testRepeatUntil() throws LuaRuntimeException, IllegalParseException {
    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("a = 0\n" + "repeat\n" + "  a = a + 1\n" + "until a == 2\n" + "a", "2"));

    for (Tuple<String, String> t : tests) {
      var obj = setupEval(t.x);
      assertEquals(t.y, obj.repr());
    }
  }

  @Test
  void testLuaScript() throws IOException, IllegalParseException, LuaRuntimeException {
    Stream<Path> walk = Files.walk(testdata);
    ArrayList<String> filesToIgnore = new ArrayList<>();
    // TODO: ignore 0 files
    filesToIgnore.add("scope.lua");
    // TODO: it's quite important pls fix it

    List<Path> files = walk.filter(f -> f.toString().endsWith(".lua")).collect(Collectors.toList());

    for (var f : files) {
      if (filesToIgnore.contains(f.getFileName().toString())) {
        System.out.println("! Ignored " + f + " for testLuaScript. Fix it !");
      } else {
        var value = runLuaScript(f);
        String expected =
            new String(Files.readAllBytes(Paths.get(f.toString().replace(".lua", ".expected"))));
        assertEquals(expected.strip(), value.strip(), String.format("File: %s", f));
      }
    }
  }

  String runLuaScript(Path file) throws IOException, IllegalParseException, LuaRuntimeException {
    String text = new String(Files.readAllBytes(file));
    return Interpreter.eval(text);
  }
}

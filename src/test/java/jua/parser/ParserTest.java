package jua.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import jua.ast.*;
import jua.lexer.Lexer;
import jua.token.*;
import org.junit.jupiter.api.Test;
import util.Tuple;
import util.Util;

public class ParserTest {

  @Test
  void testCorrectNotExpression() throws IllegalParseException {
    Parser parser = new Parser((new Lexer("x = not true")).getNTokens(0));

    ArrayList<Statement> statements = parser.parse().getChildren();
    ArrayList<Statement> expected = new ArrayList<>();

    expected.add(
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("x")),
            ExpressionFactory.create(
                TokenFactory.create(Operator.NOT),
                ExpressionFactory.create(TokenFactory.create(Literal.BOOLEAN, "true")))));

    assertIterableEquals(expected, statements);
  }

  @Test
  void shouldThrowParseExceptionIfStartingWithBinary() {
    Parser parser = new Parser((new Lexer("x = + 5")).getNTokens(0));

    assertThrows(IllegalParseException.class, parser::parse);
  }

  @Test
  void shouldThrowParseExceptionIfStartingWithRightDelimiter() {
    ArrayList<String> testStrings = new ArrayList<>();
    testStrings.add("x = ] 5 * 2");
    testStrings.add("x = ) 5 * 2");
    testStrings.add("x = } 5 * 2");

    testStrings.forEach(
        (in) -> {
          Parser parser = new Parser((new Lexer(in)).getNTokens(0));
          assertThrows(IllegalParseException.class, parser::parse);
        });
  }

  @Test
  void testAdditionAssignment() throws IllegalParseException {
    Parser parser = new Parser((new Lexer("x = (1 + 5)")).getNTokens(0));

    ArrayList<Statement> statements = parser.parse().getChildren();
    assertEquals(1, statements.size());

    Statement expected =
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("x")),
            ExpressionFactory.create(
                TokenFactory.create(Operator.PLUS),
                ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")),
                ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "5"))));
    assertEquals(expected, statements.get(0));
  }

  @Test
  void testHexAssignment() throws IllegalParseException {
    Parser parser = new Parser((new Lexer("x = (0x40 + 0x64)")).getNTokens(0));

    ArrayList<Statement> statements = parser.parse().getChildren();
    assertEquals(1, statements.size());

    Statement expected =
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("x")),
            ExpressionFactory.create(
                TokenFactory.create(Operator.PLUS),
                ExpressionFactory.create(TokenFactory.create(Literal.HEX_NUMBER, "0x40")),
                ExpressionFactory.create(TokenFactory.create(Literal.HEX_NUMBER, "0x64"))));
    assertEquals(expected, statements.get(0));
  }

  @Test
  void testAdditionAndMultiplicationAssignment() throws IllegalParseException {
    Parser parser = new Parser((new Lexer("x = 1 + 5 * a")).getNTokens(0));
    ArrayList<Statement> statements = parser.parse().getChildren();
    assertEquals(1, statements.size());

    Statement expected =
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("x")),
            ExpressionFactory.create(
                TokenFactory.create(Operator.PLUS),
                ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")),
                ExpressionFactory.create(
                    TokenFactory.create(Operator.ASTERISK),
                    ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "5")),
                    ExpressionFactory.create(TokenFactory.create("a")))));
    assertEquals(expected, statements.get(0));
  }

  @Test
  void testOperatorPrecedenceParsing() throws IllegalParseException {
    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<>("-a * b", "((- a) * b)"));
    tests.add(new Tuple<>("not -a", "(not (- a))"));
    tests.add(new Tuple<>("a + b + c", "((a + b) + c)"));
    tests.add(new Tuple<>("a + b - c", "((a + b) - c)"));
    tests.add(new Tuple<>("a * b * c", "((a * b) * c)"));
    tests.add(new Tuple<>("a / b / c", "((a / b) / c)"));
    tests.add(new Tuple<>("a + b * c + d / e - f", "(((a + (b * c)) + (d / e)) - f)"));
    tests.add(new Tuple<>("5 > 4 == 3 < 4", "(((5 > 4) == 3) < 4)"));
    tests.add(new Tuple<>("5 < 4 ~= 3 > 4", "(((5 < 4) ~= 3) > 4)"));
    tests.add(new Tuple<>("3 + 4 * 5 == 3 * 1 + 4 * 5", "((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))"));
    tests.add(new Tuple<>("3 + 4 * 5 == 3 * 1 + 4 * 5", "((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))"));
    tests.add(new Tuple<>("(5 + 5) * 2", "((5 + 5) * 2)"));
    tests.add(new Tuple<>("1 + (2 + 3) + 4", "((1 + (2 + 3)) + 4)"));
    tests.add(new Tuple<>("-(5 + 5)", "(- (5 + 5))"));
    tests.add(new Tuple<>("not(true == true)", "(not (true == true))"));
    tests.add(new Tuple<>("1 & 1 << 1 | 1 >> 1 ~ 1", "((1 & (1 << 1)) | ((1 >> 1) ~ 1))"));
    tests.add(new Tuple<>("~ xff & 1", "((~ xff) & 1)"));

    for (Tuple<String, String> t : tests) {
      Parser parser = new Parser((new Lexer(t.x)).getNTokens(0));
      ArrayList<Statement> statements = parser.parse().getChildren();
      assertEquals(1, statements.size());
      assertEquals(t.y, statements.get(0).toString(), String.format("Test: %s", t.x));
    }
  }

  @Test
  void testMultilineExpressions() throws IllegalParseException {
    Parser parser = new Parser((new Lexer("x = 3 * 2\nx + 5 / 7")).getNTokens(0));

    ArrayList<Statement> statements = parser.parse().getChildren();
    assertEquals(2, statements.size());
    System.out.println(statements.get(0).toString());
    assertEquals("x = (3 * 2)", statements.get(0).toString());
    assertEquals("(x + (5 / 7))", statements.get(1).toString());
  }

  @Test
  void testFactorialFuncStatement() throws IllegalParseException {
    String in =
        "-- defines a factorial function\n"
            + "function fact (n)\n"
            + "  if n == 0 then\n"
            + "    return 1\n"
            + "  else\n"
            + "    return n * fact(n-1)\n"
            + "  end\n"
            + "end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));

    // TODO
    System.out.println(parser.parse());
  }

  @Test
  void testSimpleFuncStatement() throws IllegalParseException {
    String in = "function identity(x)\n" + "y = x\n" + "return y\n" + "end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    ArrayList<Statement> statements = parser.parse().getChildren();
    assertEquals(1, statements.size());

    ExpressionIdentifier yIdent =
        (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("y"));
    ExpressionIdentifier xIdent =
        (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("x"));

    ArrayList<ExpressionIdentifier> args = new ArrayList<>();
    args.add(xIdent);

    StatementList statementList = new StatementList(TokenFactory.create("y"));
    statementList.addChild(
        new StatementAssignment(TokenFactory.create(Operator.ASSIGN), yIdent, xIdent));
    statementList.addChild(new StatementReturn(TokenFactory.create(Keyword.RETURN), yIdent));

    Statement expected =
        new StatementFunction(
            TokenFactory.create(Keyword.FUNCTION),
            (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("identity")),
            ExpressionFactory.createExpressionFunction(
                TokenFactory.create(Keyword.FUNCTION), args, statementList),
            false);
    assertEquals(expected, statements.get(0));
  }

  @Test
  void testSimpleFuncExpression() throws IllegalParseException {
    String in = "identity = function (x)\n" + "y = x\n" + "return y\n" + "end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    ArrayList<Statement> statements = parser.parse().getChildren();
    assertEquals(1, statements.size());

    ExpressionIdentifier yIdent =
        (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("y"));
    ExpressionIdentifier xIdent =
        (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("x"));

    ArrayList<ExpressionIdentifier> args = new ArrayList<>();
    args.add(xIdent);

    StatementList statementList = new StatementList(TokenFactory.create("y"));
    statementList.addChild(
        new StatementAssignment(TokenFactory.create(Operator.ASSIGN), yIdent, xIdent));
    statementList.addChild(new StatementReturn(TokenFactory.create(Keyword.RETURN), yIdent));

    Statement expected =
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("identity")),
            ExpressionFactory.createExpressionFunction(
                TokenFactory.create(Keyword.FUNCTION), args, statementList));
    assertEquals(expected, statements.get(0));
  }

  @Test
  void testFactorialFuncAssignment() throws IllegalParseException {
    String in =
        "-- defines a factorial function\n"
            + "fact = function (n)\n"
            + "  if n == 0 then\n"
            + "    return 1\n"
            + "  else\n"
            + "    return n * fact(n-1)\n"
            + "  end\n"
            + "end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    // TODO
    System.out.println(parser.parse());
  }

  @Test
  void testInvalidFuncStatementAssignment() throws IllegalParseException {
    String in =
        "-- notice the extra fact before the parenthesis\n"
            + "fact = function fact(n)\n"
            + "  if n == 0 then\n"
            + "    return 1\n"
            + "  else\n"
            + "    return n * fact(n-1)\n"
            + "  end\n"
            + "end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    assertThrows(IllegalParseException.class, parser::parse);
  }

  @Test
  void testIfElseif() throws IllegalParseException {
    String in =
        "  if n == 0 then\n"
            + "    return 1\n"
            + "  elseif n == 1 then\n"
            + "    return 1\n"
            + "  else\n"
            + "    return 2\n"
            + "  end\n";

    Token tok = TokenFactory.create("n");
    ExpressionIdentifier variable = ExpressionFactory.create((TokenIdentifier) tok);

    Expression zero = ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "0"));
    Expression one = ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1"));
    Expression two = ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "2"));

    Token returnTok = TokenFactory.create(Keyword.RETURN);

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    StatementIf expected =
        new StatementIf(
            TokenFactory.create(Keyword.IF),
            ExpressionFactory.create(TokenFactory.create(Operator.EQUALS), variable, zero),
            new StatementList(returnTok, new StatementReturn(returnTok, one)),
            new StatementIf(
                TokenFactory.create(Keyword.ELSEIF),
                ExpressionFactory.create(TokenFactory.create(Operator.EQUALS), variable, one),
                new StatementList(returnTok, new StatementReturn(returnTok, one)),
                new StatementList(returnTok, new StatementReturn(returnTok, two))));

    ArrayList<Statement> result = parser.parse().getChildren();

    assertEquals(expected.getConsequence(), ((StatementIf) result.get(0)).getConsequence());
  }

  @Test
  void testWhile() throws IllegalParseException {
    String in = "  n = 0\n" + "  while n < 10 do\n" + "    n = n + 1\n" + "  end\n";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    Token tok = TokenFactory.create("n");
    ArrayList<Statement> expected = new ArrayList<>();

    ExpressionIdentifier variable = ExpressionFactory.create((TokenIdentifier) tok);

    expected.add(
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            variable,
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "0"))));

    Expression condition =
        ExpressionFactory.create(
            TokenFactory.create(Operator.LT),
            variable,
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "10")));

    StatementList consequenceList = new StatementList(tok);
    consequenceList.addChild(
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            variable,
            ExpressionFactory.create(
                TokenFactory.create(Operator.PLUS),
                variable,
                ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")))));
    StatementBlock consequence =
        new StatementBlock(TokenFactory.create(Keyword.DO), consequenceList);

    Statement whileStatement =
        new StatementWhile(TokenFactory.create(Keyword.WHILE), condition, consequence);

    expected.add(whileStatement);
    ArrayList<Statement> result = parser.parse().getChildren();
    assertIterableEquals(expected, result);
  }

  @Test
  void testNumericFor() throws IllegalParseException {
    String in = "for i = 0,10 do break end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    StatementNumericFor expected =
        new StatementNumericFor(
            TokenFactory.create(Keyword.FOR),
            (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("i")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "0")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "10")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")),
            new StatementBlock(
                TokenFactory.create(Keyword.DO),
                new StatementList(
                    TokenFactory.create(Keyword.BREAK),
                    new StatementBreak(TokenFactory.create(Keyword.BREAK)))));

    Statement result = parser.parse().getChildren().get(0);
    assertEquals(expected, result);
  }

  @Test
  void testNumericForStep() throws IllegalParseException {
    String in = "for i = 0,10,2 do break end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    StatementNumericFor expected =
        new StatementNumericFor(
            TokenFactory.create(Keyword.FOR),
            (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("i")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "0")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "10")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "2")),
            new StatementBlock(
                TokenFactory.create(Keyword.DO),
                new StatementList(
                    TokenFactory.create(Keyword.BREAK),
                    new StatementBreak(TokenFactory.create(Keyword.BREAK)))));

    Statement result = parser.parse().getChildren().get(0);
    assertEquals(expected, result);
  }

  @Test
  void testNumericForException() {
    String in = "for i = 0 do break end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));

    assertThrows(IllegalParseException.class, parser::parse);
  }

  @Test
  void testGenericFor() throws IllegalParseException {
    String in = "for e in l do break end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    StatementGenericFor expected =
        new StatementGenericFor(
            TokenFactory.create(Keyword.FOR),
            util.Util.createArrayList(
                (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("e"))),
            util.Util.createArrayList(ExpressionFactory.create(TokenFactory.create("l"))),
            new StatementBlock(
                TokenFactory.create(Keyword.DO),
                new StatementList(
                    TokenFactory.create(Keyword.BREAK),
                    new StatementBreak(TokenFactory.create(Keyword.BREAK)))));

    Statement result = parser.parse().getChildren().get(0);
    assertEquals(expected, result);
  }

  @Test
  void testGenericForFull() throws IllegalParseException {
    String in = "for e in l,s,var do break end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    StatementGenericFor expected =
        new StatementGenericFor(
            TokenFactory.create(Keyword.FOR),
            util.Util.createArrayList(ExpressionFactory.create(TokenFactory.create("e"))),
            util.Util.createArrayList(
                ExpressionFactory.create(TokenFactory.create("l")),
                ExpressionFactory.create(TokenFactory.create("s")),
                ExpressionFactory.create(TokenFactory.create("var"))),
            new StatementBlock(
                TokenFactory.create(Keyword.DO),
                new StatementList(
                    TokenFactory.create(Keyword.BREAK),
                    new StatementBreak(TokenFactory.create(Keyword.BREAK)))));

    Statement result = parser.parse().getChildren().get(0);
    assertEquals(expected, result);
  }

  @Test
  void testSameSizeMultiAssignment() throws IllegalParseException {
    String in1 = "a, b, c = 1, 2, 3";
    String in2 = "a, b, c = 1, 2,\n3";

    ArrayList<Statement> stmts1 = new Parser((new Lexer(in1)).getNTokens(0)).parse().getChildren();
    ArrayList<Statement> stmts2 = new Parser((new Lexer(in2)).getNTokens(0)).parse().getChildren();

    ArrayList<Variable> identifiers = new ArrayList<>();
    identifiers.add(ExpressionFactory.create((TokenIdentifier) TokenFactory.create("a")));
    identifiers.add(ExpressionFactory.create((TokenIdentifier) TokenFactory.create("b")));
    identifiers.add(ExpressionFactory.create((TokenIdentifier) TokenFactory.create("c")));

    ArrayList<Expression> values =
        util.Util.createArrayList(
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "2")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "3")));

    StatementAssignment expected =
        new StatementAssignment(TokenFactory.create(Operator.ASSIGN), identifiers, values, false);

    assertEquals(1, stmts1.size());
    assertEquals(1, stmts2.size());
    assertEquals(expected, stmts1.get(0));
    assertEquals(expected, stmts2.get(0));
  }

  @Test
  void testMultiAssignmentWithLessValues() throws IllegalParseException {
    String in = "a, b, c = 1, 2";

    ArrayList<Statement> stmts = new Parser((new Lexer(in)).getNTokens(0)).parse().getChildren();

    ArrayList<Variable> identifiers = new ArrayList<>();
    identifiers.add(ExpressionFactory.create((TokenIdentifier) TokenFactory.create("a")));
    identifiers.add(ExpressionFactory.create((TokenIdentifier) TokenFactory.create("b")));
    identifiers.add(ExpressionFactory.create((TokenIdentifier) TokenFactory.create("c")));

    ArrayList<Expression> values =
        util.Util.createArrayList(
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "2")));

    StatementAssignment expected =
        new StatementAssignment(TokenFactory.create(Operator.ASSIGN), identifiers, values, false);

    assertEquals(1, stmts.size());
    assertEquals(expected, stmts.get(0));
  }

  @Test
  void testMultiAssignmentWithLessIdentifiers() throws IllegalParseException {
    String in = "a, b = 1, 2, 3";

    ArrayList<Statement> stmts = new Parser((new Lexer(in)).getNTokens(0)).parse().getChildren();

    ArrayList<Variable> identifiers =
        util.Util.createArrayList(
            ExpressionFactory.create((TokenIdentifier) TokenFactory.create("a")),
            ExpressionFactory.create((TokenIdentifier) TokenFactory.create("b")));

    ArrayList<Expression> values =
        util.Util.createArrayList(
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "2")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "3")));

    StatementAssignment expected =
        new StatementAssignment(TokenFactory.create(Operator.ASSIGN), identifiers, values, false);

    assertEquals(1, stmts.size());
    assertEquals(expected, stmts.get(0));
  }

  @Test
  void testBracketExpr() throws IllegalParseException {
    String in = "a[x * 2]";

    ArrayList<Statement> stmts = new Parser((new Lexer(in)).getNTokens(0)).parse().getChildren();

    StatementExpression expected =
        new StatementExpression(
            new ExpressionIndex(
                TokenFactory.create(Operator.INDEX),
                ExpressionFactory.create(TokenFactory.create("a")),
                ExpressionFactory.create(
                    TokenFactory.create(Operator.ASTERISK),
                    ExpressionFactory.create(TokenFactory.create("x")),
                    ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "2")))));
    assertEquals(1, stmts.size());
    assertEquals(expected, stmts.get(0));
  }

  @Test
  void testTableConstructorWithString() throws IllegalParseException {
    String in = "{ [f(1)] = g; \"x\", \"y\"}";

    ArrayList<Statement> stmts = new Parser((new Lexer(in)).getNTokens(0)).parse().getChildren();

    ExpressionIdentifier func =
        ((ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("f")));

    ArrayList<Tuple<Expression, Expression>> tuples = new ArrayList<>();
    tuples.add(
        new Tuple<>(
            ExpressionFactory.create(
                func,
                0,
                0,
                util.Util.createArrayList(
                    ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")))),
            ExpressionFactory.create(TokenFactory.create("g"))));
    tuples.add(
        new Tuple<>(
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")),
            ExpressionFactory.create(TokenFactory.create(Literal.STRING, "x"))));
    tuples.add(
        new Tuple<>(
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "2")),
            ExpressionFactory.create(TokenFactory.create(Literal.STRING, "y"))));

    assertEquals(1, stmts.size());

    var result =
        ((ExpressionTableConstructor) ((StatementExpression) stmts.get(0)).getExpr()).getTuples();
    assertIterableEquals(tuples, result);
  }

  @Test
  void testTableConstructor() throws IllegalParseException {
    String in = "{ [f(1)] = g; x, y; x = 1, f(x), [30] = 23; 45 }";

    ArrayList<Statement> stmts = new Parser((new Lexer(in)).getNTokens(0)).parse().getChildren();

    ExpressionIdentifier func =
        ((ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("f")));

    ArrayList<Tuple<Expression, Expression>> tuples = new ArrayList<>();
    tuples.add(
        new Tuple<>(
            ExpressionFactory.create(
                func,
                0,
                0,
                util.Util.createArrayList(
                    ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")))),
            ExpressionFactory.create(TokenFactory.create("g"))));
    tuples.add(
        new Tuple<>(
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")),
            ExpressionFactory.create(TokenFactory.create("x"))));
    tuples.add(
        new Tuple<>(
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "2")),
            ExpressionFactory.create(TokenFactory.create("y"))));
    tuples.add(
        new Tuple<>(
            ExpressionFactory.create(TokenFactory.create(Literal.STRING, "x")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1"))));
    tuples.add(
        new Tuple<>(
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "3")),
            ExpressionFactory.create(
                func,
                0,
                0,
                util.Util.createArrayList(ExpressionFactory.create(TokenFactory.create("x"))))));
    tuples.add(
        new Tuple<>(
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "30")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "23"))));
    tuples.add(
        new Tuple<>(
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "4")),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "45"))));

    assertEquals(1, stmts.size());

    StatementExpression statement = (StatementExpression) stmts.get(0);
    Expression expression = statement.getExpr();
    ExpressionTableConstructor expressionTableConstructor = (ExpressionTableConstructor) expression;
    assertIterableEquals(tuples, expressionTableConstructor.getTuples());
  }

  @Test
  void testRepeatUntil() throws IllegalParseException {
    String in =
        "a = 0\n" + "repeat\n" + "  a = a + 1\n" + "  print(a)\n" + "until a == 2\n" + "print(a)";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));

    ArrayList<Statement> actual = parser.parse().getChildren();
    ArrayList<Statement> expected = new ArrayList<>();

    Token var_a = TokenFactory.create("a");
    StatementExpression printStatement =
        new StatementExpression(
            ExpressionFactory.create(
                ExpressionFactory.create((TokenIdentifier) TokenFactory.create("print")),
                0,
                0,
                Util.createArrayList(ExpressionFactory.create(var_a))));

    expected.add(
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            (ExpressionIdentifier) ExpressionFactory.create(var_a),
            ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "0"))));

    StatementList repeatBlock = new StatementList(var_a);
    repeatBlock.addChild(
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            (ExpressionIdentifier) ExpressionFactory.create(var_a),
            ExpressionFactory.create(
                TokenFactory.create(Operator.PLUS),
                ExpressionFactory.create(var_a),
                ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "1")))));
    repeatBlock.addChild(printStatement);
    expected.add(
        new StatementRepeatUntil(
            TokenFactory.create("repeat"),
            ExpressionFactory.create(
                TokenFactory.create(Operator.EQUALS),
                ExpressionFactory.create(var_a),
                ExpressionFactory.create(TokenFactory.create(Literal.NUMBER, "2"))),
            repeatBlock));

    expected.add(printStatement);

    assertIterableEquals(expected, actual);
  }
}

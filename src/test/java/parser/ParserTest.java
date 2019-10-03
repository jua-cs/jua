package parser;

import static org.junit.jupiter.api.Assertions.*;

import ast.*;
import java.util.ArrayList;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import token.*;
import util.Tuple;

public class ParserTest {

  @Test
  void testCorrectNotExpression() throws IllegalParseException {
    Parser parser = new Parser((new Lexer("x = not true")).getNTokens(0));

    ArrayList<Statement> statements = parser.parse();
    ArrayList<Statement> expected = new ArrayList<>();

    expected.add(
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("x")),
            ExpressionFactory.create(
                (TokenOperator) TokenFactory.create(Operator.NOT),
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

    ArrayList<Statement> statements = parser.parse();
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
  void testAdditionAndMultiplicationAssignment() throws IllegalParseException {
    Parser parser = new Parser((new Lexer("x = 1 + 5 * a")).getNTokens(0));
    ArrayList<Statement> statements = parser.parse();
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

    for (Tuple<String, String> t : tests) {
      Parser parser = new Parser((new Lexer(t.x)).getNTokens(0));
      ArrayList<Statement> statements = parser.parse();
      assertEquals(1, statements.size());
      assertEquals(t.y, statements.get(0).toString(), String.format("Test: %s", t.x));
    }
  }

  @Test
  void testMultilineExpressions() throws IllegalParseException {
    Parser parser = new Parser((new Lexer("x = 3 * 2\nx + 5 / 7")).getNTokens(0));

    ArrayList<Statement> statements = parser.parse();
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
    ArrayList<Statement> statements = parser.parse();
    assertEquals(1, statements.size());

    ExpressionIdentifier yIdent =
        (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("y"));
    ExpressionIdentifier xIdent =
        (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("x"));

    ArrayList<Expression> args = new ArrayList<>();
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
                TokenFactory.create(Keyword.FUNCTION), args, statementList));
    assertEquals(expected, statements.get(0));
  }

  @Test
  void testSimpleFuncExpression() throws IllegalParseException {
    String in = "identity = function (x)\n" + "y = x\n" + "return y\n" + "end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    ArrayList<Statement> statements = parser.parse();
    assertEquals(1, statements.size());

    ExpressionIdentifier yIdent =
        (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("y"));
    ExpressionIdentifier xIdent =
        (ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("x"));

    ArrayList<Expression> args = new ArrayList<>();
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
    ExpressionIdentifier variable = new ExpressionIdentifier(tok);

    Expression zero = new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "0"));
    Expression one = new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "1"));
    Expression two = new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "2"));

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

    ArrayList<Statement> result = parser.parse();

    assertEquals(expected.getConsequence(), ((StatementIf) result.get(0)).getConsequence());
  }

  @Test
  void testWhile() throws IllegalParseException {
    String in = "  n = 0\n" + "  while n < 10 do\n" + "    n = n + 1\n" + "  end\n";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    Token tok = TokenFactory.create("n");
    ArrayList<Statement> expected = new ArrayList<>();

    ExpressionIdentifier variable = new ExpressionIdentifier(tok);

    expected.add(
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            variable,
            new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "0"))));

    Expression condition =
        ExpressionFactory.create(
            TokenFactory.create(Operator.LT),
            variable,
            new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "10")));

    StatementList consequence = new StatementList(tok);
    consequence.addChild(
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            variable,
            ExpressionFactory.create(
                TokenFactory.create(Operator.PLUS),
                variable,
                new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "1")))));

    Statement whileStatement =
        new StatementWhile(TokenFactory.create(Keyword.WHILE), condition, consequence);

    expected.add(whileStatement);
    ArrayList<Statement> result = parser.parse();
    assertIterableEquals(expected, result);
  }

  @Test
  void testSameSizeMultiAssignment() throws IllegalParseException {
    String in1 = "a, b, c = 1, 2, 3";
    String in2 = "a, b, c = 1, 2,\n3";

    ArrayList<Statement> stmts1 = new Parser((new Lexer(in1)).getNTokens(0)).parse();
    ArrayList<Statement> stmts2 = new Parser((new Lexer(in2)).getNTokens(0)).parse();

    ArrayList<ExpressionIdentifier> identifiers = new ArrayList<>();
    identifiers.add(new ExpressionIdentifier(TokenFactory.create("a")));
    identifiers.add(new ExpressionIdentifier(TokenFactory.create("b")));
    identifiers.add(new ExpressionIdentifier(TokenFactory.create("c")));

    ArrayList<Expression> values =
        util.Util.createArrayList(
            new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "1")),
            new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "2")),
            new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "3")));

    StatementAssignment expected =
        new StatementAssignment(TokenFactory.create(Operator.ASSIGN), identifiers, values);

    assertEquals(1, stmts1.size());
    assertEquals(1, stmts2.size());
    assertEquals(expected, stmts1.get(0));
    assertEquals(expected, stmts2.get(0));
  }

  @Test
  void testMultiAssignmentWithLessValues() throws IllegalParseException {
    String in = "a, b, c = 1, 2";

    ArrayList<Statement> stmts = new Parser((new Lexer(in)).getNTokens(0)).parse();

    ArrayList<ExpressionIdentifier> identifiers = new ArrayList<>();
    identifiers.add(new ExpressionIdentifier(TokenFactory.create("a")));
    identifiers.add(new ExpressionIdentifier(TokenFactory.create("b")));
    identifiers.add(new ExpressionIdentifier(TokenFactory.create("c")));

    ArrayList<Expression> values =
        util.Util.createArrayList(
            new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "1")),
            new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "2")));

    StatementAssignment expected =
        new StatementAssignment(TokenFactory.create(Operator.ASSIGN), identifiers, values);

    assertEquals(1, stmts.size());
    assertEquals(expected, stmts.get(0));
  }

  @Test
  void testMultiAssignmentWithLessIdentifiers() throws IllegalParseException {
    String in = "a, b = 1, 2, 3";

    ArrayList<Statement> stmts = new Parser((new Lexer(in)).getNTokens(0)).parse();

    var identifiers =
        util.Util.createArrayList(
            new ExpressionIdentifier(TokenFactory.create("a")),
            new ExpressionIdentifier(TokenFactory.create("b")));

    ArrayList<Expression> values =
        util.Util.createArrayList(
            new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "1")),
            new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "2")),
            new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "3")));

    StatementAssignment expected =
        new StatementAssignment(TokenFactory.create(Operator.ASSIGN), identifiers, values);

    assertEquals(1, stmts.size());
    assertEquals(expected, stmts.get(0));
  }

  @Test
  void testBracketExpr() throws IllegalParseException {
    String in = "a[x * 2]";

    ArrayList<Statement> stmts = new Parser((new Lexer(in)).getNTokens(0)).parse();

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
    // TODO enable this test when strings are parsed
    String in = "a = { [f(1)] = g; \"x\", \"y\"; x = 1, f(x), [30] = 23; 45 }";

    // ArrayList<Statement> stmts = new Parser((new Lexer(in)).getNTokens(0)).parse();
    // System.out.println(stmts);
  }

  @Test
  void testTableConstructor() throws IllegalParseException {
    String in = "{ [f(1)] = g; x, y; x = 1, f(x), [30] = 23; 45 }";

    ArrayList<Statement> stmts = new Parser((new Lexer(in)).getNTokens(0)).parse();

    ExpressionIdentifier func =
        ((ExpressionIdentifier) ExpressionFactory.create(TokenFactory.create("f")));

    ArrayList<Tuple<Expression, Expression>> tuples = new ArrayList<>();
    tuples.add(
        new Tuple<>(
            ExpressionFactory.create(
                func,
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

    var result =
        ((ExpressionTableConstructor) ((StatementExpression) stmts.get(0)).getExpr()).getTuples();
    assertIterableEquals(tuples, result);
  }
}

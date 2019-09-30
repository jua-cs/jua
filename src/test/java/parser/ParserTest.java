package parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    assertEquals(1, statements.size());

    StatementAssignment expected =
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            new ExpressionIdentifier(TokenFactory.create("x")),
            ExpressionFactory.create(
                (TokenOperator) TokenFactory.create(Operator.NOT),
                new ExpressionLiteral(TokenFactory.create(Literal.BOOLEAN, "true"))));

    assertEquals(expected, statements.get(0));
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
            new ExpressionIdentifier(TokenFactory.create("x")),
            ExpressionFactory.create(
                TokenFactory.create(Operator.PLUS),
                new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "1")),
                new ExpressionLiteral(TokenFactory.create(Literal.NUMBER, "5"))));
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
            new ExpressionIdentifier(TokenFactory.create("x")),
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

    ExpressionIdentifier yIdent = new ExpressionIdentifier(TokenFactory.create("y"));
    ExpressionIdentifier xIdent = new ExpressionIdentifier(TokenFactory.create("x"));

    ArrayList<ExpressionIdentifier> args = new ArrayList<>();
    args.add(xIdent);

    StatementList statementList = new StatementList(TokenFactory.create("y"));
    statementList.addChild(
        new StatementAssignment(TokenFactory.create(Operator.ASSIGN), yIdent, xIdent));
    statementList.addChild(new StatementReturn(TokenFactory.create(Keyword.RETURN), yIdent));

    Statement expected =
        new StatementFunction(
            TokenFactory.create(Keyword.FUNCTION),
            new ExpressionIdentifier(TokenFactory.create("identity")),
            new ExpressionFunction(TokenFactory.create(Keyword.FUNCTION), args, statementList));
    assertEquals(expected, statements.get(0));
  }

  @Test
  void testSimpleFuncExpression() throws IllegalParseException {
    String in = "identity = function (x)\n" + "y = x\n" + "return y\n" + "end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    ArrayList<Statement> statements = parser.parse();
    assertEquals(1, statements.size());

    ExpressionIdentifier yIdent = new ExpressionIdentifier(TokenFactory.create("y"));
    ExpressionIdentifier xIdent = new ExpressionIdentifier(TokenFactory.create("x"));

    ArrayList<ExpressionIdentifier> args = new ArrayList<>();
    args.add(xIdent);

    StatementList statementList = new StatementList(TokenFactory.create("y"));
    statementList.addChild(
        new StatementAssignment(TokenFactory.create(Operator.ASSIGN), yIdent, xIdent));
    statementList.addChild(new StatementReturn(TokenFactory.create(Keyword.RETURN), yIdent));

    Statement expected =
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            new ExpressionIdentifier(TokenFactory.create("identity")),
            new ExpressionFunction(TokenFactory.create(Keyword.FUNCTION), args, statementList));
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
  void testFactorialFuncAssignmentElseif() throws IllegalParseException {
    String in =
        "-- defines a factorial function\n"
            + "fact = function (n)\n"
            + "  if n == 0 then\n"
            + "    return 1\n"
            + "  elseif n == 1 then\n"
            + "    return 1\n"
            + "  else\n"
            + "    return n * fact(n-1)\n"
            + "  end\n"
            + "end";

    Parser parser = new Parser((new Lexer(in)).getNTokens(0));
    // TODO
    System.out.println(parser.parse());
  }
}

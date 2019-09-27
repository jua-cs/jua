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
    Parser parser = new Parser((new Lexer(new String("x = not true"))).getNTokens(0));

    parser.parse();
    AST ast = parser.getAst();

    ArrayList<Statement> statements = ast.getChildren();
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
    Parser parser = new Parser((new Lexer(new String("x = + 5"))).getNTokens(0));

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
    Parser parser = new Parser((new Lexer(new String("x = (1 + 5)"))).getNTokens(0));

    parser.parse();
    ArrayList<Statement> statements = parser.getAst().getChildren();
    assertEquals(1, statements.size());

    System.out.println(statements.get(0));
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
    Parser parser = new Parser((new Lexer(new String("x = 1 + 5 * a"))).getNTokens(0));
    parser.parse();
    ArrayList<Statement> statements = parser.getAst().getChildren();
    assertEquals(1, statements.size());

    Statement expected =
        new StatementAssignment(
            TokenFactory.create(Operator.ASSIGN),
            new ExpressionIdentifier(TokenFactory.create("x")),
            ExpressionFactory.create(
                TokenFactory.create(Operator.PLUS),
                ExpressionFactory.create(TokenFactory.create(1)),
                ExpressionFactory.create(
                    TokenFactory.create(Operator.ASTERISK),
                    ExpressionFactory.create(TokenFactory.create(5)),
                    ExpressionFactory.create(TokenFactory.create("a")))));
    assertEquals(expected, statements.get(0));
  }

  @Test
  void testOperatorPrecedenceParsing() throws IllegalParseException {
    ArrayList<Tuple<String, String>> tests = new ArrayList<>();
    tests.add(new Tuple<String, String>("-a * b", "((- a) * b)"));
    tests.add(new Tuple<String, String>("not -a", "(not (- a))"));
    tests.add(new Tuple<String, String>("a + b + c", "((a + b) + c)"));
    tests.add(new Tuple<String, String>("a + b - c", "((a + b) - c)"));
    tests.add(new Tuple<String, String>("a * b * c", "((a * b) * c)"));
    tests.add(new Tuple<String, String>("a / b / c", "((a / b) / c)"));
    tests.add(
        new Tuple<String, String>("a + b * c + d / e - f", "(((a + (b * c)) + (d / e)) - f)"));
    tests.add(new Tuple<String, String>("5 > 4 == 3 < 4", "(((5.0 > 4.0) == (3.0 < 4.0)))"));
    tests.add(new Tuple<String, String>("5 < 4 != 3 > 4", "((5.0 < 4.0) != (3.0 > 4.0))"));
    tests.add(
        new Tuple<String, String>(
            "3 + 4 * 5 == 3 * 1 + 4 * 5", "((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))"));
    tests.add(
        new Tuple<String, String>(
            "3 + 4 * 5 == 3 * 1 + 4 * 5", "((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))"));

    for (Tuple<String, String> t : tests) {
      System.out.println(t.x);
      Parser parser = new Parser((new Lexer(t.x)).getNTokens(0));
      parser.parse();
      ArrayList<Statement> statements = parser.getAst().getChildren();
      assertEquals(1, statements.size());
      assertEquals(t.y, statements.get(0).toString(), String.format("Test: %s", t.x));
    }
  }
}

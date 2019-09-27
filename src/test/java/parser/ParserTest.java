package parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.*;
import java.util.ArrayList;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import token.Keyword;
import token.Operator;
import token.TokenFactory;
import token.TokenOperator;

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
                new ExpressionLiteral(TokenFactory.create(Keyword.TRUE))));

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
                new ExpressionLiteral(TokenFactory.create(1)),
                new ExpressionLiteral(TokenFactory.create(5))));
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
                new ExpressionLiteral(TokenFactory.create(1)),
                ExpressionFactory.create(
                    TokenFactory.create(Operator.ASTERISK),
                    new ExpressionLiteral(TokenFactory.create(5)),
                    new ExpressionIdentifier(TokenFactory.create("a")))));
    assertEquals(expected, statements.get(0));
  }
}

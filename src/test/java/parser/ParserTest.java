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
            TokenFactory.create(Operator.ASSIGN, 1, 3),
            new ExpressionIdentifier(TokenFactory.create("x", 1, 1)),
            ExpressionUnaryFactory.create(
                (TokenOperator) TokenFactory.create(Operator.NOT, 1, 7),
                new ExpressionLiteral(TokenFactory.create(Keyword.TRUE, 1, 9))));

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
  void testValidDelimitedArithmeticExpr() throws IllegalParseException {
    Parser parser = new Parser((new Lexer(new String("x = (1 + 5)"))).getNTokens(0));

    parser.parse();
    // TODO(SAMI): verify statements
  }
}

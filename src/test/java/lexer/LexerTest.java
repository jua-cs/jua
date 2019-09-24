package lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import token.*;

public class LexerTest {

  @Test
  void testVariableAssignment() {
    Lexer lex = new Lexer(new String("x = y\nbonjour = bonsoir"));

    ArrayList<Token> list = lex.getNTokens(7);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "x", 1, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 1, 3));
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "y", 1, 5));
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "bonjour", 2, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 2, 9));
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "bonsoir", 2, 11));
    expected.add(TokenFactory.createEOF(2, 18));

    assertEquals(expected, list);
  }

  @Test
  void testLuaFactorialWithCommentaries() {
    String in =
        " -- defines a factorial function\n"
            + "function fact (n)\n"
            + "  if n == 0 then\n"
            + "    return 1\n"
            + "  else\n"
            + "    return n * fact(n-1)\n"
            + "  end\n"
            + "end\n"
            + "\n"
            + "print(n)\n";

    Lexer lex = new Lexer(in);
    ArrayList<Token> list = lex.getNTokens(29);

    ArrayList<Token> expected = new ArrayList<Token>();
    // First line of code
    expected.add(TokenFactory.create(Keyword.FUNCTION, 1, 1));
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "fact", 1, 10));
    expected.add(TokenFactory.create(Delimiter.LPAREN, 1, 15));
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "n", 1, 16));
    expected.add(TokenFactory.create(Delimiter.RPAREN, 1, 17));

    expected.add(TokenFactory.create(Keyword.IF, 2, 3));
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "n", 2, 6));
    expected.add(TokenFactory.create(Operator.EQUALS, 2, 8));
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "0", 2, 11));
    expected.add(TokenFactory.create(Keyword.THEN, 2, 13));

    expected.add(TokenFactory.create(Keyword.RETURN, 3, 5));
    expected.add(TokenFactory.create(Literal.NUMBER, "1", 3, 12));

    expected.add(TokenFactory.create(Keyword.ELSE, 4, 3));

    expected.add(TokenFactory.create(Keyword.RETURN, 5, 5));
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "n", 5, 12));
    expected.add(TokenFactory.create(Operator.ASTERISK, 5, 14));
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "fact", 5, 16));
    expected.add(TokenFactory.create(Delimiter.LPAREN, 5, 20));
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "n", 5, 21));
    expected.add(TokenFactory.create(Operator.MINUS, 5, 22));
    expected.add(TokenFactory.create(Literal.NUMBER, "1", 5, 23));
    expected.add(TokenFactory.create(Delimiter.RPAREN, 5, 24));

    expected.add(TokenFactory.create(Keyword.END, 6, 3));

    expected.add(TokenFactory.create(Keyword.END, 7, 1));

    expected.add(TokenFactory.create(Literal.IDENTIFIER, "print", 9, 1));
    expected.add(TokenFactory.create(Delimiter.LPAREN, 9, 6));
    expected.add(TokenFactory.create(Literal.IDENTIFIER, "n", 9, 7));
    expected.add(TokenFactory.create(Delimiter.RPAREN, 9, 8));
    expected.add(TokenFactory.createEOF(10, 1));

    assertEquals(expected, list);
  }
}

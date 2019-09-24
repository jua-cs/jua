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
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "x", 1, 1));
    expected.add(new TokenOperator(Operator.ASSIGN, 1, 3));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "y", 1, 5));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "bonjour", 2, 1));
    expected.add(new TokenOperator(Operator.ASSIGN, 2, 9));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "bonsoir", 2, 11));
    expected.add(new EOFToken(2, 18));

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
    expected.add(new TokenKeyword(Keyword.FUNCTION, 1, 1));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "fact", 1, 10));
    expected.add(new TokenDelimiter(Delimiter.LPAREN, 1, 15));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "n", 1, 16));
    expected.add(new TokenDelimiter(Delimiter.RPAREN, 1, 17));

    expected.add(new TokenKeyword(Keyword.IF, 2, 3));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "n", 2, 6));
    expected.add(new TokenOperator(Operator.EQUALS, 2, 8));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "0", 2, 11));
    expected.add(new TokenKeyword(Keyword.THEN, 2, 13));

    expected.add(new TokenKeyword(Keyword.RETURN, 3, 5));
    expected.add(new TokenLiteral(Literal.NUMBER, "1", 3, 12));

    expected.add(new TokenKeyword(Keyword.ELSE, 4, 3));

    expected.add(new TokenKeyword(Keyword.RETURN, 5, 5));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "n", 5, 12));
    expected.add(new TokenOperator(Operator.ASTERISK, 5, 14));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "fact", 5, 16));
    expected.add(new TokenDelimiter(Delimiter.LPAREN, 5, 20));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "n", 5, 21));
    expected.add(new TokenOperator(Operator.MINUS, 5, 22));
    expected.add(new TokenLiteral(Literal.NUMBER, "1", 5, 23));
    expected.add(new TokenDelimiter(Delimiter.RPAREN, 5, 24));

    expected.add(new TokenKeyword(Keyword.END, 6, 3));

    expected.add(new TokenKeyword(Keyword.END, 7, 1));

    expected.add(new TokenLiteral(Literal.IDENTIFIER, "print", 9, 1));
    expected.add(new TokenDelimiter(Delimiter.LPAREN, 9, 6));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "n", 9, 7));
    expected.add(new TokenDelimiter(Delimiter.RPAREN, 9, 8));
    expected.add(new EOFToken(10, 1));

    assertEquals(expected, list);
  }
}

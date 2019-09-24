package lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import token.*;

public class LexerTest {

  @Test
  void testVariableAssignment() {
    String in = "x = y\nbonjour = bonsoir";
    Lexer lex = new Lexer(in);

    ArrayList<Token> list = lex.getNTokens(6);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "x", 0, 1));
    expected.add(new TokenOperator(Operator.ASSIGN, 0, 3));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "y", 0, 5));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "bonjour", 1, 1));
    expected.add(new TokenOperator(Operator.ASSIGN, 1, 9));
    expected.add(new TokenLiteral(Literal.IDENTIFIER, "bonsoir", 1, 11));

    assertEquals(expected, list);
  }

  @Test
  void testLuaFactorialWithCommentaries() {
    String in =
        " -- defines a factorial function\n"
            + "    function fact (n)\n"
            + "      if n == 0 then\n"
            + "        return 1\n"
            + "      else\n"
            + "        return n * fact(n-1)\n"
            + "      end\n"
            + "    end\n"
            + "    \n"
            + "    print(\"enter a number:\")\n"
            + "    a = io.read(\"*number\")        -- read a number\n"
            + "    print(fact(a))";

    Lexer lex = new Lexer(in);
    ArrayList<Token> list = lex.getNTokens(3);

    list.forEach(System.out::println);
  }
}

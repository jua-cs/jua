package lexer;

import lexer.Lexer;
import org.junit.jupiter.api.Test;
import token.Literal;
import token.Token;
import token.TokenLiteral;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

      @Test
      void testVariableAssignment() {
        String in = "x = y";
        Lexer lex = new Lexer(in);
        Stream<Token> stream =
            Stream.generate(
                    () -> {
                      try {
                        return lex.nextToken();
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                      return null;
                    })
                .limit(3);

        ArrayList<Token> list = stream.collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Token> expected = new ArrayList<Token>();
        expected.add(new TokenLiteral(Literal.IDENTIFIER, "x", 0, 0));
        expected.add(new TokenLiteral(Literal.IDENTIFIER, "y", 0, 4));

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
        Stream<Token> stream =
            Stream.generate(
                    () -> {
                      try {
                        return lex.nextToken();
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                      return null;
                    })
                .limit(50);

        stream.forEach(System.out::println);
      }
    }

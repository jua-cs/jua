package jua.lexer;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import jua.evaluator.IllegalLexingException;
import jua.token.*;
import org.junit.jupiter.api.Test;

public class LexerTest {

  @Test
  void testVariableAssignment() {
    Lexer lex = new Lexer("x = y\nbonjour = bonsoir");

    ArrayList<Token> list = lex.getNTokens(0);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create("x", 1, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 1, 3));
    expected.add(TokenFactory.create("y", 1, 5));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 1, 6));
    expected.add(TokenFactory.create("bonjour", 2, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 2, 9));
    expected.add(TokenFactory.create("bonsoir", 2, 11));
    expected.add(TokenFactory.create(Special.TokenEOF, 2, 18));

    assertIterableEquals(expected, list);
  }

  @Test
  void testConcatOperator() {
    Lexer lex = new Lexer("'salut' .. 'cc'");

    ArrayList<Token> list = lex.getNTokens(3);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create(Literal.STRING, "salut", 1, 1));
    expected.add(TokenFactory.create(Operator.CONCAT, 1, 9));
    expected.add(TokenFactory.create(Literal.STRING, "cc", 1, 12));

    assertIterableEquals(expected, list);
  }

  @Test
  void testVariableWithDigitOrUnderscore() {
    Lexer lex =
        new Lexer(
            "varw1thd1g1ts0 = y\n" + "bon_jour = _bonsoir"
            //            "1ncorrect = 5hould_fail"
            );

    ArrayList<Token> list = lex.getNTokens(0);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create("varw1thd1g1ts0", 1, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 1, 16));
    expected.add(TokenFactory.create("y", 1, 18));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 1, 19));
    expected.add(TokenFactory.create("bon_jour", 2, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 2, 10));
    expected.add(TokenFactory.create("_bonsoir", 2, 12));
    expected.add(TokenFactory.create(Special.TokenEOF, 2, 20));
    // TODO: implements a better jua.lexer for number (ie fails if a char inside)
    //    expected.add(TokenFactory.create(Special.TokenInvalid, 3, 1));
    //    expected.add(TokenFactory.create(Operator.ASSIGN, 2, 11));
    //    expected.add(TokenFactory.create(Special.TokenInvalid, 3, 13));

    assertIterableEquals(expected, list);
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
    ArrayList<Token> list = lex.getNTokens(0);

    ArrayList<Token> expected = new ArrayList<Token>();
    // First line of code
    expected.add(TokenFactory.create(Keyword.FUNCTION, 2, 1));
    expected.add(TokenFactory.create("fact", 2, 10));
    expected.add(TokenFactory.create(Delimiter.LPAREN, 2, 15));
    expected.add(TokenFactory.create("n", 2, 16));
    expected.add(TokenFactory.create(Delimiter.RPAREN, 2, 17));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 2, 18));

    expected.add(TokenFactory.create(Keyword.IF, 3, 3));
    expected.add(TokenFactory.create("n", 3, 6));
    expected.add(TokenFactory.create(Operator.EQUALS, 3, 8));
    expected.add(TokenFactory.create(Literal.NUMBER, "0", 3, 11));
    expected.add(TokenFactory.create(Keyword.THEN, 3, 13));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 3, 17));

    expected.add(TokenFactory.create(Keyword.RETURN, 4, 5));
    expected.add(TokenFactory.create(Literal.NUMBER, "1", 4, 12));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 4, 13));

    expected.add(TokenFactory.create(Keyword.ELSE, 5, 3));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 5, 7));

    expected.add(TokenFactory.create(Keyword.RETURN, 6, 5));
    expected.add(TokenFactory.create("" + "n", 6, 12));
    expected.add(TokenFactory.create(Operator.ASTERISK, 6, 14));
    expected.add(TokenFactory.create("fact", 6, 16));
    expected.add(TokenFactory.create(Delimiter.LPAREN, 6, 20));
    expected.add(TokenFactory.create("n", 6, 21));
    expected.add(TokenFactory.create(Operator.MINUS, 6, 22));
    expected.add(TokenFactory.create(Literal.NUMBER, "1", 6, 23));
    expected.add(TokenFactory.create(Delimiter.RPAREN, 6, 24));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 6, 25));

    expected.add(TokenFactory.create(Keyword.END, 7, 3));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 7, 6));

    expected.add(TokenFactory.create(Keyword.END, 8, 1));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 8, 4));

    expected.add(TokenFactory.create(Delimiter.NEWLINE, 9, 1));

    expected.add(TokenFactory.create("print", 10, 1));
    expected.add(TokenFactory.create(Delimiter.LPAREN, 10, 6));
    expected.add(TokenFactory.create("n", 10, 7));
    expected.add(TokenFactory.create(Delimiter.RPAREN, 10, 8));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 10, 9));
    expected.add(TokenFactory.create(Special.TokenEOF, 11, 1));

    assertIterableEquals(expected, list);
  }

  @Test
  void testStringAssignment() {
    ArrayList<Token> list = new Lexer("x = \"hello how are you ?\"").getNTokens(3);
    ArrayList<Token> listSimpleQuote = new Lexer("x = 'hello how are you ?'").getNTokens(3);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create("x", 1, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 1, 3));
    expected.add(TokenFactory.create(Literal.STRING, "hello how are you ?", 1, 5));

    assertIterableEquals(expected, list);
    assertIterableEquals(expected, listSimpleQuote);
  }

  @Test
  void testUnterminatedString() {
    Lexer lex = new Lexer("x = 'hello");

    ArrayList<Token> list = lex.getNTokens(2);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create("x", 1, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 1, 3));
    assertIterableEquals(expected, list);

    assertThrows(IllegalLexingException.class, lex::nextToken);
  }

  @Test
  void testNestedQuotes() {
    Lexer lex = new Lexer("x = 'hello \"nested\"'");

    ArrayList<Token> list = lex.getNTokens(3);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create("x", 1, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 1, 3));
    expected.add(TokenFactory.create(Literal.STRING, "hello \"nested\"", 1, 5));

    assertIterableEquals(expected, list);
  }

  @Test
  void testEscapedQuotes() {
    Lexer lex = new Lexer("x = 'escaped \\' \\''");

    ArrayList<Token> list = lex.getNTokens(3);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create("x", 1, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 1, 3));
    expected.add(TokenFactory.create(Literal.STRING, "escaped \' \'", 1, 5));

    assertIterableEquals(expected, list);
  }

  @Test
  void testEscapeChars() {
    Lexer lex = new Lexer("x = 'hello\t tab\r carriage\n world'");

    ArrayList<Token> list = lex.getNTokens(3);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create("x", 1, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 1, 3));
    expected.add(TokenFactory.create(Literal.STRING, "hello\t tab\r carriage\n world", 1, 5));

    assertIterableEquals(expected, list);
  }

  @Test
  void testRepeatUntil() {
    String in =
        "a = 0\n" + "repeat\n" + "  a = a + 1\n" + "  print(a)\n" + "until a == 2\n" + "print(a)";
    Lexer lex = new Lexer(in);

    ArrayList<Token> list = lex.getNTokens(0);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create("a", 1, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 1, 3));
    expected.add(TokenFactory.create(Literal.NUMBER, "0", 1, 5));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 1, 6));

    expected.add(TokenFactory.create(Keyword.REPEAT, 2, 1));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 2, 7));

    expected.add(TokenFactory.create("a", 3, 3));
    expected.add(TokenFactory.create(Operator.ASSIGN, 3, 5));
    expected.add(TokenFactory.create("a", 3, 7));
    expected.add(TokenFactory.create(Operator.PLUS, 3, 9));
    expected.add(TokenFactory.create(Literal.NUMBER, "1", 3, 11));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 3, 12));

    expected.add(TokenFactory.create("print", 4, 3));
    expected.add(TokenFactory.create(Delimiter.LPAREN, 4, 8));
    expected.add(TokenFactory.create("a", 4, 9));
    expected.add(TokenFactory.create(Delimiter.RPAREN, 4, 10));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 4, 11));

    expected.add(TokenFactory.create(Keyword.UNTIL, 5, 1));
    expected.add(TokenFactory.create("a", 5, 7));
    expected.add(TokenFactory.create(Operator.EQUALS, 5, 9));
    expected.add(TokenFactory.create(Literal.NUMBER, "2", 5, 12));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 5, 13));

    expected.add(TokenFactory.create("print", 6, 1));
    expected.add(TokenFactory.create(Delimiter.LPAREN, 6, 6));
    expected.add(TokenFactory.create("a", 6, 7));
    expected.add(TokenFactory.create(Delimiter.RPAREN, 6, 8));

    expected.add(TokenFactory.create(Special.TokenEOF, 6, 9));

    assertIterableEquals(expected, list);
  }

  @Test
  void testHexadecimal() {
    String in = "a = 0xff\nb = 0x0.4\nc = 0xfg\nd = 0x";
    Lexer lex = new Lexer(in);

    ArrayList<Token> list = lex.getNTokens(0);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create("a", 1, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 1, 3));
    expected.add(TokenFactory.create(Literal.HEX_NUMBER, "0xff", 1, 5));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 1, 9));

    expected.add(TokenFactory.create("b", 2, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 2, 3));
    expected.add(TokenFactory.create(Literal.HEX_NUMBER, "0x0.4", 2, 5));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 2, 10));

    expected.add(TokenFactory.create("c", 3, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 3, 3));
    expected.add(TokenFactory.create(Literal.HEX_NUMBER, "0xfg", 3, 5));
    expected.add(TokenFactory.create(Delimiter.NEWLINE, 3, 9));

    expected.add(TokenFactory.create("d", 4, 1));
    expected.add(TokenFactory.create(Operator.ASSIGN, 4, 3));
    expected.add(TokenFactory.create(Literal.HEX_NUMBER, "0x", 4, 5));
    expected.add(TokenFactory.create(Special.TokenEOF, 4, 7));

    assertIterableEquals(expected, list);
  }

  @Test
  void testBitwiseOperators() {
    String in = "& | << <= >> >= ~ ~= ";
    Lexer lex = new Lexer(in);

    ArrayList<Token> list = lex.getNTokens(0);

    ArrayList<Token> expected = new ArrayList<Token>();
    expected.add(TokenFactory.create(Operator.B_AND, 1, 1));
    expected.add(TokenFactory.create(Operator.B_OR, 1, 3));
    expected.add(TokenFactory.create(Operator.LEFT_SHIFT, 1, 5));
    expected.add(TokenFactory.create(Operator.LTE, 1, 8));
    expected.add(TokenFactory.create(Operator.RIGHT_SHIFT, 1, 11));
    expected.add(TokenFactory.create(Operator.GTE, 1, 14));
    expected.add(TokenFactory.create(Operator.B_XOR, 1, 17));
    expected.add(TokenFactory.create(Operator.NOT_EQUAL, 1, 19));
    expected.add(TokenFactory.create(Special.TokenEOF, 1, 22));

    assertIterableEquals(expected, list);
  }
}

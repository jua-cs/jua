package jua.evaluator;

import jua.ast.Statement;
import java.util.ArrayList;
import jua.lexer.Lexer;
import org.junit.jupiter.api.Test;
import jua.parser.IllegalParseException;
import jua.parser.Parser;

public class EvaluatorTest {
  private static ArrayList<Statement> setup(String in) throws IllegalParseException {
    return new Parser((new Lexer(in)).getNTokens(0)).parse();
  }

  @Test
  void testStringConcat() throws IllegalParseException {
    var stmts = setup("'abc' .. 'def'");
    System.out.println(stmts);
  }
}

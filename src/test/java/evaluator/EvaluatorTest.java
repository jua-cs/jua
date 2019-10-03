package evaluator;

import ast.Statement;
import java.util.ArrayList;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import parser.IllegalParseException;
import parser.Parser;

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

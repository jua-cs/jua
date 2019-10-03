package jua.evaluator;

import java.util.ArrayList;
import jua.ast.Statement;
import jua.ast.StatementExpression;
import jua.lexer.Lexer;
import jua.objects.LuaObject;
import jua.parser.IllegalParseException;
import jua.parser.Parser;
import org.junit.jupiter.api.Test;

public class EvaluatorTest {
  private static ArrayList<Statement> setup(String in) throws IllegalParseException {
    return new Parser((new Lexer(in)).getNTokens(0)).parse();
  }

  private LuaObject setupExpr(String in) throws LuaRuntimeException, IllegalParseException {
    return setupExpr(in, new Evaluator());
  }

  private LuaObject setupExpr(String in, Evaluator evaluator)
      throws LuaRuntimeException, IllegalParseException {
    var expr =
        ((StatementExpression) new Parser((new Lexer(in)).getNTokens(0)).parse().get(0)).getExpr();
    return expr.evaluate(evaluator);
  }

  @Test
  void testStringConcat() throws IllegalParseException, LuaRuntimeException {
    var expr = setupExpr("'abc' .. 'def'");
    System.out.println(expr);
  }
}

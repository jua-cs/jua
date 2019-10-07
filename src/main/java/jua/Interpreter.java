package jua;

import java.io.OutputStream;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.lexer.Lexer;
import jua.parser.IllegalParseException;
import jua.parser.Parser;

public class Interpreter {
  private Lexer lexer;
  private Parser parser;
  private Scope scope;

  public Interpreter(String in) {
    lexer = new Lexer(in);
    parser = new Parser(lexer.getNTokens(0));
    scope = new Scope();
  }

  public Interpreter(String in, OutputStream out) {
    lexer = new Lexer(in);
    parser = new Parser(lexer.getNTokens(0));
    scope = new Scope(out);
  }

  public void run() throws IllegalParseException {
    var stmts = parser.parse();
    stmts
        .getChildren()
        .forEach(
            stmt -> {
              try {
                stmt.evaluate(scope);
              } catch (LuaRuntimeException e) {
                e.printStackTrace();
              }
            });
  }
}

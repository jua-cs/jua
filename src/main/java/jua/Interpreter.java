package jua;

import java.io.OutputStream;
import jua.ast.Statement;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.lexer.Lexer;
import jua.objects.LuaNil;
import jua.objects.LuaObject;
import jua.parser.IllegalParseException;
import jua.parser.Parser;
import util.BufferedChannel;

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

  public Interpreter(BufferedChannel<Character> in) {
    lexer = new Lexer(in);
    parser = new Parser(lexer.getOut());
    scope = new Scope();
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

  public void start() {

    // start lexer worker
    new Thread(
            () -> {
              try {
                lexer.start();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
        .start();

    // start parser worker
    new Thread(
            () -> {
              try {
                parser.start();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
        .start();

    // start evaluation
    BufferedChannel<Statement> in = parser.getOut();
    while (true) {
      System.out.print("> ");
      try {
        Statement s = in.read();
        LuaObject o = s.evaluate(scope);
        if (!(o == LuaNil.getInstance())) {
          System.out.println(o.repr());
        }
      } catch (InterruptedException | LuaRuntimeException e) {
        e.printStackTrace();
      }
    }
  }
}

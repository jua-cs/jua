package jua;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import jua.ast.Statement;
import jua.ast.StatementEOP;
import jua.ast.StatementExpression;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.lexer.Lexer;
import jua.objects.LuaObject;
import jua.parser.IllegalParseException;
import jua.parser.Parser;
import util.BufferedChannel;

public class Interpreter {
  private Lexer lexer;
  private Parser parser;
  private Scope scope;
  private BufferedChannel<Character> in = new BufferedChannel<>();
  private OutputStream out = System.out;
  private Thread lexerWorker;
  private Thread parserWorker;
  private Thread evaluationWorker;

  public Interpreter(String in) {
    lexer = new Lexer(in);
    parser = new Parser(lexer.getNTokens(0));
    scope = new Scope();
  }

  public Interpreter(String in, OutputStream out) {
    lexer = new Lexer(in);
    parser = new Parser(lexer.getNTokens(0));
    scope = new Scope(out);
    this.out = out;
  }

  public Interpreter(BufferedChannel<Character> in) {
    this.in = in;
    lexer = new Lexer(in);
    parser = new Parser(lexer.getOut());
    scope = new Scope();
  }

  public Interpreter(BufferedChannel<Character> in, OutputStream out) {
    this.in = in;
    lexer = new Lexer(in);
    parser = new Parser(lexer.getOut());
    scope = new Scope(out);
    this.out = out;
  }

  public BufferedChannel<Character> getIn() {
    return in;
  }

  public static String eval(String in) throws IllegalParseException, LuaRuntimeException {
    var sb = new ByteArrayOutputStream();
    Interpreter interpreter = new Interpreter(in, sb);
    interpreter.run();
    return sb.toString();
  }

  public void run() throws IllegalParseException, LuaRuntimeException {
    var stmts = parser.parse().getChildren();
    for (Statement stmt : stmts) {
      stmt.evaluate(scope);
    }
  }

  public void start(boolean isInteractive) {

    // start lexer worker
    lexerWorker =
        new Thread(
            () -> {
              try {
                lexer.start(isInteractive);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });
    lexerWorker.start();

    // start parser worker
    parserWorker =
        new Thread(
            () -> {
              try {
                parser.start(isInteractive);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });
    parserWorker.start();
    // start evaluation
    BufferedChannel<Statement> in = parser.getOut();
    evaluationWorker =
        new Thread(
            () -> {
              while (true) {
                if (isInteractive) {

                  try {
                    out.write("> ".getBytes());
                    out.flush();
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                }
                try {
                  Statement s = in.read();
                  if (s instanceof StatementEOP) {
                    break;
                  }
                  LuaObject o = s.evaluate(scope);
                  if (s instanceof StatementExpression) {
                    try {
                      out.write(o.repr().getBytes());
                      out.flush();
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                  }
                } catch (LuaRuntimeException | InterruptedException e) {
                  e.printStackTrace();
                  break;
                }
              }
            });
    evaluationWorker.start();
  }
}

package jua;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import jua.ast.Statement;
import jua.ast.StatementEOP;
import jua.ast.StatementExpression;
import jua.evaluator.IllegalLexingException;
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
  private OutputStream stdout = System.out;
  private OutputStream stderr = System.err;
  private Thread lexerWorker;
  private Thread parserWorker;
  private Thread evaluationWorker;

  public Interpreter(String in) {
    lexer = new Lexer(in);
    parser = new Parser(lexer.getNTokens(0));
    scope = new Scope();
  }

  public Interpreter(String in, OutputStream stdout) {
    lexer = new Lexer(in);
    parser = new Parser(lexer.getNTokens(0));
    scope = new Scope(stdout);
    this.stdout = stdout;
  }

  public Interpreter(BufferedChannel<Character> in) {
    this.in = in;
    lexer = new Lexer(in);
    parser = new Parser(lexer.getOut());
    scope = new Scope();
  }

  public Interpreter(BufferedChannel<Character> in, OutputStream stdout) {
    this.in = in;
    lexer = new Lexer(in);
    parser = new Parser(lexer.getOut());
    scope = new Scope(stdout);
    this.stdout = stdout;
  }

  public Interpreter(BufferedChannel<Character> in, OutputStream stdout, OutputStream stderr) {
    this.in = in;
    lexer = new Lexer(in);
    parser = new Parser(lexer.getOut());
    scope = new Scope(stdout);
    this.stdout = stdout;
    this.stderr = stderr;
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

  private void loop(boolean isInteractive) {
    while (true) {
      if (isInteractive) {
        try {
          stdout.write("> ".getBytes());
          stdout.flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      try {
        Statement s = parser.getOut().read();
        if (s instanceof StatementEOP) {
          break;
        }
        LuaObject o = s.evaluate(scope);
        if (s instanceof StatementExpression) {
          try {
            stdout.write((o.repr() + '\n').getBytes());
            stdout.flush();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      } catch (LuaRuntimeException e) {
        try {
          stderr.write((e.toString() + '\n').getBytes());
          stderr.flush();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  public void start(boolean isInteractive) {

    // start lexer worker
    lexerWorker =
        new Thread(
            () -> {
              while (true) {
                try {
                  lexer.start(isInteractive);
                  // lexer ends normally, exiting
                  break;
                } catch (InterruptedException e) {
                  e.printStackTrace();
                  break;
                } catch (IllegalLexingException e) {
                  // print the exception and resume the lexer if in interactive mode
                  try {
                    stderr.write((e.toString() + '\n').getBytes());
                    stderr.flush();
                  } catch (IOException ex) {
                    ex.printStackTrace();
                  }
                  if (!isInteractive) {
                    break;
                  }
                }
              }
            });
    lexerWorker.start();

    // start parser worker
    parserWorker =
        new Thread(
            () -> {
              while (true) {
                try {
                  parser.start(isInteractive);
                  // parser ends normally, exiting
                  break;
                } catch (InterruptedException e) {
                  e.printStackTrace();
                  break;
                } catch (IllegalParseException e) {
                  // print the exception and resume the parser if in interactive mode
                  try {
                    stderr.write((e.toString() + '\n').getBytes());
                    stderr.flush();
                  } catch (IOException ex) {
                    ex.printStackTrace();
                  }
                  if (!isInteractive) {
                    break;
                  }
                }
              }
            });
    parserWorker.start();

    evaluationWorker = new Thread(() -> loop(isInteractive));
    evaluationWorker.start();
  }
}

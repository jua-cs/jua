package jua;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

import jua.lexer.Lexer;
import jua.parser.IllegalParseException;
import jua.parser.Parser;
import jua.token.Token;
import util.BufferedChannel;

public class Main {

  public static void main(String[] args) throws IllegalParseException {
    if (args.length > 0) {
      debug();
    } else {
      repl();
    }
  }

  private static void noninteractive(BufferedChannel<Character> in) throws InterruptedException {
    try {
      while (true) {
        int ch = System.in.read();
        if (ch == -1) {
          break;
        }
        in.add((char) ch);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    in.add('\0');
    in.close();
  }

  private static void interactive(BufferedChannel<Character> in) throws InterruptedException {
    var console = System.console();

    while (true) {
      String line = console.readLine();

      // Console is closed
      if (line == null) {
        break;
      }
      line += '\n';
      for (Character ch : line.toCharArray()) {
        in.add(ch);
      }
    }
    in.close();
  }

  private static void repl() {
    BufferedChannel<Character> in = new BufferedChannel<>();
    var interpreter = new Interpreter(in);
    boolean isInteractive = System.console() != null;
    new Thread(
            () -> {
              try {
                if (isInteractive) {
                  interactive(in);
                } else {
                  noninteractive(in);
                }
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
        .start();
    interpreter.start(isInteractive);
  }

  private static void debug() throws IllegalParseException {
    String text = null;
    try {
      text = new String(System.in.readAllBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("---");
    System.out.println("Lexing:");
    ArrayList<Token> tokens = (new Lexer(text)).getNTokens(0);
    tokens.forEach(System.out::println);
    System.out.println("---");
    System.out.println("Parsing:");
    Parser parser = new Parser(tokens);
    var stmts = parser.parse();
    stmts.getChildren().forEach(System.out::println);
    System.out.println("---");
    System.out.println("Evaluation:");
    var interpreter = new Interpreter(text);
    interpreter.run();
    System.out.println("---");
  }
}

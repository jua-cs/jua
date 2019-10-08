package jua;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import jua.lexer.Lexer;
import jua.parser.IllegalParseException;
import jua.parser.Parser;
import jua.token.Token;
import util.BufferedChannel;

public class Main {

  public static void main(String[] args) throws IllegalParseException {
      repl();
  }


  private static void repl() {

      BufferedChannel<Character> in = new BufferedChannel<>();
      var scanner = new Scanner(System.in);
      new Thread(() -> {
          while (true) {
              String line = scanner.nextLine() + '\n';
              for (Character ch : line.toCharArray()) {
                  try {
                      in.add(ch);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          }
      }).start();
      var interpreter = new Interpreter(in);
      interpreter.start();
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

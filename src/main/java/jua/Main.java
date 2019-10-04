package jua;

import java.io.IOException;
import java.util.ArrayList;
import jua.evaluator.LuaRuntimeException;
import jua.evaluator.Scope;
import jua.lexer.Lexer;
import jua.parser.IllegalParseException;
import jua.parser.Parser;
import jua.token.Token;

public class Main {

  public static void main(String[] args) throws IllegalParseException {
    debug();
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
    var scope = new Scope();
    stmts
        .getChildren()
        .forEach(
            stmt -> {
              try {
                System.out.printf("> %s\n", stmt.evaluate(scope).repr());
              } catch (LuaRuntimeException e) {
                e.printStackTrace();
              }
            });
    System.out.println("---");
  }
}

package test;

import java.io.IOException;
import java.util.ArrayList;
import lexer.Lexer;
import parser.IllegalParseException;
import parser.Parser;
import token.Token;

public class Main {

  public static void main(String[] args) throws IllegalParseException {
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
    parser.parse().forEach(System.out::println);
    System.out.println("---");
  }
}

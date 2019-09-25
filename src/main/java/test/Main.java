package test;

import java.io.IOException;
import java.util.ArrayList;
import lexer.Lexer;
import parser.Parser;
import token.Token;

public class Main {

  public static void main(String[] args) {
    String text = null;
    try {
      text = new String(System.in.readAllBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }

    ArrayList<Token> tokens = (new Lexer(text)).getNTokens(0);
    tokens.forEach(System.out::println);
    System.out.println("---");
    Parser parser = new Parser(tokens);
    parser.parse();
  }
}

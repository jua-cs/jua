package test;

import lexer.Lexer;
import token.Token;
import token.TokenType;

import java.io.IOException;

public class Main {

  public static void main(String[] args) {
    String text = null;
    try {
      text = new String(System.in.readAllBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }

    Lexer lex = new Lexer(text);
    Token tok;
    do {
      tok = lex.nextToken();
      System.out.println(tok);
    } while (tok.getType() != TokenType.EOF);
  }
}

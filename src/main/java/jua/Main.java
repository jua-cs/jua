package jua;

import java.io.IOException;
import java.util.ArrayList;
import jua.ast.StatementExpression;
import jua.evaluator.Evaluator;
import jua.evaluator.LuaRuntimeException;
import jua.lexer.Lexer;
import jua.parser.IllegalParseException;
import jua.parser.Parser;
import jua.token.Token;

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
    var stmts = parser.parse();
    stmts.forEach(System.out::println);
    System.out.println("---");
    System.out.println("Evaluation:");
    var eval = new Evaluator();
    stmts.forEach(
        stmt -> {
          if (stmt instanceof StatementExpression) {
            try {
              System.out.printf("> %s\n", ((StatementExpression) stmt).getExpr().evaluate(eval));
            } catch (LuaRuntimeException e) {
              e.printStackTrace();
            }
          }
        });
    System.out.println("---");
  }
}

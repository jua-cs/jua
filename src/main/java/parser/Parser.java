package parser;

import ast.*;
import java.util.ArrayList;
import token.Operator;
import token.Token;
import token.TokenOperator;
import token.TokenType;

public class Parser {
  private ArrayList<Token> tokens;
  private int currentPos;
  private AST ast = new AST();

  public Parser(ArrayList<Token> tokens) {
    this.tokens = tokens;
  }

  private Token currentToken() {
    return tokens.get(currentPos);
  }

  private Token nextToken() {
    return tokens.get(1 + currentPos);
  }

  private void advanceTokens() {
    currentPos++;
  }

  private StatementAssignment parseAssignment() {
    ExpressionIdentifier identifier = new ExpressionIdentifier(currentToken());
    advanceTokens();
    Token tok = currentToken();
    advanceTokens();
    Expression expr = parseExpression();

    return new StatementAssignment(tok, identifier, expr);
  }

  private Expression parseExpression() {
    // We parse until we have a non binary operator as the next character and
    // we are not currently waiting the right side of
    // A binary or unary operator

    // TODO

    return new ExpressionLiteral(currentToken());
  }

  public void parse() {
    // TODO: fixme: Multi assignment is not supported
    while (currentToken().getType() != TokenType.EOF) {

      // Assignment
      if (currentToken().getType() == TokenType.LITERAL
          && nextToken().getType() == TokenType.OPERATOR
          && ((TokenOperator) nextToken()).getOperator() == Operator.ASSIGN) {
        Statement stmt = parseAssignment();
        System.out.printf("Assignment detected ! Assignment: %s", stmt);
        return;
      }
    }
  }
}

package parser;

import ast.*;
import java.util.ArrayList;
import token.*;

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

  private StatementAssignment parseAssignment() throws IllegalParseException {
    ExpressionIdentifier identifier = new ExpressionIdentifier(currentToken());
    advanceTokens();
    Token tok = currentToken();
    advanceTokens();
    Expression expr = parseExpression();

    return new StatementAssignment(tok, identifier, expr);
  }

  private Expression parseExpression() throws IllegalParseException {
    // We parse until we have a non binary operator as the next character and
    // we are not currently waiting the right side of
    // A binary or unary operator

    // TODO

    Token tok = currentToken();

    switch (tok.getType()) {
      case OPERATOR:
        TokenOperator tokOp = (TokenOperator) tok;
        switch (tokOp.getOperator().getArity()) {
          case UNARY:
            advanceTokens();
            return ExpressionUnaryFactory.create(tokOp, parseExpression());
          case BINARY:
            throw new IllegalParseException(
                String.format(
                    "Unexpected binary operator: %s, token: %s", tokOp.getOperator(), tokOp));
        }
        break;
      case DELIMITER:
        TokenDelimiter tokDelim = (TokenDelimiter) tok;
        Delimiter delim = tokDelim.getDelimiter();
        // An expression can only start with the following delimiters:
        // (, {,
        if (delim != Delimiter.LPAREN && delim != Delimiter.LBRACE) {
          throw new IllegalParseException(
              String.format("Unexpected delimiter: %s, token: %s", delim, tok));
        }

        break;
      case KEYWORD:
        break;
      case LITERAL:
        break;
      case EOF:
        throw new IllegalParseException(String.format("Unexpected EOF token: %s", tok));
      case INVALID:
        throw new IllegalParseException(String.format("Invalid token: %s", tok));
      default:
        throw new IllegalParseException("Unexpected value: " + tok.getType());
    }

    return new ExpressionLiteral(currentToken());
  }

  public void parse() throws IllegalParseException {
    // TODO: fixme: Multi assignment is not supported
    while (currentToken().getType() != TokenType.EOF) {

      // Assignment
      // Occurs when current token is literal and next token is the assign operator
      if (currentToken().getType() == TokenType.LITERAL
          && nextToken().getType() == TokenType.OPERATOR
          && ((TokenOperator) nextToken()).getOperator() == Operator.ASSIGN) {
        Statement stmt = parseAssignment();
        // TODO add to statement list
        System.out.printf("Assignment detected ! Assignment: %s", stmt);
        return;
      }
    }
  }
}

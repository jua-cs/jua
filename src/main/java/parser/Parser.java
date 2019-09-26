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

        // TODO(Sami)
        // processing

        Token endingToken = currentToken();
        if (endingToken.getType() != TokenType.DELIMITER) {
          throw new IllegalParseException(
              String.format("Expected a delimiter token but got: %s", tok));
        }

        TokenDelimiter endingDelim = (TokenDelimiter) endingToken;
        if (!endingDelim.getDelimiter().matches(delim)) {
          throw new IllegalParseException(
              String.format(
                  "Unexpected closing delimiter, opener: %s, closer: %s", tok, endingDelim));
        }
        break;
      case KEYWORD:
        break;
      case LITERAL:
      case IDENTIFIER:
        Token nextTok = nextToken();
        switch (nextTok.getType()) {
          case OPERATOR:
            TokenOperator nextTokOp = (TokenOperator) nextTok;
            if (nextTokOp.getOperator().getArity() == Arity.BINARY) {
              advanceTokens();
              advanceTokens();
              Expression expr;
              switch (tok.getType()) {
                case LITERAL:
                  expr = new ExpressionLiteral(tok);
                case IDENTIFIER:
                  expr = new ExpressionIdentifier(tok);
              }
              return ExpressionBinaryFactory.create(nextTokOp, expr, parseExpression());
            }
            throw new IllegalParseException(
                String.format("Unexpected unary operator: %s", nextTokOp));

          case DELIMITER:
            TokenDelimiter nextTokDelim = (TokenDelimiter) nextTok;
            switch (nextTokDelim.getDelimiter()) {
              case LPAREN:
                // TODO(Remi) parseFunction call
            }
        }

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
        ast.addChild(stmt);
        advanceTokens();
      }
    }
  }

  public AST getAst() {
    return ast;
  }
}

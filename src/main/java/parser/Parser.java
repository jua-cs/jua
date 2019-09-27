package parser;

import ast.*;
import java.util.ArrayList;
import java.util.HashMap;
import token.*;

public class Parser {
  private ArrayList<Token> tokens;
  private int currentPos;
  private AST ast = new AST();
  private HashMap<Token, PrefixParser> tokenPrefixParserHashMap;
  private HashMap<Token, InfixParser> tokenInfixParserHashMap;

  public Parser(ArrayList<Token> tokens) {
    this.tokens = tokens;
    this.tokenInfixParserHashMap = new HashMap<>();
    this.tokenPrefixParserHashMap = new HashMap<>();

    // Register the class which implements InfixParser interface
    // FIXME: ^ has greater precedence than unary operators, this is not handled at the moment
    registerBinaryOperator(Operator.PLUS, 4);
    registerBinaryOperator(Operator.ASTERISK, 5);
    registerBinaryOperator(Operator.SLASH, 5);
    registerBinaryOperator(Operator.MINUS, 4);
    registerBinaryOperator(Operator.CARAT, 6);
    registerBinaryOperator(Operator.PERCENT, 5);

    registerBinaryOperator(Operator.AND, 2);
    registerBinaryOperator(Operator.OR, 1);

    registerBinaryOperator(Operator.EQUALS, 3);
    registerBinaryOperator(Operator.NOT_EQUAL, 3);
    registerBinaryOperator(Operator.GT, 3);
    registerBinaryOperator(Operator.GTE, 3);
    registerBinaryOperator(Operator.LT, 3);
    registerBinaryOperator(Operator.LTE, 3);

    // TODO Add braces
    // Register the class which implements PrefixParser interface
    register(TokenFactory.create(Delimiter.LPAREN), new ParenthesisParser());
    register(TokenFactory.create(Operator.NOT), (PrefixParser) new OperatorParser(0));
    register(TokenFactory.create(Operator.MINUS), (PrefixParser) new OperatorParser(0));
  }


  Token currentToken() {
    return tokens.get(currentPos);
  }

  Token nextToken() {
    return tokens.get(1 + currentPos);
  }


  void advanceTokens() {
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

  protected Expression parseExpression2() throws IllegalParseException {
    Token tok = currentToken();
    advanceTokens();

    PrefixParser prefix = tokenPrefixParserHashMap.get(tok.getType());
    if (prefix == null) {
      throw new IllegalParseException(String.format("Unexpected token: %s", tok));
    }

    // TODO: handle precedence
    Expression lhs = prefix.parsePrefix(this, tok);

    // Check if we have an infix operator after the left hand side
    Token nextTok = currentToken();
    InfixParser infix = tokenInfixParserHashMap.get(tok.getType());
    if (infix == null) {
      return lhs;
    }

    advanceTokens();
    return infix.parseInfix(this, nextTok, lhs);
  }

  protected Expression parseExpression() throws IllegalParseException {
    // We parse until we have a non binary operator as the next character and
    // we are not currently waiting the right side of
    // A binary or unary operator
    Token tok = currentToken();
    System.out.printf("Parse expression, token: %s\n", tok);

    switch (tok.getType()) {
      case OPERATOR:
        TokenOperator tokOp = (TokenOperator) tok;
        switch (tokOp.getOperator().getArity()) {
          case UNARY:
            advanceTokens();
            return ExpressionFactory.create(tokOp, parseExpression());
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

        advanceTokens();
        Expression expr = parseExpression();

        Token endingToken = currentToken();
        if (endingToken.getType() != TokenType.DELIMITER) {
          throw new IllegalParseException(
              String.format("Expected a delimiter token but got: %s", endingToken));
        }

        TokenDelimiter endingDelim = (TokenDelimiter) endingToken;
        if (!endingDelim.getDelimiter().matches(delim)) {
          throw new IllegalParseException(
              String.format(
                  "Unexpected closing delimiter, opener: %s, closer: %s", tok, endingDelim));
        }

        // Eat the closing delimiter
        advanceTokens();
        return expr;
      case KEYWORD:
        // TODO
        break;
      case IDENTIFIER:
      case LITERAL:
        Token nextTok = nextToken();

        switch (nextTok.getType()) {
          case OPERATOR:
            TokenOperator nextTokOp = (TokenOperator) nextTok;
            // ALlow only binary operators if expression started with a literal
            if (nextTokOp.getOperator().getArity() != Arity.BINARY) {
              throw new IllegalParseException(
                  String.format(
                      "Expected binary operator but got: %s, token: %s",
                      nextTokOp.getOperator(), nextTokOp));
            }

            advanceTokens();
            advanceTokens();
            Expression lhs = ExpressionFactory.create(tok);
            Expression rhs = parseExpression();
            return ExpressionFactory.create(nextTokOp, lhs, rhs);
          case DELIMITER:
            // If we encounter a closing delimiter just return the current literal
            TokenDelimiter nextTokDelim = (TokenDelimiter) nextTok;
            Delimiter nextDelim = nextTokDelim.getDelimiter();
            if (nextDelim != Delimiter.RPAREN && nextDelim != Delimiter.RBRACE) {
              throw new IllegalParseException(
                  String.format("Unexpected delimiter: %s, token: %s", nextDelim, nextTok));
            }

            advanceTokens();
            return ExpressionFactory.create(tok);
            // TODO: make sure that a keyword can't be after a literal
            // case KEYWORD:
            // break;
          default:
            throw new IllegalParseException(
                String.format("Unexpected token: %s after literal: %s", nextTok, tok));
        }

      case EOF:
        throw new IllegalParseException(String.format("Unexpected EOF token: %s", tok));
      case INVALID:
        throw new IllegalParseException(String.format("Invalid token: %s", tok));
      default:
        throw new IllegalParseException("Unexpected value: " + tok.getType());
    }

    return ExpressionFactory.create(currentToken());
  }


  public void parse() throws IllegalParseException {
    // TODO: fixme: Multi assignment is not supported
    while (currentPos < tokens.size() && currentToken().getType() != TokenType.EOF) {
      // Assignment
      // Occurs when current token is literal and next token is the assign operator
      TokenType currType = currentToken().getType();
      if ((currType == TokenType.LITERAL || currType == TokenType.IDENTIFIER)
          && nextToken().getType() == TokenType.OPERATOR
          && ((TokenOperator) nextToken()).getOperator() == Operator.ASSIGN) {
        Statement stmt = parseAssignment();
        ast.addChild(stmt);
        advanceTokens();
      } else {
        throw new IllegalParseException(String.format("Unknown token: %s", currentToken()));
      }
    }
  }

  public AST getAst() {
    return ast;
  }

  protected void registerBinaryOperator(Operator op, int precedence) {
    tokenPrefixParserHashMap.put(TokenFactory.create(op), new OperatorParser(precedence));
  }

  protected void register(Token type, PrefixParser parser) {
    tokenPrefixParserHashMap.put(type, parser);
  }

  protected void register(Token type, InfixParser parser) {
    tokenInfixParserHashMap.put(type, parser);
  }
}

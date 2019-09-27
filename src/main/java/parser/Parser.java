package parser;

import ast.*;
import java.util.ArrayList;
import java.util.HashMap;
import token.*;

public class Parser {
  // Used to access the map
  private static final Token identifierKey = TokenFactory.create("", 0, 0);
  private static final Token literalKey = TokenFactory.create(0, 0, 0);
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

    register(TokenFactory.create(Delimiter.LPAREN), new FunctionCallParser(7));

    // TODO Add braces
    // Register the class which implements PrefixParser interface
    register(TokenFactory.create(Delimiter.LPAREN), new ParenthesisParser());
    register(TokenFactory.create(Operator.NOT), (PrefixParser) new OperatorParser(0));
    register(TokenFactory.create(Operator.MINUS), (PrefixParser) new OperatorParser(0));
    register(literalKey, new LiteralParser());
    register(identifierKey, new IdentifierParser());
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

  // TODO: add others overloading
  void consume(Delimiter delimiter) throws IllegalParseException {
    if (!nextToken().isSubtype(delimiter)) {
      throw new IllegalParseException("Expecting " + delimiter + " but found " + nextToken());
    }

    advanceTokens();
  }

  private StatementAssignment parseAssignment() throws IllegalParseException {
    ExpressionIdentifier identifier = new ExpressionIdentifier(currentToken());
    advanceTokens();
    Token tok = currentToken();
    advanceTokens();
    Expression expr = parseExpression();

    return new StatementAssignment(tok, identifier, expr);
  }

  protected Expression parseExpression() throws IllegalParseException {
    return parseExpression(0);
  }

  protected Expression parseExpression(int precedence) throws IllegalParseException {
    Token tok = currentToken();
    advanceTokens();

    PrefixParser prefix = getPrefixParser(tok);
    if (prefix == null) {
      throw new IllegalParseException(String.format("Unexpected token: %s", tok));
    }

    Expression lhs = prefix.parsePrefix(this, tok);
    while (precedence < getCurrTokenPrecedence()) {
      tok = currentToken();
      advanceTokens();

      // We are 100 % sure that infix is not null here because getCurrTokenPrecedence
      // returns 0 otherwise and precedence has to be positive
      InfixParser infix = tokenInfixParserHashMap.get(tok);
      lhs = infix.parseInfix(this, tok, lhs);
    }
    return lhs;
  }

  private int getCurrTokenPrecedence() {
    InfixParser parser = tokenInfixParserHashMap.get(currentToken());

    return parser != null ? parser.getPrecedence() : 0;
  }

  protected void registerBinaryOperator(Operator op, int precedence) {
    tokenInfixParserHashMap.put(TokenFactory.create(op), new OperatorParser(precedence));
  }

  protected void register(Token type, PrefixParser parser) {
    tokenPrefixParserHashMap.put(type, parser);
  }

  protected void register(Token type, InfixParser parser) {
    tokenInfixParserHashMap.put(type, parser);
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
        ast.addChild(parseAssignment());
        advanceTokens();
      } else {
        ast.addChild(new StatementExpression(parseExpression()));
      }
    }
  }

  private PrefixParser getPrefixParser(Token token) {
    switch (token.getType()) {
      case IDENTIFIER:
        return tokenPrefixParserHashMap.get(identifierKey);
      case LITERAL:
        return tokenPrefixParserHashMap.get(literalKey);
      default:
        return tokenPrefixParserHashMap.get(token);
    }
  }

  public AST getAst() {
    return ast;
  }
}

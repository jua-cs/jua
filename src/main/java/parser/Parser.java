package parser;

import ast.*;
import java.util.ArrayList;
import java.util.HashMap;
import token.*;

public class Parser {
  // Used to access the map
  private static final Token identifierKey = TokenFactory.create("", 0, 0);
  private static final Token literalKey = TokenFactory.create(Literal.BOOLEAN, "0", 0, 0);
  private ArrayList<Token> tokens;
  private int currentPos;
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

    register(TokenFactory.create(Delimiter.LPAREN), new FunctionCallParser(8));

    // TODO Add braces
    // Register the class which implements PrefixParser interface
    register(TokenFactory.create(Delimiter.LPAREN), new ParenthesisParser(7));
    register(TokenFactory.create(Operator.NOT), (PrefixParser) new OperatorParser(7));
    register(TokenFactory.create(Operator.MINUS), (PrefixParser) new OperatorParser(7));
    register(TokenFactory.create(Keyword.FUNCTION), (PrefixParser) new FunctionExprParser());
    register(literalKey, new LiteralParser());
    register(identifierKey, new IdentifierParser());
  }

  Token currentToken() {
    return tokens.get(currentPos);
  }

  private Token nextToken() {
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

  private Expression parseExpression() throws IllegalParseException {
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

  public ArrayList<Statement> parse() throws IllegalParseException {
    ArrayList<Statement> statements = new ArrayList<>();

    while (currentPos < tokens.size() && currentTokenIsValid()) {
      statements.add(parseStatement());
    }

    return statements;
  }

  protected StatementList parseBlockStatement() throws IllegalParseException {
    StatementList list = new StatementList(currentToken());

    while (currentTokenIsValid() && !isBlockEnd()) {
      Statement child = parseStatement();
      list.addChild(child);
      if (child instanceof StatementReturn) {
        break;
      }
    }
    return list;
  }

  protected Statement parseStatement() throws IllegalParseException {
    if (isAssignmentStatement()) {
      // TODO: fixme: Multi assignment is not supported
      // Assignment
      // Occurs when current token is literal and next token is the assign operator
      return parseAssignment();
    } else if (isFunctionStatement()) {
      return parseFunctionStatement();
    } else if (isReturnStatement()) {
      return parseReturnStatement();
    } else {
      return new StatementExpression(parseExpression());
    }
  }

  private StatementFunction parseFunctionStatement() throws IllegalParseException {
    // Next token should be an identifier
    Token tok = currentToken();
    advanceTokens();

    ExpressionIdentifier funcName = new ExpressionIdentifier(currentToken());
    advanceTokens();

    // Parse args
    ArrayList<ExpressionIdentifier> args = parseFuncArgs();
    StatementList stmts = parseBlockStatement();

    // consume END of function statement
    advanceTokens();
    return new StatementFunction(tok, funcName, new ExpressionFunction(tok, args, stmts));
  }

  private boolean isFunctionStatement() {
    Token tok = currentToken();
    boolean isFunc =
        tok.getType() == TokenType.KEYWORD && ((TokenKeyword) tok).getKeyword() == Keyword.FUNCTION;
    boolean nextIsIdent = nextToken().getType() == TokenType.IDENTIFIER;
    return isFunc && nextIsIdent;
  }

  private StatementReturn parseReturnStatement() throws IllegalParseException {
    Token tok = currentToken();
    advanceTokens();
    StatementReturn stmt = new StatementReturn(tok, parseExpression());
    return stmt;
  }

  private boolean isReturnStatement() {
    Token tok = currentToken();
    return tok.getType() == TokenType.KEYWORD
        && ((TokenKeyword) tok).getKeyword() == Keyword.RETURN;
  }

  private boolean isAssignmentStatement() {
    TokenType currType = currentToken().getType();
    boolean isIdent = currType == TokenType.IDENTIFIER;
    boolean nextTokIsAssign =
        nextToken().getType() == TokenType.OPERATOR
            && ((TokenOperator) nextToken()).getOperator() == Operator.ASSIGN;
    return isIdent && nextTokIsAssign;
  }

  private boolean currentTokenIsValid() {
    return currentToken().getType() != TokenType.EOF
        && currentToken().getType() != TokenType.INVALID;
  }

  private boolean isBlockEnd() {
    Token tok = currentToken();
    return tok.getType() == TokenType.KEYWORD
        && (((TokenKeyword) tok).getKeyword() == Keyword.END
            || ((TokenKeyword) tok).getKeyword() == Keyword.ELSE
            || ((TokenKeyword) tok).getKeyword() == Keyword.ELSEIF);
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

  protected ArrayList<ExpressionIdentifier> parseFuncArgs() throws IllegalParseException {
    advanceTokens();
    ArrayList<ExpressionIdentifier> args = new ArrayList<>();
    // if there is no args, we look for a ')'

    Token tok = currentToken();
    while (!tok.isSubtype(Delimiter.RPAREN)) {
      if (tok.getType() != TokenType.IDENTIFIER) {
        throw new IllegalParseException(
            String.format("Expected identifier in function args but got: %s", tok));
      }
      args.add(new ExpressionIdentifier(tok));
      advanceTokens();
      tok = currentToken();
    }

    // Consume ')'
    advanceTokens();

    return args;
  }
}

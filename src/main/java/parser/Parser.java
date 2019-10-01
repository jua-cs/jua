package parser;

import ast.*;
import java.util.ArrayList;
import java.util.HashMap;
import token.*;

public class Parser {


  // Used to access the HasmMap with Token, with still a functionning equals for Lexer
  private class TokenHashmMapKey {
    private final Token token;

    public TokenHashmMapKey(Token token) {
      this.token = token;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      TokenHashmMapKey that = (TokenHashmMapKey) o;

      return (token != null) && token.getClass() == that.token.getClass() && token.hashCode() == that.token.hashCode();
    }

    @Override
    public int hashCode() {
      return token != null ? token.hashCode() : 0;
    }
  }

  private static final Token identifierKey = TokenFactory.create("", 0, 0);
  private static final Token literalKey = TokenFactory.create(Literal.BOOLEAN, "0", 0, 0);
  private ArrayList<Token> tokens;
  private int currentPos;
  private HashMap<TokenHashmMapKey, PrefixParser> tokenPrefixParserHashMap;
  private HashMap<TokenHashmMapKey, InfixParser> tokenInfixParserHashMap;

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

    // Register the class which implements PrefixParser interface
    register(TokenFactory.create(Delimiter.LBRACE), new TableConstructorParser(7));
    register(TokenFactory.create(Delimiter.LBRACK), new BracketParser(7));
    register(TokenFactory.create(Delimiter.LPAREN), new ParenthesisParser(7));
    register(TokenFactory.create(Operator.NOT), (PrefixParser) new OperatorParser(7));
    register(TokenFactory.create(Operator.MINUS), (PrefixParser) new OperatorParser(7));
    register(TokenFactory.create(Keyword.FUNCTION), (PrefixParser) new FunctionExprParser());
    register(literalKey, new LiteralParser());
    register(identifierKey, new IdentifierParser());
  }

  private Token nextToken(int i) {
    return tokens.get(i + currentPos);
  }

  private Token nextToken() {
    return nextToken(1);
  }

  Token currentToken() {
    return nextToken(0);
  }

  void advanceTokens() {
    currentPos++;
  }

  void consume(Delimiter delimiter) throws IllegalParseException {
    if (!currentToken().isSubtype(delimiter)) {
      throw new IllegalParseException("Expecting " + delimiter + " but found " + nextToken());
    }
    advanceTokens();
  }

  void consume(Keyword keyword) throws IllegalParseException {
    if (!currentToken().isSubtype(keyword)) {
      throw new IllegalParseException("Expecting " + keyword + " but found " + nextToken());
    }
    advanceTokens();
  }

  void consume(Operator operator) throws IllegalParseException {
    if (!currentToken().isSubtype(operator)) {
      throw new IllegalParseException("Expecting " + operator + " but found " + nextToken());
    }
    advanceTokens();
  }

  protected StatementAssignment parseAssignment() throws IllegalParseException {
    return parseAssignment(-1);
  }

  protected StatementAssignment parseAssignment(int max) throws IllegalParseException {
    // At least one identifier
    ArrayList<ExpressionIdentifier> identifiers = parseCommaSeparatedExpressions(0, max);

    // Store the '=' position
    Token assignTok = currentToken();
    // Consume '='
    consume(Operator.ASSIGN);

    ArrayList<Expression> exprs = parseCommaSeparatedExpressions(0, max);
    return new StatementAssignment(assignTok, identifiers, exprs);
  }

  private Expression parseExpression() throws IllegalParseException {
    return parseExpression(0);
  }

  protected <T extends Expression> ArrayList<T> parseCommaSeparatedExpressions(int precedence)
      throws IllegalParseException {
    return parseCommaSeparatedExpressions(precedence, -1);
  }

  protected <T extends Expression> ArrayList<T> parseCommaSeparatedExpressions(
      int precedence, int max) throws IllegalParseException {
    ArrayList<T> exprs = new ArrayList<>();

    exprs.add((T) parseExpression());
    int count = 1;

    while (currentToken().isSubtype(Delimiter.COMMA) && (max <= 0 || count < max)) {
      // Consume ','
      consume(Delimiter.COMMA);
      exprs.add((T) parseExpression(precedence));
    }

    return exprs;
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
      InfixParser infix = getInfix(tok);
      lhs = infix.parseInfix(this, tok, lhs);
    }
    return lhs;
  }

  private int getCurrTokenPrecedence() {
    InfixParser parser = getInfix(currentToken());

    return parser != null ? parser.getPrecedence() : 0;
  }

  protected void registerBinaryOperator(Operator op, int precedence) {
    tokenInfixParserHashMap.put(new TokenHashmMapKey(TokenFactory.create(op)), new OperatorParser(precedence));
  }

  protected void register(Token type, PrefixParser parser) {
    tokenPrefixParserHashMap.put(new TokenHashmMapKey(type), parser);
  }

  protected void register(Token type, InfixParser parser) {
    tokenInfixParserHashMap.put(new TokenHashmMapKey(type), parser);
  }

  public ArrayList<Statement> parse() throws IllegalParseException {
    ArrayList<Statement> statements = new ArrayList<>();

    while (currentPos < tokens.size() && currentTokenIsValid()) {
      statements.add(parseStatement());
    }

    return statements;
  }

  private PrefixParser getPrefix(Token token) {
    return tokenPrefixParserHashMap.get(new TokenHashmMapKey(token));
  }


  private InfixParser getInfix(Token tok) {
    return tokenInfixParserHashMap.get(new TokenHashmMapKey(tok));
  }

  private PrefixParser getPrefixParser(Token token) {
    switch (token.getType()) {
      case IDENTIFIER:
        return getPrefix(identifierKey);
      case LITERAL:
        return getPrefix(literalKey);
      default:
        PrefixParser prefixParser = getPrefix(token);
        return prefixParser;
    }
  }

  protected StatementList parseListStatement() throws IllegalParseException {
    StatementList list = new StatementList();

    while (currentTokenIsValid() && !isBlockEnd()) {
      Statement child = parseStatement();
      list.addChild(child);
      if (child instanceof StatementReturn) {
        break;
      }
    }
    return list;
  }

  private boolean isBlockStatement() {
    Token tok = currentToken();
    return tok.getType() == TokenType.KEYWORD && ((TokenKeyword) tok).getKeyword() == Keyword.DO;
  }

  protected StatementList parseBlockStatement() throws IllegalParseException {

    // consume the DO keyword
    consume(Keyword.DO);

    StatementList list = parseListStatement();

    // consume the END keyword
    consume(Keyword.END);

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
    } else if (isIfStatement()) {
      return parseIfStatement();
    } else if (isBlockStatement()) {
      return parseBlockStatement();
    } else if (isWhileStatement()) {
      return parseWhileStatement();
    } else if (isForStatement()) {
      return parseForStatement();
    } else {
      return new StatementExpression(parseExpression());
    }
  }

  private StatementFunction parseFunctionStatement() throws IllegalParseException {
    // Next token should be an identifier
    Token tok = currentToken();
    advanceTokens();

    if (!(currentToken().getType() == TokenType.IDENTIFIER)) {
      throw new IllegalParseException(
              String.format("Expected identifier in function args but got: %s", currentToken())
      );
    }
    ExpressionIdentifier funcName = (ExpressionIdentifier) ExpressionFactory.create(currentToken());
    advanceTokens();

    // Parse args
    ArrayList<Expression> args = parseFuncArgs();
    StatementList stmts = parseListStatement();

    // consume END of function statement
    consume(Keyword.END);
    return new StatementFunction(tok, funcName, ExpressionFactory.createExpressionFunction(tok, args, stmts));
  }

  private boolean isFunctionStatement() {
    boolean isFunc =
        currentToken().isSubtype(Keyword.FUNCTION);
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
    return currentToken().isSubtype(Keyword.RETURN);
  }

  protected boolean isAssignmentStatement() {
    int pos = 0;

    do {
      TokenType currType = nextToken(pos).getType();
      boolean isIdent = currType == TokenType.IDENTIFIER;

      if (!isIdent) {
        return false;
      }

      // Simple assignment
      if (nextToken(pos + 1).isSubtype(Operator.ASSIGN)) {
        return true;
      }

      // Else check for comma else return
      if (!nextToken(pos + 1).isSubtype(Delimiter.COMMA)) {
        return false;
      }

      pos += 2;
    } while (!nextToken(pos).isSubtype(Operator.ASSIGN));

    return true;
  }

  private boolean isIfStatement() {
    return currentToken().isSubtype(Keyword.IF);
  }

  private StatementIf parseIfStatement() throws IllegalParseException {
    return parseIfStatement(false);
  }

  // nested should only be true when parsing an elseif statement inside a main if
  // this argument tells parseIfStatement not to consume the only END keyword
  private StatementIf parseIfStatement(boolean nested) throws IllegalParseException {

    advanceTokens();
    Expression condition = parseExpression();

    Token tok = currentToken();
    if (!tok.isSubtype(Keyword.THEN)) {
      throw new IllegalParseException(String.format("unexpected token %s, then expected", tok));
    }
    advanceTokens();
    Statement consequence = parseListStatement();
    tok = currentToken();
    if (!isBlockEnd()) {
      throw new IllegalParseException(
          String.format("unexpected token %s, expected end, else or elseif", tok));
    }
    Statement alternative = null;
    TokenKeyword keyword = (TokenKeyword) tok;
    switch (keyword.getKeyword()) {
      case ELSEIF:
        alternative = parseIfStatement(true);
        break;
      case ELSE:
        advanceTokens();
        alternative = parseListStatement();
        break;
    }

    tok = currentToken();
    if (!tok.isSubtype(Keyword.END)) {
      throw new IllegalParseException(String.format("unexpected token %s, expected end", keyword));
    }

    if (!nested) {
      // consume the END keyword
      consume(Keyword.END);
    }

    return new StatementIf(tok, condition, consequence, alternative);
  }

  private boolean isWhileStatement() {
    return currentToken().isSubtype(Keyword.WHILE);
  }

  private Statement parseWhileStatement() throws IllegalParseException {
    Token tok = currentToken();
    advanceTokens();
    Expression condition = parseExpression();
    if (!isBlockStatement()) {
      throw new IllegalParseException(
          String.format("expected do ... end statement after while %s", tok));
    }

    Statement consequence = parseBlockStatement();

    return new StatementWhile(tok, condition, consequence);
  }

  private boolean isForStatement() {
    Token tok = currentToken();
    return tok.getType() == TokenType.KEYWORD && ((TokenKeyword) tok).getKeyword() == Keyword.FOR;
  }

  private StatementFor parseForStatement() throws IllegalParseException {
    Token next = nextToken(3);
    if (next.isSubtype(Operator.ASSIGN)) {
      return parseNumericForStatement();
    }

    return parseGenericForStatement();
  }

  private StatementFor parseNumericForStatement() throws IllegalParseException {
    Token tok = currentToken();
    advanceTokens();

    if (!isAssignmentStatement()) {
      throw new IllegalParseException(String.format("expected assignment in for loop"));
    }
    StatementAssignment assignment = parseAssignment();
    ExpressionIdentifier variable = assignment.getLhs().get(0);
    Expression var = assignment.getRhs().get(0);
    Expression limit = null;
    Expression step = null;

    if (assignment.getRhs().size() > 1) {
      limit = assignment.getRhs().get(1);
    } else {
      throw new IllegalParseException("expected limit in for loop");
    }

    if (assignment.getRhs().size() > 2) {
      step = assignment.getRhs().get(2);
    }

    Statement block = parseBlockStatement();

    return new StatementNumericFor(tok, variable, block, var, limit, step);
  }

  private StatementFor parseGenericForStatement() throws IllegalParseException {
    ArrayList<ExpressionIdentifier> variables = parseCommaSeparatedExpressions(0);

    Token tok = currentToken();

    // Check if we are on equal token
    if (!tok.isSubtype(Keyword.IN)) {
      throw new IllegalParseException(String.format("Expected in keyword but got %s", tok));
    }

    ArrayList<Expression> explist = parseCommaSeparatedExpressions(0);
    Expression iterator = explist.get(0);
    Expression state = null;
    Expression var = null;
    if (explist.size() > 1) {
      state = explist.get(1);
    }
    if (explist.size() > 2) {
      var = explist.get(2);
    }

    Statement block = parseBlockStatement();

    return new StatementGenericFor(tok, variables, block, iterator, state, var);
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




  protected ArrayList<Expression> parseFuncArgs() throws IllegalParseException {
    advanceTokens();
    ArrayList<Expression> args = new ArrayList<>();
    // if there is no args, we look for a ')'

    Token tok = currentToken();
    while (!tok.isSubtype(Delimiter.RPAREN)) {
      // TODO: what if we have f(3+4, 5) ? I'm not sure we're looking for an identifier. Make a test... (19)
      if (tok.getType() != TokenType.IDENTIFIER) {
        throw new IllegalParseException(
            String.format("Expected identifier in function args but got: %s", tok));
      }
      args.add(ExpressionFactory.create(tok));
      advanceTokens();
      tok = currentToken();
    }

    // Consume ')'
    consume(Delimiter.RPAREN);

    return args;
  }
}

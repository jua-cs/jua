package jua.parser;

import java.util.ArrayList;
import java.util.HashMap;
import jua.ast.*;
import jua.token.*;
import util.BufferedChannel;

public class Parser {
  private static final Token identifierKey = TokenFactory.create("", 0, 0);
  private static final Token literalKey = TokenFactory.create(Literal.BOOLEAN, "0", 0, 0);
  private BufferedChannel<Token> tokens;
  private HashMap<TokenHashmMapKey, PrefixParser> tokenPrefixParserHashMap;
  private HashMap<TokenHashmMapKey, InfixParser> tokenInfixParserHashMap;

  public Parser(BufferedChannel<Token> tokens) {
    init(tokens);
  }

  public Parser(ArrayList<Token> tokenList) {
    BufferedChannel<Token> tokens = new BufferedChannel<Token>();
    tokenList.forEach(
        tok -> {
          try {
            tokens.add(tok);
          } catch (InterruptedException e) {
            // TODO: handle this
            e.printStackTrace();
          }
        });

    init(tokens);
  }

  private void init(BufferedChannel<Token> tokens) {
    this.tokens = tokens;
    registerParsers();
  }

  private void registerParsers() {
    this.tokenInfixParserHashMap = new HashMap<>();
    this.tokenPrefixParserHashMap = new HashMap<>();

    // Register the class which implements InfixParser interface
    // FIXME: ^ has greater precedence than unary operators, this is not handled at the moment
    registerBinaryOperator(Operator.PLUS, 4);
    registerBinaryOperator(Operator.ASTERISK, 6);
    registerBinaryOperator(Operator.SLASH, 6);
    registerBinaryOperator(Operator.MINUS, 4);
    registerBinaryOperator(Operator.CARAT, 7);
    registerBinaryOperator(Operator.PERCENT, 6);
    registerBinaryOperator(Operator.CONCAT, 5);

    registerBinaryOperator(Operator.AND, 2);
    registerBinaryOperator(Operator.OR, 1);

    registerBinaryOperator(Operator.EQUALS, 3);
    registerBinaryOperator(Operator.NOT_EQUAL, 3);
    registerBinaryOperator(Operator.GT, 3);
    registerBinaryOperator(Operator.GTE, 3);
    registerBinaryOperator(Operator.LT, 3);
    registerBinaryOperator(Operator.LTE, 3);
    registerBinaryOperator(Operator.DOT, 10);

    register(TokenFactory.create(Delimiter.LPAREN), new FunctionCallParser(9));

    // Register the class which implements PrefixParser interface
    register(TokenFactory.create(Delimiter.LBRACE), new TableConstructorParser());
    register(TokenFactory.create(Delimiter.LBRACK), new BracketParser(8));
    register(TokenFactory.create(Delimiter.LPAREN), new ParenthesisParser(8));
    register(TokenFactory.create(Operator.NOT), (PrefixParser) new OperatorParser(8));
    register(TokenFactory.create(Operator.MINUS), (PrefixParser) new OperatorParser(8));
    register(TokenFactory.create(Keyword.FUNCTION), (PrefixParser) new FunctionExprParser());
    register(literalKey, new LiteralParser());
    register(identifierKey, new IdentifierParser());
  }

  public Token nextToken(int i) {
    try {
      return tokens.peek(i);
    } catch (InterruptedException e) {
      // TODO: handle this
      e.printStackTrace();
    }
    return null;
  }

  private Token nextToken() {
    return nextToken(1);
  }

  Token currentToken() {
    return nextToken(0);
  }

  void advanceTokens() {
    try {
      tokens.skip();
    } catch (InterruptedException e) {
      // TODO: handle this
      e.printStackTrace();
    }
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

  private StatementAssignment parseAssignment() throws IllegalParseException {
    return parseAssignment(-1);
  }

  protected StatementAssignment parseAssignment(int max) throws IllegalParseException {
    boolean isLocal = false;
    if (currentToken().isSubtype(Keyword.LOCAL)) {
      isLocal = true;
      advanceTokens();
    }
    // At least one identifier
    ArrayList<ExpressionIdentifier> identifiers = parseCommaSeparatedExpressions(0, max);

    // Store the '=' position
    Token assignTok = currentToken();
    // Consume '='
    consume(Operator.ASSIGN);

    ArrayList<Expression> exprs = parseCommaSeparatedExpressions(0, max);
    return new StatementAssignment(assignTok, identifiers, exprs, isLocal);
  }

  private Expression parseExpression() throws IllegalParseException {
    return parseExpression(0);
  }

  protected <T extends Expression> ArrayList<T> parseCommaSeparatedExpressions(int precedence)
      throws IllegalParseException {
    return parseCommaSeparatedExpressions(precedence, -1);
  }

  @SuppressWarnings(value = "unchecked")
  private <T extends Expression> ArrayList<T> parseCommaSeparatedExpressions(
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
      throw new IllegalParseException(String.format("Unexpected jua.token: %s", tok));
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
    // TODO: Hacky, fixme fast
    Token tok = tokens.peek();
    if (tok == null) {
      return 0;
    }

    InfixParser parser = getInfix(tok);

    return parser != null ? parser.getPrecedence() : 0;
  }

  private void registerBinaryOperator(Operator op, int precedence) {
    tokenInfixParserHashMap.put(
        new TokenHashmMapKey(TokenFactory.create(op)), new OperatorParser(precedence));
  }

  private void register(Token type, PrefixParser parser) {
    tokenPrefixParserHashMap.put(new TokenHashmMapKey(type), parser);
  }

  private void register(Token type, InfixParser parser) {
    tokenInfixParserHashMap.put(new TokenHashmMapKey(type), parser);
  }

  public StatementList parse() throws IllegalParseException {
    StatementList statements = new StatementList(currentToken());

    while (currentTokenIsValid()) {
      statements.addChild(parseStatement());
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
    if (token.isLiteral()) {
      return getPrefix(literalKey);
    } else if (token.isIdentifier()) {
      return getPrefix(identifierKey);
    } else {
      return getPrefix(token);
    }
  }

  protected StatementList parseListStatement() throws IllegalParseException {
    StatementList list = new StatementList(currentToken());

    while (currentToken().isValid() && !isBlockEnd()) {
      Statement child = parseStatement();
      list.addChild(child);
      if (child instanceof StatementReturn) {
        break;
      }
    }
    return list;
  }

  private boolean isBlockStatement() {
    return currentToken().isSubtype(Keyword.DO);
  }

  private StatementList parseBlockStatement() throws IllegalParseException {

    // consume the DO keyword
    consume(Keyword.DO);

    StatementList list = parseListStatement();

    // consume the END keyword
    consume(Keyword.END);

    return list;
  }

  public Statement parseStatement() throws IllegalParseException {
    if (isAssignmentStatement()) {
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
    } else if (isBreakStatement()) {
      return parseBreakStatement();
    } else {
      return new StatementExpression(parseExpression());
    }
  }

  private StatementFunction parseFunctionStatement() throws IllegalParseException {
    // Next jua.token should be an identifier
    Token tok = currentToken();
    advanceTokens();

    if (!currentToken().isIdentifier()) {
      throw new IllegalParseException(
          String.format("Expected identifier in function args but got: %s", currentToken()));
    }
    ExpressionIdentifier funcName = (ExpressionIdentifier) ExpressionFactory.create(currentToken());
    advanceTokens();

    // Parse args
    ArrayList<ExpressionIdentifier> args = parseFuncArgs();
    StatementList stmts = parseListStatement();

    // consume END of function statement
    consume(Keyword.END);
    return new StatementFunction(
        tok, funcName, ExpressionFactory.createExpressionFunction(tok, args, stmts));
  }

  private boolean isFunctionStatement() {
    boolean isFunc = currentToken().isSubtype(Keyword.FUNCTION);
    boolean nextIsIdent = nextToken().isIdentifier();
    return isFunc && nextIsIdent;
  }

  private StatementReturn parseReturnStatement() throws IllegalParseException {
    Token tok = currentToken();
    advanceTokens();
    StatementReturn stmt = new StatementReturn(tok, parseCommaSeparatedExpressions(0));
    return stmt;
  }

  private boolean isReturnStatement() {
    return currentToken().isSubtype(Keyword.RETURN);
  }

  protected boolean isAssignmentStatement() {
    int pos = 0;

    if (currentToken().isSubtype(Keyword.LOCAL)) {
      return true;
    }

    do {
      boolean isIdent = nextToken(pos).isIdentifier();

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

    //  TODO: use consume(Keyword.IF);
    advanceTokens();
    Expression condition = parseExpression();

    consume(Keyword.THEN);
    Statement consequence = parseListStatement();
    Token tok = currentToken();
    if (!isBlockEnd()) {
      throw new IllegalParseException(
          String.format("unexpected jua.token %s, expected end, else or elseif", tok));
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
      throw new IllegalParseException(
          String.format("unexpected jua.token %s, expected end", keyword));
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
    return currentToken().isSubtype(Keyword.FOR);
  }

  private StatementFor parseForStatement() throws IllegalParseException {
    // consume FOR keyword
    Token tok = currentToken();
    advanceTokens();

    if (isAssignmentStatement()) {
      return parseNumericForStatement(tok);
    }

    return parseGenericForStatement(tok);
  }

  private StatementFor parseNumericForStatement(Token tok) throws IllegalParseException {
    if (!isAssignmentStatement()) {
      throw new IllegalParseException("expected assignment in for loop");
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

    return new StatementNumericFor(tok, variable, var, limit, step, block);
  }

  private StatementFor parseGenericForStatement(Token tok) throws IllegalParseException {
    ArrayList<ExpressionIdentifier> variables = parseCommaSeparatedExpressions(0);

    // Check if we are on equal jua.token
    if (!currentToken().isSubtype(Keyword.IN)) {
      throw new IllegalParseException(String.format("Expected in keyword but got %s", tok));
    }
    advanceTokens();

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

    return new StatementGenericFor(tok, variables, iterator, state, var, block);
  }

  private boolean isBreakStatement() {
    return currentToken().isSubtype(Keyword.BREAK);
  }

  private StatementBreak parseBreakStatement() {
    // consume BREAK keyword
    Token tok = currentToken();
    advanceTokens();

    return new StatementBreak(tok);
  }

  private boolean currentTokenIsValid() {
    return !(currentToken() instanceof TokenEOF || currentToken() instanceof TokenInvalid);
  }

  private boolean isBlockEnd() {
    Token tok = currentToken();
    return tok.isSubtype(Keyword.END)
        || tok.isSubtype(Keyword.ELSE)
        || tok.isSubtype(Keyword.ELSEIF);
  }

  protected ArrayList<ExpressionIdentifier> parseFuncArgs() throws IllegalParseException {
    // consume the left parenthesis
    advanceTokens();
    ArrayList<ExpressionIdentifier> args = new ArrayList<>();
    // if there is no args, we look for a ')'
    if (currentToken().isSubtype(Delimiter.RPAREN)) {
      consume(Delimiter.RPAREN);
      return args;
    }
    args = parseCommaSeparatedExpressions(0);

    // Consume ')'
    consume(Delimiter.RPAREN);

    return args;
  }

  // Used to access the HasmMap with Token, with still a functioning equals for Lexer
  private static class TokenHashmMapKey {
    private final Token token;

    public TokenHashmMapKey(Token token) {
      this.token = token;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      TokenHashmMapKey that = (TokenHashmMapKey) o;

      return (token != null)
          && token.getClass() == that.token.getClass()
          && token.hashCode() == that.token.hashCode();
    }

    @Override
    public int hashCode() {
      return token != null ? token.hashCode() : 0;
    }
  }
}

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
  private BufferedChannel<Statement> out = new BufferedChannel<>();

  private HashMap<TokenHashMapKey, PrefixParser> tokenPrefixParserHashMap;
  private HashMap<TokenHashMapKey, InfixParser> tokenInfixParserHashMap;
  private ArrayList<StatementParser> statementParserList;

  public Parser(BufferedChannel<Token> tokens) {
    init(tokens);
  }

  public Parser(ArrayList<Token> tokenList) {
    BufferedChannel<Token> tokens = new BufferedChannel<>();
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

  // *******************************************************************
  // Register all the smaller parser which handle each small cases
  // *******************************************************************

  private void registerParsers() {
    this.tokenInfixParserHashMap = new HashMap<>();
    this.tokenPrefixParserHashMap = new HashMap<>();
    this.statementParserList = new ArrayList<>();

    // Register the class which implements InfixParser interface
    // TODO: ^ has greater precedence than unary operators, this is not handled at the moment
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
    register(TokenFactory.create(Operator.COLON), new MethodCallParser(9));

    // Register the classes which implements PrefixParser interface
    register(TokenFactory.create(Delimiter.LBRACE), new TableConstructorParser());
    register(TokenFactory.create(Delimiter.LBRACK), new BracketParser(8));
    register(TokenFactory.create(Delimiter.LPAREN), new ParenthesisParser(8));
    register(TokenFactory.create(Operator.NOT), (PrefixParser) new OperatorParser(8));
    register(TokenFactory.create(Operator.MINUS), (PrefixParser) new OperatorParser(8));
    register(TokenFactory.create(Operator.HASH), (PrefixParser) new OperatorParser(8));
    register(TokenFactory.create(Keyword.FUNCTION), new FunctionExprParser());
    register(literalKey, new LiteralParser());
    register(identifierKey, new IdentifierParser());

    // Register the classes which implements StatementParser interface
    register(new FunctionStatementParser());
    register(new ReturnStatementParser());
    register(new IfStatementParser());
    register(new BlockStatementParser());
    register(new WhileStatementParser());
    register(new BreakStatementParser());
    register(new RepeatStatementParser());
    register(new ForStatementParser());
    register(new LocalAssignementStatementParser());
  }

  private void registerBinaryOperator(Operator op, int precedence) {
    tokenInfixParserHashMap.put(
        new TokenHashMapKey(TokenFactory.create(op)), new OperatorParser(precedence));
  }

  private void register(Token type, PrefixParser parser) {
    tokenPrefixParserHashMap.put(new TokenHashMapKey(type), parser);
  }

  private void register(Token type, InfixParser parser) {
    tokenInfixParserHashMap.put(new TokenHashMapKey(type), parser);
  }

  private void register(StatementParser statementParser) {
    statementParserList.add(statementParser);
  }

  private PrefixParser getPrefix(Token token) {
    return tokenPrefixParserHashMap.get(new TokenHashMapKey(token));
  }

  private InfixParser getInfix(Token tok) {
    return tokenInfixParserHashMap.get(new TokenHashMapKey(tok));
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

  // do not use currentToken() not to consume the new line
  private int getCurrTokenPrecedence() {
    Token tok = currentTokenNoSkip();
    if (tok == null) {
      return 0;
    }

    InfixParser parser = getInfix(tok);

    return parser != null ? parser.getPrecedence() : 0;
  }

  // Used to access the HashMap with Token, with still a functioning equals for Lexer
  private static class TokenHashMapKey {
    private final Token token;

    public TokenHashMapKey(Token token) {
      this.token = token;
    }
    // Used to set a "light" equals between tokens, ignoring position and line.
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      TokenHashMapKey that = (TokenHashMapKey) o;

      return (token != null)
          && token.getClass() == that.token.getClass()
          && token.hashCode() == that.token.hashCode();
    }

    @Override
    public int hashCode() {
      return token != null ? token.hashCode() : 0;
    }
  }

  // *******************************************************************
  //  Method to navigate through the stream of Token from the Lexer
  // *******************************************************************

  Token currentToken() {
    return nextToken(0, true);
  }

  Token currentTokenNoSkip() {
    return nextToken(0, false);
  }

  public Token nextToken(int i, boolean skipNewline) {
    try {
      Token currentTok = tokens.peek();
      while (skipNewline && currentTok != null && currentTok.isSubtype(Delimiter.NEWLINE)) {
        advanceTokens();
        currentTok = tokens.peek();
      }
      return tokens.peek(i);
    } catch (InterruptedException e) {
      // TODO: handle this
      e.printStackTrace();
    }
    return null;
  }

  Token nextToken(int i) {
    return nextToken(i, true);
  }

  Token nextToken() {
    return nextToken(1, true);
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
      throw new IllegalParseException("Expecting " + delimiter + " but found " + currentToken());
    }
    advanceTokens();
  }

  void consume(Keyword keyword) throws IllegalParseException {
    if (!currentToken().isSubtype(keyword)) {
      throw new IllegalParseException("Expecting " + keyword + " but found " + currentToken());
    }
    advanceTokens();
  }

  void consume(Operator operator) throws IllegalParseException {
    if (!currentToken().isSubtype(operator)) {
      throw new IllegalParseException("Expecting " + operator + " but found " + currentToken());
    }
    advanceTokens();
  }

  TokenIdentifier consumeIdentifier() throws IllegalParseException {
    Token tok = currentToken();
    if (!(tok.isIdentifier())) {
      throw new IllegalParseException("Expecting an identifier but found a" + nextToken());
    }
    advanceTokens();
    return (TokenIdentifier) tok;
  }

  // *******************************************************************
  //  Main parsing functions
  // *******************************************************************

  Expression parseExpression() throws IllegalParseException {
    return parseExpression(0);
  }

  protected Expression parseExpression(int precedence) throws IllegalParseException {
    Token tok = currentToken();
    // can't use consume here
    advanceTokens();

    PrefixParser prefix = getPrefixParser(tok);
    if (prefix == null) {
      throw new IllegalParseException(String.format("Unexpected jua.token: %s", tok));
    }

    Expression lhs = prefix.parsePrefix(this, tok);
    while (precedence < getCurrTokenPrecedence()) {
      tok = currentToken();
      // can't use consume here
      advanceTokens();

      // We are 100 % sure that infix is not null here because getCurrTokenPrecedence
      // returns 0 otherwise and precedence has to be positive
      InfixParser infix = getInfix(tok);
      lhs = infix.parseInfix(this, tok, lhs);
    }
    return lhs;
  }

  public Statement parseStatement() throws IllegalParseException {

    for (StatementParser p : statementParserList) {
      if (p.matches(this)) {
        return p.parse(this);
      }
    }

    ArrayList<Expression> exprs = parseCommaSeparatedExpressions(0);

    // Check if we are on an assignment
    if (currentTokenNoSkip().isSubtype(Operator.ASSIGN)) {
      // Cast into variables
      ArrayList<Variable> vars = new ArrayList<>();
      exprs.forEach(expr -> vars.add((Variable) expr));

      TokenOperator assignTok = (TokenOperator) currentToken();
      consume(Operator.ASSIGN);

      return new StatementAssignment(assignTok, vars, parseCommaSeparatedExpressions(0), false);
    }

    // Otherwise return a statement expression
    return new StatementExpression(exprs);
  }

  protected <T> ArrayList<T> parseCommaSeparatedExpressions(int precedence)
      throws IllegalParseException {
    return parseCommaSeparatedExpressions(precedence, -1);
  }

  // TODO; remove this horror
  @SuppressWarnings(value = "unchecked")
  <T> ArrayList<T> parseCommaSeparatedExpressions(int precedence, int max)
      throws IllegalParseException {
    ArrayList<T> exprs = new ArrayList<>();

    exprs.add((T) parseExpression());
    int count = 1;

    while (currentTokenNoSkip().isSubtype(Delimiter.COMMA) && (max <= 0 || count < max)) {
      // Consume ','
      consume(Delimiter.COMMA);
      exprs.add((T) parseExpression(precedence));
    }

    return exprs;
  }

  protected StatementList parseListStatement() throws IllegalParseException {
    StatementList list = new StatementList(currentToken());

    while (currentToken().isValid()
        && !currentToken().isBlockEnd()
        && !currentToken().isSubtype(Keyword.UNTIL)) {
      Statement child = parseStatement();
      list.addChild(child);
      if (child instanceof StatementReturn) {
        break;
      }
    }
    return list;
  }

  // *******************************************************************
  //  Interfaces to use the Parser
  // *******************************************************************

  public void start(boolean isInteractive) throws InterruptedException, IllegalParseException {
    while (currentToken().isValid()) {
      try {
        Statement statement = parseStatement();
        out.add(statement);
      } catch (IllegalParseException e) {
        if (!isInteractive) {
          out.add(new StatementEOP());
        } else {
          // send a nil to reset the repl
          out.add(
              new StatementExpression(
                  ExpressionFactory.create(TokenFactory.create(Literal.NIL, "nil"))));
        }
        throw e;
      }
    }
    out.add(new StatementEOP());
  }

  public StatementList parse() throws IllegalParseException {
    StatementList statements = new StatementList(currentToken());

    while (currentToken().isValid()) {
      statements.addChild(parseStatement());
    }

    return statements;
  }

  public BufferedChannel<Statement> getOut() {
    return out;
  }
}

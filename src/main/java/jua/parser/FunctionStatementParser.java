package jua.parser;

import java.util.ArrayList;
import jua.ast.*;
import jua.token.Keyword;
import jua.token.Operator;
import jua.token.Token;

public class FunctionStatementParser implements StatementParser {

  @Override
  public Statement parse(Parser parser) throws IllegalParseException {
    // Next jua.token should be an identifier
    Token tok = parser.currentToken();
    parser.consume(Keyword.FUNCTION);

    // Parse any expression and cast it into a Variable to allow for:
    // function f() -> Identifier
    // function a.b.c() -> ExpressionAccess
    // function d[e].f[0]() -> ExpressionIndex

    // We don't want to parse the expression call so we set the precedence to just below it
    // it also matches the precedence of the index and access operators
    Variable funcVar = (Variable) parser.parseExpression(9);

    // function x.y:z()
    // Check if the statement is not a method assignment (like function x.y:z())
    String methodName = parseMethodName(parser);

    // Parse args
    ArrayList<ExpressionIdentifier> args = FunctionExprParser.parseFuncArgs(parser);
    StatementList stmts = parser.parseListStatement();

    // consume END of function statement
    parser.consume(Keyword.END);

    ExpressionFunction funcExpr = ExpressionFactory.createExpressionFunction(tok, args, stmts);

    if (methodName != null) {
      return new StatementMethod(tok, funcVar, methodName, funcExpr);
    }

    return new StatementFunction(tok, funcVar, funcExpr);
  }

  // parseMethodName should be called before parsing args to check if the current statement is a
  // method or a regular function
  private String parseMethodName(Parser parser) {
    if (!parser.currentToken().isSubtype(Operator.COLON)) {
      // We are not in a method
      return null;
    }

    parser.advanceTokens();
    Token tok = parser.currentToken();
    parser.advanceTokens();
    return tok.getLiteral();
  }

  @Override
  public boolean matches(Parser parser) {
    boolean isFunc = parser.currentToken().isSubtype(Keyword.FUNCTION);
    boolean nextIsIdentifier = parser.nextToken().isIdentifier();
    return isFunc && nextIsIdentifier;
  }
}

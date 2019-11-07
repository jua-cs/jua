package jua.parser;

import java.util.ArrayList;
import jua.ast.*;
import jua.token.Keyword;
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
    // it also matches the precedence of the index and access operators (ie same as
    // FunctionCallParser in Parser)
    Variable funcVar = (Variable) parser.parseExpression(14);

    // TODO: support :
    // function x.y:z()

    // Parse args
    ArrayList<ExpressionIdentifier> args = FunctionExprParser.parseFuncArgs(parser);
    StatementList stmts = parser.parseListStatement();

    // consume END of function statement
    parser.consume(Keyword.END);
    return new StatementFunction(
        tok, funcVar, ExpressionFactory.createExpressionFunction(tok, args, stmts));
  }

  @Override
  public boolean matches(Parser parser) {
    boolean isFunc = parser.currentToken().isSubtype(Keyword.FUNCTION);
    boolean nextIsIdentifier = parser.nextToken().isIdentifier();
    return isFunc && nextIsIdentifier;
  }
}

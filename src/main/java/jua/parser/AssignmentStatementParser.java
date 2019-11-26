package jua.parser;

import java.util.ArrayList;
import jua.ast.*;
import jua.token.Delimiter;
import jua.token.Keyword;
import jua.token.Operator;
import jua.token.Token;

public class AssignmentStatementParser implements StatementParser {

  public static StatementAssignment parseAssignment(Parser parser, int max)
      throws IllegalParseException {
    AssignmentStatementParser assignmentStatementParser = new AssignmentStatementParser();
    return (StatementAssignment) assignmentStatementParser.parse(parser, max);
  }

  public static StatementAssignment parseAssignment(Parser parser) throws IllegalParseException {
    AssignmentStatementParser assignmentStatementParser = new AssignmentStatementParser();
    return (StatementAssignment) assignmentStatementParser.parse(parser);
  }

  public static boolean isAssignmentStatement(Parser parser) {
    AssignmentStatementParser assignmentStatementParser = new AssignmentStatementParser();
    return assignmentStatementParser.matches(parser);
  }

  @Override
  public Statement parse(Parser parser) throws IllegalParseException {
    return parse(parser, -1);
  }

  public Statement parse(Parser parser, int max) throws IllegalParseException {
    boolean isLocal = false;
    if (parser.currentToken().isSubtype(Keyword.LOCAL)) {
      isLocal = true;
      parser.consume(Keyword.LOCAL);
    }
    // At least one identifier
    ArrayList<ExpressionIdentifier> identifiers = parser.parseCommaSeparatedExpressions(0, max);
    ArrayList<Variable> variables =
        util.Util.createArrayList(identifiers.toArray(new Variable[identifiers.size()]));

    // Store the '=' position
    Token assignTok = parser.currentToken();
    // Consume '='
    parser.consume(Operator.ASSIGN);

    ArrayList<Expression> exprs = parser.parseCommaSeparatedExpressions(0, max);
    return new StatementAssignment(assignTok, variables, exprs, isLocal);
  }

  @Override
  public boolean matches(Parser parser) {
    int pos = 0;

    if (parser.currentToken().isSubtype(Keyword.LOCAL)) {
      // TODO local function f()... is valid so we should also check one character ahead
      return true;
    }

    do {
      boolean isIdent = parser.nextToken(pos).isIdentifier();

      if (!isIdent) {
        return false;
      }

      // Simple assignment
      if (parser.nextToken(pos + 1).isSubtype(Operator.ASSIGN)) {
        return true;
      }

      // Else check for comma else return
      if (!parser.nextToken(pos + 1).isSubtype(Delimiter.COMMA)) {
        return false;
      }

      pos += 2;
    } while (!parser.nextToken(pos).isSubtype(Operator.ASSIGN));

    return true;
  }
}

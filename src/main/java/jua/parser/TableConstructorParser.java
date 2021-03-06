package jua.parser;

import java.util.ArrayList;
import jua.ast.*;
import jua.token.*;
import util.Tuple;

public class TableConstructorParser implements PrefixParser {
  @Override
  public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
    int keyCounting = 1;

    Token token = parser.currentToken();
    ArrayList<Tuple<Expression, Expression>> tuples = new ArrayList<>();
    while (!parser.currentToken().isSubtype(Delimiter.RBRACE)) {
      // Three cases:

      // [exp] = exp -> use the value of [exp] as the key
      if (token.isSubtype(Delimiter.LBRACK)) {
        parser.advanceTokens();
        Expression key = parser.parseExpression(0);

        // Consume ] =
        parser.consume(Delimiter.RBRACK);
        parser.consume(Operator.ASSIGN);

        Tuple<Expression, Expression> tup = new Tuple<>(key, parser.parseExpression(0));
        tuples.add(tup);
      } else if (AssignmentStatementParser.isAssignmentStatement(parser)) {
        // name = exp -> use the name as the key
        StatementAssignment assign = AssignmentStatementParser.parseAssignment(parser, 1);
        Tuple<Expression, Expression> tup =
            new Tuple<>(
                // Convert the LHS to a string
                ExpressionFactory.create(
                    TokenFactory.create(
                        Literal.STRING,
                        ((ExpressionIdentifier) assign.getLhs().get(0)).getLiteral())),
                assign.getRhs().get(0));
        tuples.add(tup);
      } else {
        // exp -> use the current keyCounting as the key
        Tuple<Expression, Expression> tup =
            new Tuple<>(
                ExpressionFactory.create(
                    // TODO: fixme ugly int to string here
                    TokenFactory.create(Literal.NUMBER, String.valueOf(keyCounting))),
                parser.parseExpression(0));

        tuples.add(tup);
        keyCounting++;
      }

      if (parser.currentToken().isSubtype(Delimiter.RBRACE)) {
        break;
      }

      parser.advanceTokens();
      token = parser.currentToken();
    }

    // Consume the '}'
    parser.consume(Delimiter.RBRACE);

    return new ExpressionTableConstructor(tok, tuples);
  }
}

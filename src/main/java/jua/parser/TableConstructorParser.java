package jua.parser;

import java.util.ArrayList;
import jua.ast.Expression;
import jua.ast.ExpressionFactory;
import jua.ast.ExpressionTableConstructor;
import jua.ast.StatementAssignment;
import jua.token.*;
import util.Tuple;

public class TableConstructorParser implements PrefixParser {
  @Override
  public Expression parsePrefix(Parser parser, Token tok) throws IllegalParseException {
    int keyCounting = 1;

    Token token = parser.currentToken();
    ArrayList<Tuple<Expression, Expression>> tuples = new ArrayList<>();
    while (true) {
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
      } else if (parser.isAssignmentStatement()) {
        // name = exp -> use the name as the key
        StatementAssignment assign = parser.parseAssignment(1);
        Tuple<Expression, Expression> tup =
            new Tuple<>(
                // Convert the LHS to a string
                ExpressionFactory.create(
                    TokenFactory.create(Literal.STRING, assign.getLhs().get(0).getLiteral())),
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

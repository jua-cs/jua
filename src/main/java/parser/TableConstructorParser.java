package parser;

import ast.Expression;
import ast.ExpressionFactory;
import ast.ExpressionTableConstructor;
import ast.StatementAssignment;
import java.util.ArrayList;
import token.Delimiter;
import token.Literal;
import token.Token;
import token.TokenFactory;
import util.Tuple;

public class TableConstructorParser implements PrefixParser {
  private final int precedence;

  public TableConstructorParser(int precedence) {
    this.precedence = precedence;
  }

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

        // Consume the ']'
        if (!parser.currentToken().isSubtype(Delimiter.RBRACK)) {
          throw new IllegalParseException(
              String.format("Expected ']' but got: %s", parser.currentToken()));
        }
        parser.advanceTokens();

        // TODO ensure '='
        parser.advanceTokens();

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
    parser.advanceTokens();

    return new ExpressionTableConstructor(tok, tuples);
  }
}

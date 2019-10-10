package jua.parser;

import jua.ast.Expression;
import jua.ast.ExpressionFactory;
import jua.ast.ExpressionFunctionCall;
import jua.token.*;

public class MethodCallParser implements InfixParser {
  private final FunctionCallParser callParser;

  MethodCallParser(int precedence) {
    this.callParser = new FunctionCallParser(precedence);
  }

  @Override
  public ExpressionFunctionCall parseInfix(Parser parser, Token tok, Expression lhs)
      throws IllegalParseException {
    TokenIdentifier ident = parser.consumeIdentifier();
    parser.consume(Delimiter.LPAREN);
    Expression index =
        ExpressionFactory.create(
            TokenFactory.create(Operator.DOT, tok.getLine(), tok.getPosition()),
            lhs,
            ExpressionFactory.create(ident));

    // TODO avoid evaluating lhs twice (in args and in index)
    // we could have an ExpressionMethodCall for that
    ExpressionFunctionCall expr = callParser.parseInfix(parser, tok, index);
    expr.addArg(0, lhs);

    return expr;
  }

  @Override
  public int getPrecedence() {
    return callParser.getPrecedence();
  }
}

package jua.parser;

import jua.ast.Expression;
import jua.ast.ExpressionFactory;
import jua.token.Token;

public class MethodCallParser implements InfixParser {
  private final FunctionCallParser callParser;

  public MethodCallParser(int precedence) {
    this.callParser = new FunctionCallParser(precedence);
  }

  @Override
  public Expression parseInfix(Parser parser, Token tok, Expression lhs)
      throws IllegalParseException {
    return ExpressionFactory.create(callParser.parseInfix(parser, tok, lhs));
  }

  @Override
  public int getPrecedence() {
    return callParser.getPrecedence();
  }
}

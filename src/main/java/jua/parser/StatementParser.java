package jua.parser;

import jua.ast.Statement;

public interface StatementParser {

  Statement parse(Parser parser) throws IllegalParseException;

  boolean matches(Parser parser);
}

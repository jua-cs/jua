package jua.parser;

public class IllegalParseException extends Exception {
  public IllegalParseException() {}

  public IllegalParseException(String message) {
    super(message);
  }

  public IllegalParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalParseException(Throwable cause) {
    super(cause);
  }

  public IllegalParseException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

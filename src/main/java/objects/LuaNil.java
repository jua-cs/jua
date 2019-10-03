package objects;

public class LuaNil implements LuaObject {
  private static final String value = "nil";

  @Override
  public String repr() {
    return value;
  }
}

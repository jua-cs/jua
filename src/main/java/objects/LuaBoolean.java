package objects;

public class LuaBoolean implements LuaObject {
  private boolean value;

  public LuaBoolean(boolean value) {
    this.value = value;
  }

  @Override
  public String repr() {
    if (value) {
      return "true";
    }

    return "false";
  }
}

package objects;

public class LuaString implements LuaObject {
  private String value;

  public LuaString(String value) {
    this.value = value;
  }

  @Override
  public String repr() {
    return value;
  }
}

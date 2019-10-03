package objects;

public class LuaNumber implements LuaObject {
  private Number value;

  public LuaNumber(Number value) {
    this.value = value;
  }

  @Override
  public String repr() {
    return value.toString();
  }
}

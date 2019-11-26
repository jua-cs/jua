package jua.objects;

public class LuaBreak implements LuaObject {
  @Override
  public String repr() {
    return "break";
  }

  @Override
  public String getTypeName() {
    return "break";
  }
}

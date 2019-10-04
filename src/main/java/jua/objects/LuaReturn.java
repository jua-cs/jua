package jua.objects;

public class LuaReturn implements LuaObject {
  private LuaObject value = new LuaNil();

  public LuaReturn(LuaObject value) {
    this.value = value;
  }

  public LuaReturn() {}

  public LuaObject getValue() {
    return value;
  }

  @Override
  public String repr() {
    return value.repr();
  }
}

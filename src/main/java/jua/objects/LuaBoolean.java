package jua.objects;

public class LuaBoolean implements LuaObject {
  private boolean value;

  public static LuaBoolean valueOf(LuaObject object) {
    if (object instanceof LuaBoolean) {
      return (LuaBoolean) object;
    } else if (object instanceof LuaNil) {
      return new LuaBoolean(false);
    } else {
      return new LuaBoolean(true);
    }
  }

  public LuaBoolean(boolean value) {
    this.value = value;
  }

  public boolean getValue() {
    return value;
  }

  @Override
  public String repr() {
    if (value) {
      return "true";
    }

    return "false";
  }
}

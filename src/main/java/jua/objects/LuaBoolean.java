package jua.objects;

import java.util.Objects;

public class LuaBoolean implements LuaObject {
  private boolean value;

  public LuaBoolean(boolean value) {
    this.value = value;
  }

  public static LuaBoolean valueOf(LuaObject object) {
    if (object instanceof LuaBoolean) {
      return (LuaBoolean) object;
    } else if (object instanceof LuaNil) {
      return new LuaBoolean(false);
    } else if (object instanceof LuaReturn) {
      return LuaBoolean.valueOf(((LuaReturn) object).getValue());
    } else {
      return new LuaBoolean(true);
    }
  }

  public boolean getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "LuaBoolean{" + "value=" + value + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LuaBoolean that = (LuaBoolean) o;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String repr() {
    if (value) {
      return "true";
    }

    return "false";
  }
}

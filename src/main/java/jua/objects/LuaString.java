package jua.objects;

import java.util.Objects;
import jua.evaluator.IllegalCastException;

public class LuaString implements LuaObject {

  private String value;

  public LuaString(String value) {
    this.value = value;
  }

  public static LuaString valueOf(LuaObject object) throws IllegalCastException {
    // Only types allowed to be casted to a string in lua
    if (object instanceof LuaString || object instanceof LuaNumber) {
      return new LuaString(object.repr());
    } else if (object instanceof LuaReturn) {
      return LuaString.valueOf(((LuaReturn) object).getValues().get(0));
    }

    throw IllegalCastException.create(object, "LuaString");
  }

  public void append(LuaString str) {
    value = value + str.getValue();
  }

  @Override
  public String repr() {
    return value;
  }

  @Override
  public String toString() {
    return "LuaString{" + "value='" + value + '\'' + '}';
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LuaString luaString = (LuaString) o;
    return Objects.equals(value, luaString.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}

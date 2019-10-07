package jua.objects;

import java.util.Objects;
import jua.evaluator.IllegalCastException;

public class LuaNumber implements LuaObject {
  private Double value;

  public LuaNumber(Double value) {
    this.value = value;
  }

  public static LuaNumber valueOf(LuaObject o) throws IllegalCastException {

    if (o instanceof LuaReturn) {
      return LuaNumber.valueOf(((LuaReturn) o).getValues().get(0));
    }
    // http://www.lua.org/manual/5.1/manual.html#pdf-tonumber reference
    if (o instanceof LuaNumber) return (LuaNumber) o;
    if (o instanceof LuaString) {
      LuaString luaString = (LuaString) o;
      String str = luaString.getValue();
      Double number;
      try {
        number = Double.valueOf(str);
      } catch (NumberFormatException e) {
        throw new IllegalCastException(
            String.format("The LuaString value %s is not a number.", str));
      }

      return new LuaNumber(number);
    }
    throw IllegalCastException.create(o, "LuaNumber");
  }

  public Double getValue() {
    return value;
  }

  @Override
  public String repr() {
    if (value == Math.floor(value)) {
      return String.format("%d", value.intValue());
    }

    return ((Float) ((float) ((double) value))).toString();
  }

  @Override
  public String toString() {
    return "LuaNumber{" + "value=" + value + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LuaNumber luaNumber = (LuaNumber) o;
    return Objects.equals(value, luaNumber.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}

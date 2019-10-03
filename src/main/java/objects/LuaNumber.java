package objects;

import evaluator.IllegalCastException;

public class LuaNumber implements LuaObject {
  private Double value;

  public LuaNumber(Double value) {
    this.value = value;
  }

  public static LuaNumber valueOf(LuaObject o) throws IllegalCastException {
    // http://www.lua.org/manual/5.1/manual.html#pdf-tonumber reference
    if (o instanceof LuaNumber) return (LuaNumber) o;
    if (o instanceof LuaString) {
      LuaString luaString = (LuaString) o;
      String str = luaString.getValue();
      Double number = Double.valueOf(str);
      if (number.isNaN())
        throw new IllegalCastException(
            String.format("The LuaString value %s is not a number.", str));
      return new LuaNumber(number);
    }
    throw new IllegalCastException(
        String.format("Can only get LuaNumber from LuaNumber or LuaString, not %s", o.getClass()));
  }

  public Double getValue() {
    return value;
  }

  public Double getValue() {
    return value;
  }

  @Override
  public String repr() {
    return value.toString();
  }
}

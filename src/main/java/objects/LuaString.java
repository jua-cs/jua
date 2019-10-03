package objects;

import evaluator.IllegalCastException;

public class LuaString implements LuaObject {

  private String value;

  public LuaString(String value) {
    this.value = value;
  }

  public static LuaString valueOf(LuaObject object) throws IllegalCastException {
    // Only types allowed to be casted to a string in lua
    if (object instanceof LuaString || object instanceof LuaNumber) {
      return new LuaString(object.repr());
    }

    throw IllegalCastException.create(object, "LuaString");
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void append(LuaString str) {
    value = value + str.getValue();
  }

  @Override
  public String repr() {
    return value;
  }

  public String getValue() {
    return value;
  }
}

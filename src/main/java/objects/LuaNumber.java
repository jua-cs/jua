package objects;

public class LuaNumber implements LuaObject {
  private Number value;

  public LuaNumber(Number value) {
    this.value = value;
  }

  public static LuaNumber valueOf(LuaObject o) throws Exception {
    if (o instanceof LuaNumber)return (LuaNumber) o;
    if (o instanceof LuaString) {
      LuaString luaString = (LuaString) o;
      String str =  luaString.getValue();
      // TODO; check difference with the builtin tonumber in lua
      Double number = Double.valueOf(str);
      if (number.isNaN()) throw new Exception();
      return new LuaNumber(number);
    }
    throw new Exception();
  }

  @Override
  public String repr() {
    return value.toString();
  }
}

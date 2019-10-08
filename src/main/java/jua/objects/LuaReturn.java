package jua.objects;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LuaReturn implements LuaObject {
  private ArrayList<LuaObject> values = util.Util.createArrayList(LuaNil.getInstance());

  public LuaReturn(ArrayList<LuaObject> values) {
    if (values.size() > 0) {
      this.values = values;
    }
  }

  public LuaReturn(LuaObject value) {
    this.values = util.Util.createArrayList(value);
  }

  public LuaReturn() {}

  public ArrayList<LuaObject> getValues() {
    return values;
  }

  @Override
  public String repr() {
    return values.stream().map(LuaObject::repr).collect(Collectors.joining(","));
  }
}

package jua.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class LuaTable implements LuaObject {
  private HashMap<LuaObject, LuaObject> map = new HashMap<>();

  @Override
  public String toString() {
    return "LuaTable{" + "map=" + map + '}';
  }

  @Override
  public String repr() {
    return String.format("table: 0xTODODO");
  }

  public LuaObject get(LuaObject key) {
    return map.getOrDefault(key, LuaNil.getInstance());
  }

  public void put(LuaObject key, LuaObject value) {
    map.put(key, value);
  }

  public int size() {
    return map.size();
  }

  public ArrayList<LuaObject> keys() {
    return new ArrayList<>(map.keySet());
  }
}

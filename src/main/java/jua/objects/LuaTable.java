package jua.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;
import util.Tuple;

public class LuaTable implements LuaObject {
  private HashMap<LuaObject, LuaObject> map = new HashMap<>();
  private ArrayList<LuaObject> list = new ArrayList<>();

  @Override
  public String toString() {
    return "LuaTable{" + "map=" + map + ", list=" + list + '}';
  }

  @Override
  public String repr() {
    return String.format("table: @%d", this.hashCode());
  }

  public LuaObject getList(int idx) {
    if (idx < list.size()) {
      var res = list.get(idx);
      return res != null ? res : LuaNil.getInstance();
    }
    return LuaNil.getInstance();
  }

  public LuaObject get(LuaObject key) {
    // If integer go through the list otherwise fallback on the map
    if (isPositiveInteger(key)) {
      int nb = ((LuaNumber) key).getIntValue();
      if (nb < list.size()) {
        var res = list.get(nb);
        return res != null ? res : LuaNil.getInstance();
      }
    }

    return map.getOrDefault(key, LuaNil.getInstance());
  }

  public void put(String key, LuaObject value) {
    put(new LuaString(key), value);
  }

  public void put(LuaObject key, LuaObject value) {
    if (isPositiveInteger(key)) {
      // Check if there is enough space to store it in the array list
      int nb = ((LuaNumber) key).getIntValue();
      if (nb < list.size()) {
        list.set(nb, value);
      } else {
        // TODO: find a better way of doing this to avoid memory leak
        // Fill with nulls until size is good
        while (nb >= list.size()) {
          list.add(null);
        }
        list.set(nb, value);
      }
    } else {
      map.put(key, value);
    }
  }

  public LuaObject remove(LuaObject key) {
    if (isPositiveInteger(key)) {
      int nb = ((LuaNumber) key).getIntValue();
      return nb < list.size() ? list.remove(nb) : LuaNil.getInstance();
    } else {
      return map.remove(key);
    }
  }

  public int size() {
    // Count non null elements from the list starting at 1
    if (list.size() <= 1) {
      return 0;
    }

    int count = 0;
    for (LuaObject obj : list.subList(1, list.size())) {
      if (obj == null) {
        return count;
      }

      count++;
    }

    return count;
  }

  public ArrayList<LuaObject> keys() {
    return new ArrayList<>(map.keySet());
  }

  public ArrayList<LuaObject> listValues() {
    return list.stream().filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
  }

  public ArrayList<Tuple<LuaObject, LuaObject>> items() {
    return map.keySet().stream()
        .map(k -> new Tuple<>(k, map.get(k)))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private boolean isPositiveInteger(LuaObject o) {
    if (!(o instanceof LuaNumber)) {
      return false;
    }

    Double nb = ((LuaNumber) o).getValue();

    return nb == Math.floor(nb) && nb > 0;
  }

  public void insertList(LuaObject value) {
    // Insert at the first null encountered or at the end
    // Start at 1 !
    for (int i = 1; i < list.size(); i++) {
      if (list.get(i) == null) {
        list.set(i, value);
        return;
      }
    }

    list.add(value);
  }
}

package jua.objects;

public class LuaNil implements LuaObject {
  private static final String value = "nil";

  @Override
  public String repr() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LuaNil luaNil = (LuaNil) o;
    // This is wanted since we only want one nil value
    return value == luaNil.value;
  }
}

package util;

import java.util.ArrayList;

public class Util {
  public static <T> ArrayList<T> createArrayList(T... elements) {
    ArrayList<T> a = new ArrayList<>();
    for (T element : elements) {
      a.add(element);
    }

    return a;
  }
}

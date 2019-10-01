package util;

import java.util.ArrayList;
import java.util.Collections;

public class Util {

  @SafeVarargs
  public static <T> ArrayList<T> createArrayList(T... elements) {
    ArrayList<T> a = new ArrayList<>();
    Collections.addAll(a, elements);

    return a;
  }
}

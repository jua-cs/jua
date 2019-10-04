package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Util {

  @SafeVarargs
  public static <T> ArrayList<T> createArrayList(T... elements) {
    ArrayList<T> a = new ArrayList<>();
    Collections.addAll(a, elements);

    return a;
  }

  public static String indent(String str) {
    return new ArrayList<String>(Arrays.asList(str.split("\n")))
        .stream().map(s -> "\t" + s).collect(Collectors.joining("\n"));
  }
}

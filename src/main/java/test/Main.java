package test;

import java.util.stream.Stream;
import token.Keyword;

public class Main {

  public static void main(String[] args) {
    Stream.of(Keyword.values()).forEach(System.out::println);
  }
}

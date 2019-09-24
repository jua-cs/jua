package test;

import token.Keyword;

import java.util.stream.Stream;

public class Main {

  public static void main(String[] args) {
    Stream.of(Keyword.values()).forEach(System.out::println);
  }
}

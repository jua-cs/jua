package cli;

import api.Server;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import jua.Interpreter;
import jua.evaluator.LuaRuntimeException;
import jua.lexer.Lexer;
import jua.parser.IllegalParseException;
import jua.parser.Parser;
import jua.token.Token;
import util.BufferedChannel;

public class Main {

  public static void main(String[] args) throws IllegalParseException, LuaRuntimeException {
    ArrayList<String> argsList = new ArrayList<String>(Arrays.asList(args));

    if (argsList.size() > 0
        && (argsList.contains("-h")
            || argsList.contains("--help")
            || argsList.get(0).equals("help"))) {
      usage();
    }

    if (argsList.contains("--server")) {
      new Server().run(args, 3000);
    }

    ArrayList<String> nonFlags =
        argsList.stream()
            .filter(x -> !x.startsWith("-"))
            .collect(Collectors.toCollection(ArrayList::new));

    // Check if we should use a file or stdin
    InputStream in = System.in;
    if (nonFlags.size() > 0) {
      String name = nonFlags.get(0);
      try {
        in = new FileInputStream(name);
      } catch (FileNotFoundException e) {
        System.err.printf("Could not find file: %s\n", name);
        System.exit(1);
      }
    }

    if (argsList.contains("-d") || argsList.contains("--debug")) {
      debug(in);
    } else {
      repl(in);
    }
  }

  private static void noninteractive(BufferedChannel<Character> out, InputStream in)
      throws InterruptedException {
    try {
      while (true) {
        int ch = in.read();
        if (ch == -1) {
          break;
        }
        out.add((char) ch);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    out.add('\0');
  }

  private static void interactive(BufferedChannel<Character> out) throws InterruptedException {
    var console = System.console();

    while (true) {
      String line = console.readLine();

      // Console is closed
      if (line == null) {
        break;
      }
      line += '\n';
      for (Character ch : line.toCharArray()) {
        out.add(ch);
      }
    }
  }

  private static void repl(InputStream in) {
    BufferedChannel<Character> ch = new BufferedChannel<>();
    var interpreter = new Interpreter(ch);
    boolean isInteractive = System.console() != null && in == System.in;
    new Thread(
            () -> {
              try {
                if (isInteractive) {
                  interactive(ch);
                } else {
                  noninteractive(ch, in);
                }
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
        .start();
    interpreter.start(isInteractive);
  }

  private static void debug(InputStream in) throws IllegalParseException, LuaRuntimeException {
    String text = null;
    try {
      text = new String(in.readAllBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("---");
    System.out.println("Lexing:");
    ArrayList<Token> tokens = (new Lexer(text)).getNTokens(0);
    tokens.forEach(System.out::println);
    System.out.println("---");
    System.out.println("Parsing:");
    Parser parser = new Parser(tokens);
    var stmts = parser.parse();
    stmts.getChildren().forEach(System.out::println);
    System.out.println("---");
    System.out.println("Evaluation:");
    var interpreter = new Interpreter(text);
    interpreter.run();
    System.out.println("---");
  }

  private static void usage() {
    System.err.println(
        "Welcome to jua ! Usage:\n"
            + "\n"
            + "- jua to launch a Lua REPL\n"
            + "- jua --server to run the web API (on port 3000, hardcoded for now TODO)\n"
            + "- jua <file.lua> to run a lua script (use -d or --debug to enable the debug mode)\n"
            + "- jua -h or jua --help to print this help message\n");
    System.exit(0);
  }
}

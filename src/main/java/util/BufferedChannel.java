package util;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class BufferedChannel<T> {
  private static final int size = 10000;

  private LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>(size);
  private ArrayList<T> buffer = new ArrayList<>();

  public static BufferedChannel<Character> fromString(String in) {
    BufferedChannel<Character> bc = new BufferedChannel<>();
    for (char c : in.toCharArray()) {
      // process c
      try {
        bc.add((char) c);
      } catch (InterruptedException e) {
        // TOOD handle
        e.printStackTrace();
      }
    }

    // Add EOF
    try {
      bc.add((char) 0);
    } catch (InterruptedException e) {
      // TODO Handle
      e.printStackTrace();
    }

    return bc;
  }

  public T peek() throws InterruptedException {
    if (buffer.isEmpty()) {
      return queue.peek();
    }

    return buffer.get(0);
  }

  public T read() throws InterruptedException {
    if (buffer.isEmpty()) {
      return queue.take();
    }

    return buffer.remove(0);
  }

  public final void add(T element) throws InterruptedException {
    queue.put(element);
  }
}
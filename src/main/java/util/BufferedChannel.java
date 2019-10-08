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

  public T peek(int pos) throws InterruptedException {
    if (pos < 0) {
      return null;
    }

    if (pos < buffer.size()) {
      return buffer.get(pos);
    }

    // Else read until pos and buffer it
    for (int i = 0; i <= pos - buffer.size(); i++) {
      buffer.add(queue.take());
    }

    return buffer.get(pos);
  }

  public T peek() {
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

  public void skip() throws InterruptedException {
    if (buffer.isEmpty()) {
      queue.take();
    } else {
      buffer.remove(0);
    }
  }

  public final void add(T element) throws InterruptedException {
    // System.out.printf("Adding: %s of type %s to queue\n", element, element.getClass());
    queue.put(element);
  }
}

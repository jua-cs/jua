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
    synchronized (buffer) {
      if (pos < 0) {
        return null;
      }

      if (pos < buffer.size()) {
        return buffer.get(pos);
      }

      // Else read until pos and buffer it
      while (buffer.size() <= pos) {
        buffer.add(queue.take());
      }
      return buffer.get(pos);
    }
  }

  public T peek() throws InterruptedException {
    return peek(0);
  }

  public T read() throws InterruptedException {

    synchronized (buffer) {
      if (!buffer.isEmpty()) {
        return buffer.remove(0);
      }
    }
    return queue.take();
  }

  public void skip() throws InterruptedException {
    synchronized (buffer) {
      if (!buffer.isEmpty()) {
        buffer.remove(0);
        return;
      }
    }
    queue.take();
  }

  public final void add(T element) throws InterruptedException {
    // System.out.printf("Adding: %s of type %s to queue\n", element, element.getClass());
    queue.put(element);
  }
}

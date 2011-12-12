/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.util.Queue;

/**
 *
 * @author luisarmando
 */

public class BlockingQueue<T> implements Queue<T> {
  private final Object mutex = new Object();
  private final Queue<T> queue;
  private final int maxSize;

  public BlockingQueue(Queue<T> queue, int maxSize) {
    if (queue == null) {
      throw new IllegalArgumentException("queue can't be null");
    }
    else if (maxSize < 0) {
      throw new IllegalArgumentException("maxSize can't be &lt; 0");
    }

    this.queue = queue;
    this.maxSize = maxSize;
  }

  public BlockingQueue(Queue<T> queue) {
    this(queue, Integer.MAX_VALUE);
  }

  @Override
  public void enqueue(T value) {
    synchronized (mutex) {
      while (size() == maxSize) {
        waitForNotification();
      }
      queue.enqueue(value);
      mutex.notifyAll();
    }
  }

  private synchronized void waitForNotification() {
    try {
      mutex.wait();
    } catch (InterruptedException e) {
      // Ignored
    }
  }

  @Override
  public T dequeue() throws EmptyQueueException {
    synchronized (mutex) {
      while (isEmpty()) {
        waitForNotification();
      }

      T value = queue.dequeue();
      mutex.notifyAll();
      return value;
    }
  }

  @Override
  public void clear() {
    synchronized (mutex) {
      queue.clear();
      mutex.notifyAll();
    }
  }

  @Override
  public int size() {
    synchronized (mutex) {
      return queue.size();
    }
  }

  @Override
  public boolean isEmpty() {
    synchronized (mutex) {
      return queue.isEmpty();
    }
  }
}

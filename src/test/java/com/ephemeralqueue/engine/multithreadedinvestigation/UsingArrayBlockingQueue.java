package com.ephemeralqueue.engine.multithreadedinvestigation;

import org.junit.jupiter.api.Test;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/*
 * I learned from this test that my implementation of
 * Ephemeral Queue is not thread safe, but I would like it be.
 *
 * https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html
 * https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html
 */
public class UsingArrayBlockingQueue {
  private static final int QUEUE_LENGTH = 100_000;

  @Test
  public void main() {
    usingTwoThreadsAndArrayBlockingQueue();
  }

  /** In this strategy, two queues add values in a competing manner to the BlockingQueue.
   *
   * Then values are dequeue in a third thread, the println method is synchronized so that
   * values are printed at the same time as dequeuing.
   *
   */
  private static void usingTwoThreadsAndArrayBlockingQueue() {
    Queue<Integer> q = new ArrayBlockingQueue<Integer>(QUEUE_LENGTH);

    ArrayBlockingQueueClientThread t1 = new ArrayBlockingQueueClientThread(q, 1);
    ArrayBlockingQueueClientThread t2 = new ArrayBlockingQueueClientThread(q, 2000);

    t1.start();
    t2.start();
  }

  /**
   * This was the solution to the problem documented here:
   * <a href="https://stackoverflow.com/questions/79430429/unexpected-behavior-with-2-enqueuing-threads-when-using-built-in-java-arrayblock/79430564#79430564">...</a>
   *
   * @param q
   * @throws InterruptedException
   */
  private static synchronized void synchronizedPollAndPrint(Queue<Integer> q) throws InterruptedException {
    System.out.println(q.poll());
  }

  private static class ArrayBlockingQueueClientThread extends Thread {
    Queue<Integer> q;
    int multiplier;

    ArrayBlockingQueueClientThread(Queue<Integer> ephemeralQueue, int multiplier) {
      this.q = ephemeralQueue;
      this.multiplier = multiplier;
    }

    public void run() {
      for (int i = 0; i < 20 ; i++) {
        q.add(i * multiplier);
      }

      int k = 0;
      while(k<20)
      {
        k++;
        try {
          synchronizedPollAndPrint(q);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

}
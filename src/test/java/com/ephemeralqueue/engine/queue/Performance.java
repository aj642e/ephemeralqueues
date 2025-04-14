package com.ephemeralqueue.engine.queue;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Queue;

public class Performance {
  private static final int QUEUE_CAPACITY = 100_000;

  @Test
  public void main() {
    loadTestOneQueue();
  }

  private static void loadTestOneQueue() {
    /**
     * As it turns out, my implementation is 3X worse performance than
     * Array Blocking Queue and Concurrent Linked Queue.
     */
    Queue<Integer> q = new RingBufferQueue(QUEUE_CAPACITY);
    Instant start = Instant.now();

    for (int i = 0; i < 1_000_000; i++) {
      q.add(i);
      q.remove();
    }

    Instant end = Instant.now();

    System.out.println("one million enqueues and dequeues (2 million operations total) took this many nanoseconds:");
    System.out.println(Duration.between(start, end).getNano());
    System.out.println("one operation took this many nanoseconds: ");
    System.out.println(Duration.between(start, end).getNano() / 2_000_000);
  }
}
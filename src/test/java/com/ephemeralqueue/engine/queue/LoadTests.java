package com.ephemeralqueue.engine.queue;

import java.time.Duration;
import java.time.Instant;
import java.util.Queue;

public class LoadTests {
  private static final int QUEUE_LENGTH = 100_000;

  public static void main(String[] args) {
    loadTestOneQueue();
  }

  private static void loadTestOneQueue() {
    Queue<Integer> q = new EphemeralQueue(QUEUE_LENGTH);
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
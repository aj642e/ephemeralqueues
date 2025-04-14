package com.ephemeralqueue.engine.queuecollection;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * General findings so far:
 *
 * 1000 queues, 10,000 operations == .6 seconds (675071000 ns).
 * 5000 queues, 100K operations == 40 seconds.
 *
 * Same cost per operation (~90 ns).
 *
 * Is this good or not?
 *
 */
public class Performance {
  private static final int QUEUE_CAPACITY = 50_000;
  private static AtomicInteger count = new AtomicInteger(0);

  @Test
  public void main() throws InterruptedException {
    loadTestAddingAndPolling(500);
    loadTestAddingAndPolling(1000);
  }

  private static void loadTestAddingAndPolling(int numQueues) throws InterruptedException {
    count.set(0);
    QueueCollection queueCollection = new QueueCollection(numQueues, QUEUE_CAPACITY);
    Instant start = Instant.now();

    for (int i = 0; i < numQueues; i++) {
      queueCollection.createQueue();
    }

    List<Thread> threads = new ArrayList<>();

    for (int i = 0; i < numQueues; i++) {
      Thread thread = new QueueClient(i, queueCollection);
      threads.add(thread);
    }

    for (Thread thread : threads) {
      thread.start();
    }

    for (Thread thread : threads) {
      thread.join();
    }

    Instant end = Instant.now();

    System.out.println(
        numQueues + " queues and threads, " + QUEUE_CAPACITY + " operations per queue took this many nanoseconds:");
    System.out.println(Duration.between(start, end).toNanos());

    System.out.println("approx this many seconds: ");
    System.out.println(Duration.between(start, end).toSeconds());

    System.out.println("one operation took this many nanoseconds: ");
    System.out.println(
        (Duration.between(start, end).toNanos() / (numQueues * QUEUE_CAPACITY))
    );

    System.out.println("this many pollings were made: " + count.get());
  }

  static class QueueClient extends Thread {
    QueueCollection queueCollection;
    int queueId;

    QueueClient(int queueId, QueueCollection queueCollection) {
      this.queueId = queueId;
      this.queueCollection = queueCollection;
    }

    public void run() {
      for (int i = 0; i < QUEUE_CAPACITY; i++) {
        queueCollection.add(queueId, i);
      }

      for (int i = 0; i < QUEUE_CAPACITY; i++) {
        QueueValue v = queueCollection.poll(queueId);
        count.getAndIncrement();

        /*
          This is to confirm the amount of polling is the same as amount of insertions
         */
        if (v.value() == null) {
          throw new RuntimeException("Queue " + queueId + " is empty");
        }
      }
    }
  }
}
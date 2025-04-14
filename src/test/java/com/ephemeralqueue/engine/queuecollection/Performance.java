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
 * Multithreaded test:
 * 1000 queues, 10,000 operations == .6 seconds (675071000 ns).
 * 5000 queues, 100K operations == 40 seconds.
 *
 * Same cost per operation (~90 ns).
 *
 * Is this good or not?
 *
 */
public class Performance {
  private static final int QUEUE_CAPACITY = 100_000;
  private static AtomicInteger count = new AtomicInteger(0);

  @Test
  public void main() throws InterruptedException {
    int oneThousandQueues = 1000;
    multiThreadedAddingAndPolling(oneThousandQueues);
    /*
    The multithreaded is faster by about 2x.
     */
    singleThreadedAddingAndPolling(oneThousandQueues);
  }

  private static void singleThreadedAddingAndPolling(int numQueues) {
    QueueCollection queueCollection = new QueueCollection(numQueues, QUEUE_CAPACITY);
    Instant start = Instant.now();

    createQueues(numQueues, queueCollection);

    for (int queueId = 0; queueId < numQueues; queueId++) {

      addToQueue(queueCollection, queueId);

      pollQueue(queueCollection, queueId);

    }

    Instant end = Instant.now();

    printResults(numQueues, start, end, (long) numQueues);
  }

  private static void multiThreadedAddingAndPolling(int numQueues) throws InterruptedException {
    count.set(0);
    QueueCollection queueCollection = new QueueCollection(numQueues, QUEUE_CAPACITY);
    Instant start = Instant.now();

    createQueues(numQueues, queueCollection);

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

    printResults(numQueues, start, end, numQueues);
  }

  static class QueueClient extends Thread {
    QueueCollection queueCollection;
    int queueId;

    QueueClient(int queueId, QueueCollection queueCollection) {
      this.queueId = queueId;
      this.queueCollection = queueCollection;
    }

    public void run() {
      addToQueue(queueCollection, queueId);
      pollQueue(queueCollection, queueId);
    }
  }

  private static void printResults(int numQueues, Instant start, Instant end, long numQueues1) {
    System.out.println(
        numQueues + " queues and threads, " + QUEUE_CAPACITY + " operations per queue took this many nanoseconds:");
    System.out.println(Duration.between(start, end).toNanos());

    System.out.println("approx this many seconds: ");
    System.out.println(Duration.between(start, end).toSeconds());

    System.out.println("one operation took this many nanoseconds: ");
    System.out.println(
        (Duration.between(start, end).toNanos() / (numQueues1 * QUEUE_CAPACITY))
    );

    /*
    Running an atomic counter across all the threads sent operation cost from 15 ns to 70 ns. Wow!
     */
//    System.out.println("this many pollings were made: " + count.get());
  }

  private static void createQueues(int numQueues, QueueCollection queueCollection) {
    for (int i = 0; i < numQueues; i++) {
      queueCollection.createQueue();
    }
  }

  private static void addToQueue(QueueCollection queueCollection, int queueId) {
    for (int i = 0; i < QUEUE_CAPACITY; i++) {
      queueCollection.add(queueId, i);
    }
  }

  private static void pollQueue(QueueCollection queueCollection, int queueId) {
    for (int i = 0; i < QUEUE_CAPACITY; i++) {
      QueueValue v = queueCollection.poll(queueId);
      if (v.value() == null) {
        throw new RuntimeException("Queue " + queueId + " is empty");
      }
    }
  }
}
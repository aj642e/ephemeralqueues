package com.ephemeralqueue.engine.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class EphemeralQueueTests {
  private static final int QUEUE_LENGTH = 100_000;
  private static final List<Boolean> assertions = new ArrayList<>();

  public static void main(String[] args) {
    enqueueAllAndDequeueAll();
    enqueueAllAndDequeueAllPartialFill();
    enqueueAndDequeueIncrementally();
    assertEmptyAtStart();

    System.out.println("Do all the tests pass?");
    System.out.println(assertions.stream().allMatch(assertion -> assertion));
  }

  private static void assertEmptyAtStart() {
    Queue<Integer> q = getEphemeralQueue();
    assert_empty_queue(q);
  }

  private static EphemeralQueue getEphemeralQueue() {
    return new EphemeralQueue(QUEUE_LENGTH);
  }


  private static void assert_empty_queue(Queue<Integer> q) {
    try {
      q.remove();
      assertions.add(false);
    } catch (NoSuchElementException e) {
      assertions.add(true);
    }
  }

  private static void enqueueAndDequeueIncrementally() {
    Queue<Integer> q = getEphemeralQueue();

    for (int i = 0; i < QUEUE_LENGTH / 2; i++) {
      assertions.add(q.add(i));
    }

    for (int i = 0; i < QUEUE_LENGTH / 2; i++) {
      assertions.add((int) q.remove() == i);
      assertions.add(q.add(i * 10));
    }

    for (int i = 0; i < QUEUE_LENGTH / 2; i++) {
      assertions.add((int) q.remove() == i*10);
    }
  }

  private static void enqueueAllAndDequeueAllPartialFill() {
    Queue<Integer> q = getEphemeralQueue();

    for (int i = 0; i < QUEUE_LENGTH / 2; i++) {
      assertions.add(q.add(i));
    }

    for (int i = 0; i < QUEUE_LENGTH / 2; i++) {
      assertions.add((int) q.remove() == i);
    }

    assert_empty_queue(q);

    for (int i = 0; i < QUEUE_LENGTH / 2; i++) {
      assertions.add(q.add(i * 10));
    }

    for (int i = 0; i < QUEUE_LENGTH / 2; i++) {
      assertions.add((int) q.remove() == i*10);
    }

    for (int i = 0; i < QUEUE_LENGTH / 2; i++) {
      assertions.add(q.add(i * 100));
    }

    for (int i = 0; i < QUEUE_LENGTH / 2; i++) {
      assertions.add((int) q.remove() == i*100);
    }
  }

  private static void enqueueAllAndDequeueAll() {
    Queue<Integer> q = getEphemeralQueue();

    for (int i = 0; i < QUEUE_LENGTH; i++) {
      assertions.add(q.add(i));
    }

    assertions.add(!q.add(1));

    for (int i = 0; i < QUEUE_LENGTH; i++) {
      assertions.add((int) q.remove() == i);
    }

    assert_empty_queue(q);

    for (int i = 0; i < QUEUE_LENGTH; i++) {
      assertions.add(q.add(i * 10));
    }

    assertions.add(!q.add(1));

    for (int i = 0; i < QUEUE_LENGTH; i++) {
      assertions.add((int) q.remove() == i*10);
    }
  }
}
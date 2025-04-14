package com.ephemeralqueue.engine.queue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Behavior {
  private static final int QUEUE_CAPACITY = 100_000;
  private static final List<Boolean> assertions = new ArrayList<>();

  @Test
  public void main() {
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
    return new EphemeralQueue(QUEUE_CAPACITY);
  }

  private static void assertTest(boolean result) {
    if (!result) {
      throw new RuntimeException("Test failed, see stack trace for details");
    }

    assertions.add(true);
  }

  private static void assert_empty_queue(Queue<Integer> q) {
    try {
      q.remove();
      assertTest(false);
    } catch (NoSuchElementException e) {
      assertTest(true);
    }
  }


  private static void enqueueAndDequeueIncrementally() {
    Queue<Integer> q = getEphemeralQueue();

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      assertTest(q.add(i));
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      assertTest((int) q.remove() == i);
      assertTest(q.add(i * 10));
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      assertTest((int) q.remove() == i*10);
    }
  }

  private static void enqueueAllAndDequeueAllPartialFill() {
    Queue<Integer> q = getEphemeralQueue();

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      assertTest(q.add(i));
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      assertTest((int) q.remove() == i);
    }

    assert_empty_queue(q);

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      assertTest(q.add(i * 10));
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      assertTest((int) q.remove() == i*10);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      assertTest(q.add(i * 100));
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      assertTest((int) q.remove() == i*100);
    }
  }

  private static void enqueueAllAndDequeueAll() {
    Queue<Integer> q = getEphemeralQueue();

    for (int i = 0; i < QUEUE_CAPACITY; i++) {
      assertTest(q.add(i));
    }

    assertTest(!q.add(1));

    for (int i = 0; i < QUEUE_CAPACITY; i++) {
      assertTest((int) q.remove() == i);
    }

    assert_empty_queue(q);

    for (int i = 0; i < QUEUE_CAPACITY; i++) {
      assertTest(q.add(i * 10));
    }

    assertTest(!q.add(1));

    for (int i = 0; i < QUEUE_CAPACITY; i++) {
      assertTest((int) q.remove() == i*10);
    }
  }
}
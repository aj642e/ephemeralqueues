package com.ephemeralqueue.engine.queue;

import com.ephemeralqueue.Shared;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
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
    Queue<Integer> q = getQueue();
    assert_empty_queue(q);
  }

  private static RingBufferQueue getQueue() {
    return new RingBufferQueue(QUEUE_CAPACITY);
  }

  private static void assert_empty_queue(Queue<Integer> q) {
    Shared.assertTrue(q.poll() == null);
  }


  private static void enqueueAndDequeueIncrementally() {
    Queue<Integer> q = getQueue();

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      Shared.assertTrue(q.add(i), assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      Shared.assertTrue((int) q.poll() == i, assertions);
      Shared.assertTrue(q.add(i * 10), assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      Shared.assertTrue((int) q.poll() == i*10, assertions);
    }
  }

  private static void enqueueAllAndDequeueAllPartialFill() {
    Queue<Integer> q = getQueue();

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      Shared.assertTrue(q.add(i), assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      Shared.assertTrue((int) q.poll() == i, assertions);
    }

    assert_empty_queue(q);

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      Shared.assertTrue(q.add(i * 10), assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      Shared.assertTrue((int) q.poll() == i*10, assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      Shared.assertTrue(q.add(i * 100), assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      Shared.assertTrue((int) q.poll() == i*100, assertions);
    }
  }

  private static void enqueueAllAndDequeueAll() {
    Queue<Integer> q = getQueue();

    Shared.testCompleteAddAndRemove(q, QUEUE_CAPACITY);

    assert_empty_queue(q);

    Shared.testCompleteAddAndRemove(q, QUEUE_CAPACITY);

    assert_empty_queue(q);

    Shared.assertTrue(q.add(1), assertions);
    Shared.assertTrue(q.poll() == 1, assertions);

    Shared.testCompleteAddAndRemove(q, QUEUE_CAPACITY-1);
  }
}
package com.ephemeralqueue.engine.queue;

import com.ephemeralqueue.TestUtil;
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
    Queue<Integer> q = getQueue();
    assert_empty_queue(q);
  }

  private static RingBufferQueue getQueue() {
    return new RingBufferQueue(QUEUE_CAPACITY);
  }

  private static void assert_empty_queue(Queue<Integer> q) {
    try {
      q.remove();
      TestUtil.assertTrue(false, assertions);
    } catch (NoSuchElementException e) {
      TestUtil.assertTrue(true, assertions);
    }
  }


  private static void enqueueAndDequeueIncrementally() {
    Queue<Integer> q = getQueue();

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      TestUtil.assertTrue(q.add(i), assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      TestUtil.assertTrue((int) q.remove() == i, assertions);
      TestUtil.assertTrue(q.add(i * 10), assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      TestUtil.assertTrue((int) q.remove() == i*10, assertions);
    }
  }

  private static void enqueueAllAndDequeueAllPartialFill() {
    Queue<Integer> q = getQueue();

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      TestUtil.assertTrue(q.add(i), assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      TestUtil.assertTrue((int) q.remove() == i, assertions);
    }

    assert_empty_queue(q);

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      TestUtil.assertTrue(q.add(i * 10), assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      TestUtil.assertTrue((int) q.remove() == i*10, assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      TestUtil.assertTrue(q.add(i * 100), assertions);
    }

    for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
      TestUtil.assertTrue((int) q.remove() == i*100, assertions);
    }
  }

  private static void enqueueAllAndDequeueAll() {
    Queue<Integer> q = getQueue();

    for (int i = 0; i < QUEUE_CAPACITY; i++) {
      TestUtil.assertTrue(q.add(i), assertions);
    }

    TestUtil.assertTrue(!q.add(1), assertions);

    for (int i = 0; i < QUEUE_CAPACITY; i++) {
      TestUtil.assertTrue((int) q.remove() == i, assertions);
    }

    assert_empty_queue(q);

    for (int i = 0; i < QUEUE_CAPACITY; i++) {
      TestUtil.assertTrue(q.add(i * 10), assertions);
    }

    TestUtil.assertTrue(!q.add(1), assertions);

    for (int i = 0; i < QUEUE_CAPACITY; i++) {
      TestUtil.assertTrue((int) q.remove() == i*10, assertions);
    }
  }
}
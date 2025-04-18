package com.ephemeralqueue;

import com.ephemeralqueue.engine.queuecollection.QueueCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Shared {

  public static void assertTrue(boolean expected) {
    if (!expected) {
      throw new RuntimeException("Test failed, see stack trace for details");
    }
  }

  public static void assertTrue(boolean expected, List<Boolean> results) {
    if (!expected) {
      throw new RuntimeException("Test failed, see stack trace for details");
    }

    results.add(true);
  }

  /*
  Maybe we want to consolidate these 2 methods.
   */
  public static void testCompleteAddAndRemove(QueueCollection queueCollection, int queueId, int capacity) {
    Random r = new Random();
    List<Integer> vals = new ArrayList<>();
    List<Integer> result = new ArrayList<>();

    for (int i = 0; i < capacity; i++) {
      vals.add(r.nextInt());
    }

    for (int i = 0; i < capacity; i++) {
      queueCollection.add(queueId, vals.get(i));
    }

    for (int i = 0; i < capacity; i++) {
      result.add(queueCollection.poll(queueId).value());
    }

    assertTrue(result.equals(vals));
  }

  public static void testCompleteAddAndRemove(Queue<Integer> queue, int capacity) {
    Random r = new Random();
    List<Integer> vals = new ArrayList<>();
    List<Integer> result = new ArrayList<>();

    for (int i = 0; i < capacity; i++) {
      vals.add(r.nextInt());
    }

    for (int i = 0; i < capacity; i++) {
      queue.add(vals.get(i));
    }

    for (int i = 0; i < capacity; i++) {
      result.add(queue.poll());
    }

    assertTrue(result.equals(vals));
  }
}

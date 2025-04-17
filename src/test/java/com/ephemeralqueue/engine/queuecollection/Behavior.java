package com.ephemeralqueue.engine.queuecollection;

import com.ephemeralqueue.TestUtil;
import com.ephemeralqueue.engine.queuecollection.entities.QueueId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static com.ephemeralqueue.engine.queuecollection.Performance.createQueues;

class Behavior {
  @Test
  public void main() throws InterruptedException {
    singleQueue();
    manyQueues();
    collectionFull();
    deleteTwice();
    queueCreationIdsIsMonotonic();
  }

  public static void singleQueue() {
    QueueCollection queueCollection = new QueueCollection(1, 100);

    QueueId q = queueCollection.createQueue();

    Random r = new Random();
    List<Integer> vals = new ArrayList<>();
    List<Integer> result = new ArrayList<>();

    for (int i = 0; i < 100; i++) {
      vals.add(r.nextInt());
    }

    for (int i = 0; i < 100; i++) {
      queueCollection.add(q.id(), vals.get(i));
    }

    for (int i = 0; i < 100; i++) {
      result.add(queueCollection.poll(q.id()).value());
    }

    TestUtil.assertTrue(result.equals(vals));
  }

  public static void manyQueues() throws InterruptedException {
    QueueCollection queueCollection = new QueueCollection(100, 100);

    createQueues(100, queueCollection);

    List<Thread> threads = new ArrayList<>();

    for (int i = 0; i < 100; i++) {
      Thread thread = new QueueClient(i, queueCollection);
      threads.add(thread);
    }

    for (Thread thread : threads) {
      thread.start();
    }

    for (Thread thread : threads) {
      thread.join();
    }

    //Delete queues.
    queueCollection.deleteQueue(1);
    queueCollection.deleteQueue(2);

    // Attempt to add after deleting.
    try {
      queueCollection.add(2, new Random().nextInt());
      TestUtil.assertTrue(false);
    } catch (NoSuchElementException e) {
      TestUtil.assertTrue(true);
    }

    try {
      queueCollection.add(1, new Random().nextInt());
      TestUtil.assertTrue(false);
    } catch (NoSuchElementException e) {
      TestUtil.assertTrue(true);
    }
  }

  static class QueueClient extends Thread {
    QueueCollection queueCollection;
    int queueId;

    QueueClient(int queueId, QueueCollection queueCollection) {
      this.queueId = queueId;
      this.queueCollection = queueCollection;
    }

    public void run() {
      Random r = new Random();
      List<Integer> vals = new ArrayList<>();
      List<Integer> result = new ArrayList<>();

      for (int i = 0; i < 100; i++) {
        vals.add(r.nextInt());
      }

      for (int i = 0; i < 100; i++) {
        queueCollection.add(queueId, vals.get(i));
      }

      for (int i = 0; i < 100; i++) {
        result.add(queueCollection.poll(queueId).value());
      }

      TestUtil.assertTrue(result.equals(vals));
    }
  }

  public static void collectionFull() {
    QueueCollection queueCollection = new QueueCollection(10, QueueCollection.DEFAULT_SIZE);

    for (int i = 0; i < 10; i++) {
      queueCollection.createQueue();
    }

    try {
      queueCollection.createQueue();
      TestUtil.assertTrue(false);
    } catch (IllegalStateException e) {
      TestUtil.assertTrue(true);
    }
  }

  public static void queueCreationIdsIsMonotonic() {
    QueueCollection queueCollection = new QueueCollection(QueueCollection.DEFAULT_SIZE, QueueCollection.DEFAULT_SIZE);

    QueueId id1 = queueCollection.createQueue();
    QueueId id2 = queueCollection.createQueue();
    QueueId id3 = queueCollection.createQueue();

    TestUtil.assertTrue(id1.id() == 0);
    TestUtil.assertTrue(id2.id() == 1);
    TestUtil.assertTrue(id3.id() == 2);

    queueCollection.deleteQueue(id3.id());

    QueueId id4 = queueCollection.createQueue();
    QueueId id5 = queueCollection.createQueue();

    TestUtil.assertTrue(id4.id() == 3);
    TestUtil.assertTrue(id5.id() == 4);
  }

  public static void deleteTwice() {
    QueueCollection queueCollection = new QueueCollection();

    QueueId q = queueCollection.createQueue();

    queueCollection.deleteQueue(q.id());

    // Try to delete again and nothing should happen.
    queueCollection.deleteQueue(q.id());

    TestUtil.assertTrue(true);
  }
}
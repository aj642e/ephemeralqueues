package com.ephemeralqueue.engine.queuecollection;

import com.ephemeralqueue.Shared;
import com.ephemeralqueue.engine.queuecollection.entities.QueueId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static com.ephemeralqueue.engine.queuecollection.Performance.createQueues;

class Behavior {

  public static final int QUEUE_CAPACITY = 100;

  @Test
  public void main() throws InterruptedException {
    singleQueue();
    manyQueuesCreationPreThreads();
    collectionFull();
    deleteTwice();
    queueCreationIdsIsMonotonic();
  }

  public static void singleQueue() {
    QueueCollection queueCollection = new QueueCollection(1, QUEUE_CAPACITY);

    QueueId q = queueCollection.createQueue();

    Shared.testCompleteAddAndRemove(queueCollection, q.id(), QUEUE_CAPACITY);
  }

  public static void manyQueuesCreationPreThreads() throws InterruptedException {
    QueueCollection queueCollection = new QueueCollection(QUEUE_CAPACITY, QUEUE_CAPACITY);

    createQueues(QUEUE_CAPACITY, queueCollection);

    List<Thread> threads = new ArrayList<>();

    for (int i = 0; i < QUEUE_CAPACITY; i++) {
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
      Shared.assertTrue(false);
    } catch (NoSuchElementException e) {
      Shared.assertTrue(true);
    }

    try {
      queueCollection.add(1, new Random().nextInt());
      Shared.assertTrue(false);
    } catch (NoSuchElementException e) {
      Shared.assertTrue(true);
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
      Shared.testCompleteAddAndRemove(queueCollection, queueId, QUEUE_CAPACITY);
    }
  }

  public static void collectionFull() {
    QueueCollection queueCollection = new QueueCollection(10, QueueCollection.DEFAULT_SIZE);

    for (int i = 0; i < 10; i++) {
      queueCollection.createQueue();
    }

    try {
      queueCollection.createQueue();
      Shared.assertTrue(false);
    } catch (IllegalStateException e) {
      Shared.assertTrue(true);
    }
  }

  public static void queueCreationIdsIsMonotonic() {
    QueueCollection queueCollection = new QueueCollection(QueueCollection.DEFAULT_SIZE, QueueCollection.DEFAULT_SIZE);

    QueueId id1 = queueCollection.createQueue();
    QueueId id2 = queueCollection.createQueue();
    QueueId id3 = queueCollection.createQueue();

    Shared.assertTrue(id1.id() == 0);
    Shared.assertTrue(id2.id() == 1);
    Shared.assertTrue(id3.id() == 2);

    queueCollection.deleteQueue(id3.id());

    QueueId id4 = queueCollection.createQueue();
    QueueId id5 = queueCollection.createQueue();

    Shared.assertTrue(id4.id() == 3);
    Shared.assertTrue(id5.id() == 4);
  }

  public static void deleteTwice() {
    QueueCollection queueCollection = new QueueCollection();

    QueueId q = queueCollection.createQueue();

    queueCollection.deleteQueue(q.id());

    // Try to delete again and nothing should happen.
    queueCollection.deleteQueue(q.id());

    Shared.assertTrue(true);
  }
}
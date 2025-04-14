package com.ephemeralqueue.engine.queuecollection;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

class Behavior {
  private static final int FIRST_VAL  = 10;
  private static final int SECOND_VAL = 20;
  private static final int THIRD_VAL  = 30;
  private static final int FOURTH_VAL = 40;

  @Test
  public void main() {
    singleQueue();
    twoQueues();
    collectionFull();
    deleteTwice();
  }

  public static void singleQueue() {
    QueueCollection queueCollection = new QueueCollection(1, 4);

    QueueId q = queueCollection.createQueue();

    queueCollection.add(q.id(), FIRST_VAL);
    queueCollection.add(q.id(), SECOND_VAL);
    queueCollection.add(q.id(), THIRD_VAL);
    queueCollection.add(q.id(), FOURTH_VAL);

    QueueValue v = queueCollection.poll(q.id());
    System.out.println(v.value() == FIRST_VAL);

    v = queueCollection.poll(q.id());
    System.out.println(v.value() == SECOND_VAL);

    v = queueCollection.poll(q.id());
    System.out.println(v.value() == THIRD_VAL);

    v = queueCollection.poll(q.id());
    System.out.println(v.value() == FOURTH_VAL);
  }

  public static void twoQueues() {
    QueueCollection queueCollection = new QueueCollection(2, 4);

    //Create Queues.
    QueueId q = queueCollection.createQueue();
    QueueId q2 = queueCollection.createQueue();

    //Add to Queues.
    queueCollection.add(q.id(), FIRST_VAL);
    queueCollection.add(q.id(), SECOND_VAL);
    queueCollection.add(q.id(), THIRD_VAL);
    queueCollection.add(q.id(), FOURTH_VAL);

    queueCollection.add(q2.id(), FOURTH_VAL);
    queueCollection.add(q2.id(), THIRD_VAL);
    queueCollection.add(q2.id(), SECOND_VAL);
    queueCollection.add(q2.id(), FIRST_VAL);

    //Poll the Queues.
    QueueValue v = queueCollection.poll(q.id());
    System.out.println(v.value() == FIRST_VAL);

    v = queueCollection.poll(q.id());
    System.out.println(v.value() == SECOND_VAL);

    v = queueCollection.poll(q.id());
    System.out.println(v.value() == THIRD_VAL);

    v = queueCollection.poll(q.id());
    System.out.println(v.value() == FOURTH_VAL);

    v = queueCollection.poll(q2.id());
    System.out.println(v.value() == FOURTH_VAL);

    v = queueCollection.poll(q2.id());
    System.out.println(v.value() == THIRD_VAL);

    v = queueCollection.poll(q2.id());
    System.out.println(v.value() == SECOND_VAL);

    v = queueCollection.poll(q2.id());
    System.out.println(v.value() == FIRST_VAL);

    // Poll empty queues returns null.
    System.out.println(queueCollection.poll(q2.id()).value() == null);
    System.out.println(queueCollection.poll(q.id()).value() == null);

    //Delete queues.
    queueCollection.deleteQueue(q.id());
    queueCollection.deleteQueue(q2.id());

    // Attempt to add after deleting.
    try {
      queueCollection.add(q2.id(), FOURTH_VAL);
      System.out.println(false);
    } catch (NoSuchElementException e) {
      System.out.println(true);
    }

    try {
      queueCollection.add(q.id(), FOURTH_VAL);
      System.out.println(false);
    } catch (NoSuchElementException e) {
      System.out.println(true);
    }
  }

  public static void collectionFull() {
    QueueCollection queueCollection = new QueueCollection(QueueCollection.DEFAULT_SIZE, QueueCollection.DEFAULT_SIZE);

    for (int i = 0; i < QueueCollection.DEFAULT_SIZE; i++) {
      queueCollection.createQueue();
    }

    try {
      queueCollection.createQueue();
      System.out.println(false);
    } catch (IllegalStateException e) {
      System.out.println(true);
    }
  }

  public static void deleteTwice() {
    QueueCollection queueCollection = new QueueCollection();

    QueueId q = queueCollection.createQueue();

    queueCollection.deleteQueue(q.id());

    // Try to delete again and nothing should happen.
    queueCollection.deleteQueue(q.id());

    System.out.println(true);
  }
}
package com.ephemeralqueue.engine.queuecollection;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueCollection {
  public static final String                        QUEUE_NOT_FOUND_MESSAGE    = "Queue not found.";
  public static final String                        COLLECTION_IS_FULL_MESSAGE = "Collection is Full.";
  public static final int                           DEFAULT_SIZE               = 100;

  private final       int                           maxQueueLength;
  private final       Queue<Integer>[] collection;

  /**
   * Question: Which parts of this implementation are not thread safe?
   *
   * 1. checkQueueExistence()
   * 2. getNewQueueId()
   * 3. deleteQueue is interesting because it just deletes queues, but something could get put in there afterward.
   *
   *  ... so basically all accessing and managing the queue collection.
   *
   * @param maxCollectionSize
   * @param maxQueueLength
   */
  public QueueCollection(int maxCollectionSize, int maxQueueLength) {
    this.maxQueueLength = maxQueueLength;
    this.collection     = new ArrayBlockingQueue[maxCollectionSize];
  }

  public QueueCollection() {
    this(DEFAULT_SIZE, DEFAULT_SIZE);
  }

  public QueueId createQueue() {
    int i         = getNewQueueId();
    collection[i] = new ArrayBlockingQueue<>(maxQueueLength);

    return new QueueId(i);
  }

  public void deleteQueue(int i) {
    collection[i] = null;
  }

  public boolean add(int i, int val) {
    checkQueueExistence(i);
    return collection[i].add(val);
  }

  public QueueValue remove(int i) {
    checkQueueExistence(i);
    return new QueueValue(collection[i].remove());
  }

  private void checkQueueExistence(int i) {
    if (collection[i] == null) {
      throw new NoSuchElementException(QUEUE_NOT_FOUND_MESSAGE);
    }
  }

  private int getNewQueueId() {
    for (int i = 0; i < collection.length; i++)
    {
      if (collection[i] == null) return i;
    }

    throw new IllegalStateException(COLLECTION_IS_FULL_MESSAGE);
  }
}
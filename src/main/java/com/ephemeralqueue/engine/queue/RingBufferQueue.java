package com.ephemeralqueue.engine.queue;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A ring-buffered version of a Queue.
 *
 * It is not thread safe.
 *
 * It is also not as performant as the built-in implementations.
 */
public class RingBufferQueue implements Queue<Integer> {
  public static final String NOT_SUPPORTED_YET_MESSAGE = "Not supported yet.";

  private final int[] memory;

  /*
    Enqueue into Front Idx.
    Dequeue by removing element at Rear Idx + 1 and increment Rear Idx.

    If  Rear Idx == Front Idx, there are no elements.
   */
  private int   front = 0;
  private int   rear  = 0;

  public RingBufferQueue(int capacity) {
    /*
    Required +1 to capacity for managing pointers.
     */
    memory = new int[capacity + 1];
  }

  public boolean add(Integer val) {
    int next =
        front == memory.length - 1
            ?
            0
            :
            front + 1;

    if (next == rear) {
      return false;
    }

    memory[front] = val;
    front = next;

    return true;
  }

  public Integer poll() {
    if (rear == front) {
      return null;
    }

    int next =
        rear == memory.length - 1
            ?
            0
            :
            rear + 1;

    int dequeued = memory[rear];
    rear = next;

    return dequeued;
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public int size() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public boolean isEmpty() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public boolean contains(Object o) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public Iterator<Integer> iterator() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public Object[] toArray() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public <T> T[] toArray(T[] a) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public boolean addAll(Collection<? extends Integer> c) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public boolean offer(Integer integer) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public Integer remove() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public Integer element() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }

  @Override
  public Integer peek() {
    throw new UnsupportedOperationException(NOT_SUPPORTED_YET_MESSAGE);
  }
}

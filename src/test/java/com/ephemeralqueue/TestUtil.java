package com.ephemeralqueue;

import java.util.List;

public class TestUtil {

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
}

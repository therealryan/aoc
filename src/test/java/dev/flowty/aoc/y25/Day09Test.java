package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09Test {

  public static final String[] TEST_DATA = Input.linesOf(
    "7,1\n" +
      "11,1\n" +
      "11,7\n" +
      "9,7\n" +
      "9,5\n" +
      "2,5\n" +
      "2,3\n" +
      "7,3\n");

  private static final String[] DATA = Input.linesFrom("y25", "input_09.txt");

  @Test
  void part1Test() {
    assertEquals(50, Day09.part1( TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(4763040296L, Day09.part1( DATA));
  }

  @Test
  void part2Test() {
    assertEquals(24, Day09.part2(TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(1396494456, Day09.part2(DATA));
  }
}
package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Test {

  public static final String[] TEST_DATA = Input.linesOf(
    "3-5\n" +
      "10-14\n" +
      "16-20\n" +
      "12-18\n" +
      "\n" +
      "1\n" +
      "5\n" +
      "8\n" +
      "11\n" +
      "17\n" +
      "32");

  private static final String[] DATA = Input.linesFrom("y25", "input_05.txt");

  @Test
  void part1Test() {
    assertEquals(3, Day05.part1(TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(509, Day05.part1(DATA));
  }

  @Test
  void part2Test() {
    assertEquals(14, Day05.part2(TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(336790092076620l, Day05.part2(DATA));
  }
}
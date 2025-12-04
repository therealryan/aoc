package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day04Test {

  public static final String[] TEST_DATA = Input.linesOf(
    "..@@.@@@@.\n" +
      "@@@.@.@.@@\n" +
      "@@@@@.@.@@\n" +
      "@.@@@@..@.\n" +
      "@@.@@@@.@@\n" +
      ".@@@@@@@.@\n" +
      ".@.@.@.@@@\n" +
      "@.@@@.@@@@\n" +
      ".@@@@@@@@.\n" +
      "@.@.@@@.@.");

  private static final String[] DATA = Input.linesFrom("y25", "input_04.txt");

  @Test
  void part1Test() {
    assertEquals(13, Day04.part1(TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(1569, Day04.part1(DATA));
  }

  @Test
  void part2Test() {
    assertEquals(43, Day04.part2(TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(9280, Day04.part2(DATA));
  }
}
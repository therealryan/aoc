package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day07Test {

  public static final String[] TEST_DATA = Input.linesOf(
    ".......S.......\n" +
      "...............\n" +
      ".......^.......\n" +
      "...............\n" +
      "......^.^......\n" +
      "...............\n" +
      ".....^.^.^.....\n" +
      "...............\n" +
      "....^.^...^....\n" +
      "...............\n" +
      "...^.^...^.^...\n" +
      "...............\n" +
      "..^...^.....^..\n" +
      "...............\n" +
      ".^.^.^.^.^...^.\n" +
      "...............");

  private static final String[] DATA = Input.linesFrom("y25", "input_07.txt");

  @Test
  void part1Test() {
    assertEquals(21, Day07.part1(TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(1690, Day07.part1(DATA));
  }

  @Test
  void part2Test() {
    assertEquals(40, Day07.part2(TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(221371496188107L, Day07.part2(DATA));
  }
}
package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

  public static final String[] TEST_DATA = Input.linesOf(
    "aaa: you hhh\n" +
      "you: bbb ccc\n" +
      "bbb: ddd eee\n" +
      "ccc: ddd eee fff\n" +
      "ddd: ggg\n" +
      "eee: out\n" +
      "fff: out\n" +
      "ggg: out\n" +
      "hhh: ccc fff iii\n" +
      "iii: out");

  private static final String[] DATA = Input.linesFrom("y25", "input_11.txt");

  @Test
  void part1Test() {
    assertEquals(5, Day11.part1(TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(788, Day11.part1(DATA));
  }

  @Test
  void part2Test() {
    assertEquals(0, Day11.part2(TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(0, Day11.part2(DATA));
  }
}
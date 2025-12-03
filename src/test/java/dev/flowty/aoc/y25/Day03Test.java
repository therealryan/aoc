package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03Test {

  public static final String[] TEST_DATA = Input.linesOf(
    "987654321111111\n" +
      "811111111111119\n" +
      "234234234234278\n" +
      "818181911112111");

  private static final String[] DATA = Input.linesFrom("y25", "input_03.txt");

  @Test
  void part1Test() {
    assertEquals(357, Day03.part1(TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(17158, Day03.part1(DATA));
  }

  @Test
  void part2Test() {
    assertEquals(3121910778619l, Day03.part2(TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(170449335646486l, Day03.part2(DATA));
  }
}
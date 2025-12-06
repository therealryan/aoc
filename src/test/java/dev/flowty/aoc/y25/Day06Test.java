package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day06Test {

  public static final String[] TEST_DATA = Input.linesOf(
    "123 328  51 64 \n" +
      " 45 64  387 23 \n" +
      "  6 98  215 314\n" +
      "*   +   *   +  ");

  private static final String[] DATA = Input.linesFrom("y25", "input_06.txt");

  @Test
  void part1Test() {
    assertEquals(4277556, Day06.part1(TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(6725216329103L, Day06.part1(DATA));
  }

  @Test
  void part2Test() {
    assertEquals(3263827, Day06.part2(TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(10600728112865L, Day06.part2(DATA));
  }
}
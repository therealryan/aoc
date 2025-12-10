package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {

  public static final String[] TEST_DATA = Input.linesOf(
    "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}\n" +
      "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}\n" +
      "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}");

  private static final String[] DATA = Input.linesFrom("y25", "input_10.txt");

  @Test
  void part1Test() {
    assertEquals(7, Day10.part1(TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(514, Day10.part1(DATA));
  }

  @Test
  void part2Test() {
    assertEquals(0, Day10.part2(TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(0, Day10.part2(DATA));
  }
}
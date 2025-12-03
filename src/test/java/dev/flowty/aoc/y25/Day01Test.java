package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day01Test {

  public static final String[] TEST_DATA = Input.linesOf("L68\n" +
    "L30\n" +
    "R48\n" +
    "L5\n" +
    "R60\n" +
    "L55\n" +
    "L1\n" +
    "L99\n" +
    "R14\n" +
    "L82");

  private static final String[] DATA = Input.linesFrom("y25", "input_01.txt");

  @Test
  void part1Test() {
    assertEquals(3, Day01.part1(TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(1089, Day01.part1(DATA));
  }

  @Test
  void part2Test() {
    assertEquals(6, Day01.part2(TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(6530, Day01.part2(DATA));
  }
}
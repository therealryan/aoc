package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day02Test {

  public static final String TEST_DATA = Input.linesOf(
    "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
      "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
      "824824821-824824827,2121212118-2121212124")[0];

  private static final String DATA = Input.linesFrom("y25", "input_02.txt")[0];

  @Test
  void part1Test() {
    assertEquals(1227775554, Day02.part1(TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(22062284697l, Day02.part1(DATA));
  }

  @Test
  void part2Test() {
    assertEquals(4174379265l, Day02.part2(TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(46666175279l, Day02.part2(DATA));
  }
}
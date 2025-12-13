package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

  public static final String[] PART_1_TEST_DATA = Input.linesOf(
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
    assertEquals(5, Day11.part1(PART_1_TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(788, Day11.part1(DATA));
  }

  public static final String[] PART_2_TEST_DATA = Input.linesOf(
    "svr: aaa bbb\n" +
      "aaa: fft\n" +
      "fft: ccc\n" +
      "bbb: tty\n" +
      "tty: ccc\n" +
      "ccc: ddd eee\n" +
      "ddd: hub\n" +
      "hub: fff\n" +
      "eee: dac\n" +
      "dac: fff\n" +
      "fff: ggg hhh\n" +
      "ggg: out\n" +
      "hhh: out");

  @Test
  void part2Test() {
    assertEquals(2, Day11.part2(PART_2_TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(316291887968000L, Day11.part2(DATA));
  }
}
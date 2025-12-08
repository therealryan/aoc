package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test {

  public static final String[] TEST_DATA = Input.linesOf(
    "162,817,812\n" +
      "57,618,57\n" +
      "906,360,560\n" +
      "592,479,940\n" +
      "352,342,300\n" +
      "466,668,158\n" +
      "542,29,236\n" +
      "431,825,988\n" +
      "739,650,466\n" +
      "52,470,668\n" +
      "216,146,977\n" +
      "819,987,18\n" +
      "117,168,530\n" +
      "805,96,715\n" +
      "346,949,466\n" +
      "970,615,88\n" +
      "941,993,340\n" +
      "862,61,35\n" +
      "984,92,344\n" +
      "425,690,689");

  private static final String[] DATA = Input.linesFrom("y25", "input_08.txt");

  @Test
  void part1Test() {
    assertEquals(40, Day08.part1(10, TEST_DATA));
  }

  @Test
  void part1() {
    assertEquals(62186, Day08.part1(1000, DATA));
  }

  @Test
  void part2Test() {
    assertEquals(25272, Day08.part2(TEST_DATA));
  }

  @Test
  void part2() {
    assertEquals(8420405530L, Day08.part2(DATA));
  }
}
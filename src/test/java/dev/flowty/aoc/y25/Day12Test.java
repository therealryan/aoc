package dev.flowty.aoc.y25;

import dev.flowty.aoc.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

  public static final String[] TEST_DATA = Input.linesOf(
    "0:\n" +
      "###\n" +
      "##.\n" +
      "##.\n" +
      "\n" +
      "1:\n" +
      "###\n" +
      "##.\n" +
      ".##\n" +
      "\n" +
      "2:\n" +
      ".##\n" +
      "###\n" +
      "##.\n" +
      "\n" +
      "3:\n" +
      "##.\n" +
      "###\n" +
      "##.\n" +
      "\n" +
      "4:\n" +
      "###\n" +
      "#..\n" +
      "###\n" +
      "\n" +
      "5:\n" +
      "###\n" +
      ".#.\n" +
      "###\n" +
      "\n" +
      "4x4: 0 0 0 0 2 0\n" +
      "12x5: 1 0 1 0 2 2\n" +
      "12x5: 1 0 1 0 3 2");

  public static final String[] MIN_FIT = Input.linesOf(
    "0:\n" +
      "###\n" +
      "##.\n" +
      "##.\n" +
      "\n" +
      "3x3: 1");

  private static final String[] DATA = Input.linesFrom("y25", "input_12.txt");

  @Test
  void part1Test() {
    assertEquals(1, Day12.part1(MIN_FIT));
  }

  @Test
  void part1() {
    assertEquals(536, Day12.part1(DATA));
  }
}
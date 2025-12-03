package dev.flowty.aoc.y25;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Day03 {

  static long part1(String... lines) {
    return maxJolt(2, lines);
  }

  static long part2(String... lines) {
    return maxJolt(12, lines);
  }

  private static long maxJolt(int batteries, String... lines) {
    long jolt = 0;

    for (String line : lines) {
      int[] bp = new int[batteries];
      int last = -1;
      for (int i = 0; i < bp.length; i++) {
        last = largestPosition(line, last + 1, line.length() - batteries + i+1);
        bp[i] = last;
      }

      String joltString = IntStream.of(bp)
        .mapToObj(p -> String.valueOf(line.charAt(p)))
        .collect(Collectors.joining());
      jolt += Long.parseLong(joltString);
    }

    return jolt;
  }

  private static int largestPosition(String bank, int start, int end) {
    int pos = start;
    char largest = bank.charAt(start);
    for (int i = start; i < end; i++) {
      if (largest < bank.charAt(i)) {
        largest = bank.charAt(i);
        pos = i;
      }
    }
    return pos;
  }
}

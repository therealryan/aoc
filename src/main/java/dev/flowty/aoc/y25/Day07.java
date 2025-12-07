package dev.flowty.aoc.y25;

import java.util.Map;
import java.util.TreeMap;

public class Day07 {

  static long part1(String... lines) {
    return simulate(lines).splits();
  }

  static long part2(String... lines) {
    return simulate(lines).timelines();
  }

  private static Result simulate(String... lines) {
    Map<Integer, Long> beams = new TreeMap<>();
    int splits = 0;
    for (String line : lines) {
      for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
        if (c == '.') {
          // nowt
        } else if (c == 'S') {
          beams.put(i, 1L);
        } else if (c == '^') {
          Long split = beams.remove(i);
          if (split != null && split > 0) {
            beams.merge(i - 1, split, Long::sum);
            beams.merge(i + 1, split, Long::sum);
            splits++;
          }
        } else {
          throw new IllegalArgumentException(line + "@" + i + "='" + c + "'");
        }
      }
    }
    return new Result(
      splits,
      beams.values().stream().reduce(0L, Long::sum));
  }

  private record Result(long splits, long timelines) {
  }
}

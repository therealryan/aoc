package dev.flowty.aoc.y25;

import java.util.function.LongPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

class Day02 {

  static long part1(String line) {
    return sumInvalid(line, Day02::isInvalidPart1);
  }

  static long part2(String line) {
    return sumInvalid(line, Day02::isInvalidPart2);
  }

  static long sumInvalid(String line, LongPredicate test) {

    long count = 0;

    Pattern input = Pattern.compile("(\\d+)-(\\d+)");
    for (String range : line.split(",")) {
      Matcher m = input.matcher(range);
      if (!m.matches()) {
        throw new IllegalArgumentException(range);
      }

      long low = Long.parseLong(m.group(1));
      long high = Long.parseLong(m.group(2));

      if (low > high) {
        throw new IllegalArgumentException(range);
      }

      count += LongStream.rangeClosed(low, high)
        .filter(test)
        .sum();
    }

    return count;
  }

  private static boolean isInvalidPart1(long id) {
    String ids = String.valueOf(id);
    if (ids.length() % 2 == 1) {
      return false;
    }
    int mid = ids.length() / 2;
    return ids.substring(0, mid).equals(ids.substring(mid));
  }

  private static boolean isInvalidPart2(long id) {
    String ids = String.valueOf(id);
    for (int l = 1; l <= ids.length() / 2; l++) {
      String t = ids.substring(0, l);
      String rep = t.repeat(ids.length() / l);
      if (rep.equals(ids)) {
        return true;
      }
    }
    return false;
  }
}

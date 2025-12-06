package dev.flowty.aoc.y25;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

public class Day05 {

  static long part1(String... lines) {
    List<String> rangeDefs = new ArrayList<>();
    List<Long> ids = null;

    for (String line : lines) {
      if (line.isEmpty()) {
        ids = new ArrayList<>();
      } else {
        if (ids == null) {
          rangeDefs.add(line);
        } else {
          ids.add(Long.parseLong(line));
        }
      }
    }

    Ranges ranges = new Ranges(rangeDefs);
    return ids.stream()
      .filter(ranges::contains)
      .count();
  }

  static long part2(String... lines) {
    List<String> rangeDefs = new ArrayList<>();
    List<Long> ids = null;

    for (String line : lines) {
      if (line.isEmpty()) {
        ids = new ArrayList<>();
      } else {
        if (ids == null) {
          rangeDefs.add(line);
        } else {
          ids.add(Long.parseLong(line));
        }
      }
    }

    Ranges ranges = new Ranges(rangeDefs);
    return ranges.size();
  }

  private static class Ranges {
    private Range[] ranges;

    private Ranges(List<String> input) {
      Deque<Range> raw = input.stream()
        .map(Range::parse)
        .sorted(Range.ORDER)
        .collect(toCollection(ArrayDeque::new));

      Range current = raw.removeFirst();
      List<Range> merged = new ArrayList<>();
      while (!raw.isEmpty()) {
        Range r = raw.removeFirst();
        if (current.intersects(r)) {
          current = current.merge(r);
        } else {
          merged.add(current);
          current = r;
        }
      }
      merged.add(current);

      ranges = merged.toArray(Range[]::new);
    }

    boolean contains(long value) {
      return Stream.of(ranges).anyMatch(r -> r.contains(value));
    }

    long size(){
      return Stream.of(ranges).mapToLong(Range::size).sum();
    }
  }

  private static record Range(long low, long high) {
    static Comparator<Range> ORDER = Comparator
      .comparing(Range::low)
      .thenComparing(Range::high);
    private static Pattern INPUT = Pattern.compile("(\\d+)-(\\d+)");

    public static Range parse(String input) {
      Matcher m = INPUT.matcher(input);
      if (!m.matches()) {
        throw new IllegalArgumentException(input);
      }
      return new Range(Long.parseLong(m.group(1)), Long.parseLong(m.group(2)));
    }

    boolean contains(long value) {
      return low <= value && value <= high;
    }

    boolean intersects(Range other) {
      boolean lower = other.high < low;
      boolean higher = high < other.low;
      return !(lower || higher);
    }

    Range merge(Range other) {
      return new Range(Math.min(low, other.low), Math.max(high, other.high));
    }

    long size() {
      return high - low + 1;
    }
  }
}

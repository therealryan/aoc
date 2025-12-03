package dev.flowty.aoc.y25;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Day01 {

  static int part1(String[] lines) {
    int zeroes = 0;
    int position = 50;

    Pattern input = Pattern.compile("([LR])(\\d+)");
    for (String line : lines) {
      Matcher m = input.matcher(line);
      if (!m.matches()) {
        throw new IllegalArgumentException(line);
      }
      boolean left = "L".equals(m.group(1));
      int count = Integer.parseInt(m.group(2));

      position += (left ? -1 : 1) * count;
      while (position < 0) {
        position += 100;
      }
      while (position > 99) {
        position -= 100;
      }
      if (position == 0) {
        zeroes++;
      }
    }

    return zeroes;
  }

  static int part2(String[] lines) {
    int zeroes = 0;
    int position = 50;

    Pattern input = Pattern.compile("([LR])(\\d+)");
    for (String line : lines) {
      Matcher m = input.matcher(line);
      if (!m.matches()) {
        throw new IllegalArgumentException(line);
      }
      boolean left = "L".equals(m.group(1));
      int count = Integer.parseInt(m.group(2));

      for (int i = 0; i < count; i++) {
        position += (left ? -1 : 1) * 1;
        while (position < 0) {
          position += 100;
        }
        while (position > 99) {
          position -= 100;
        }
        if (position == 0) {
          zeroes++;
        }
      }
    }

    return zeroes;
  }
}

package dev.flowty.aoc.y25;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongBinaryOperator;

public class Day06 {

  static long part1(String... lines) {
    List<Sum> sums = new ArrayList<>();
    for (String line : lines) {
      String[] tokens = line.trim().split(" +");
      for (int i = 0; i < tokens.length; i++) {
        while (sums.size() < i + 1) {
          sums.add(new Sum());
        }
        sums.get(i).with(tokens[i]);
      }
    }
    return sums.stream()
      .mapToLong(Sum::compute)
      .sum();
  }

  static long part2(String... lines) {
    long total = 0;
    Sum sum = new Sum();
    for (int x = lines[0].length() - 1; x >= 0; x--) {
      StringBuilder num = new StringBuilder();
      for (int y = 0; y < lines.length; y++) {
        if (lines[y].isEmpty()) {
          // last empty line
        } else {
          char c = lines[y].charAt(x);
          if (Character.isDigit(c)) {
            num.append(c);
          } else if (!Character.isSpaceChar(c)) {
            total += sum
              .with(num.toString())
              .with(String.valueOf(c))
              .compute();
            sum = new Sum();
            num = new StringBuilder();
          }
        }
      }
      sum.with(num.toString());
    }
    return total;
  }

  private static class Sum {
    private final List<Integer> numbers = new ArrayList<>();
    private Operator operator;

    public Sum with(String token) {
      if (!token.isEmpty()) {
        if (token.matches("\\d+")) {
          numbers.add(Integer.parseInt(token));
        } else {
          operator = Operator.forSign(token);
        }
      }
      return this;
    }

    public long compute() {
      return numbers.stream().mapToLong(Integer::longValue)
        .reduce(operator.identity, operator);
    }

    private enum Operator implements LongBinaryOperator {
      ADD(0, "+") {
        @Override
        public long applyAsLong(long left, long right) {
          return left + right;
        }
      },
      MULTIPLY(1, "*") {
        @Override
        public long applyAsLong(long left, long right) {
          return left * right;
        }
      },
      ;
      public final long identity;
      public final String sign;

      Operator(long identity, String sign) {
        this.identity = identity;
        this.sign = sign;
      }

      static Operator forSign(String sign) {
        for (Operator op : values()) {
          if (op.sign.equals(sign)) {
            return op;
          }
        }
        throw new IllegalArgumentException("'" + sign + "'");
      }

    }
  }
}

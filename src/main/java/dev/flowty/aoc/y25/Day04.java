package dev.flowty.aoc.y25;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Day04 {

  static long part1(String... lines) {
    return new Grid(lines).accessible().size();
  }

  static long part2(String... lines) {
    return new Grid(lines).removeable();
  }

  private static class Grid {
    private final List<List<Boolean>> grid = new ArrayList<>();

    public Grid(String... lines) {
      for (String line : lines) {
        List<Boolean> row = new ArrayList<>();
        for (String ch : line.split("")) {
          row.add(ch.equals("@"));
        }
        grid.add(row);
      }
    }

    private int removeable() {
      int count = 0;
      List<Coord> accessible;
      while (!(accessible = accessible()).isEmpty()) {
        count += accessible.size();
        remove(accessible);
      }
      return count;
    }

    private void remove(List<Coord> coords) {
      for (Coord c : coords) {
        grid.get(c.x()).set(c.y, false);
      }
    }

    private List<Coord> accessible() {
      List<Coord> result = new ArrayList<>();
      for (int x = 0; x < grid.get(0).size(); x++) {
        for (int y = 0; y < grid.size(); y++) {
          if (rollAt(x, y) && surrounding(x, y) < 4) {
            result.add(new Coord(x, y));
          }
        }
      }
      return result;
    }

    private int surrounding(int x, int y) {
      int count = 0;
      for (int h = -1; h <= 1; h++) {
        for (int v = -1; v <= 1; v++) {
          if (h == 0 && v == 0) {
            // self
          } else {
            count += rollAt(x + h, y + v) ? 1 : 0;
          }
        }
      }
      return count;
    }

    private boolean rollAt(int x, int y) {
      if (x < 0 || x >= grid.get(0).size()) {
        return false;
      }
      if (y < 0 || y >= grid.size()) {
        return false;
      }
      return grid.get(x).get(y);
    }

    @Override
    public String toString() {
      return grid.stream()
        .map(row -> row.stream()
          .map(b -> b ? "@" : ".")
          .collect(Collectors.joining()))
        .collect(Collectors.joining("\n"));
    }
  }

  private record Coord(int x, int y){}

}

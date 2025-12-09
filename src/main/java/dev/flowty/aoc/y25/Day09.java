package dev.flowty.aoc.y25;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 {

  static long part1(String... lines) {
    List<Tile> tiles = parse(lines);

    long largest = -1;
    for (int i = 0; i < tiles.size(); i++) {
      for (int j = i + 1; j < tiles.size(); j++) {
        long area = tiles.get(i).area(tiles.get(j));
        if (area > largest) {
          largest = area;
        }
      }
    }

    return largest;
  }

  static long part2(String... lines) {
    List<Tile> tiles = parse(lines);

    Rectangle largest = new Rectangle(
      tiles.get(0).min(tiles.get(0)),
      tiles.get(0).max(tiles.get(0)));
    for (int i = 0; i < tiles.size(); i++) {
      for (int j = i + 1; j < tiles.size(); j++) {
        Rectangle r = new Rectangle(
          tiles.get(i).min(tiles.get(j)),
          tiles.get(i).max(tiles.get(j)));

        if (r.area() > largest.area() && !r.intersectsWalk(tiles)) {
          largest = r;
        }
      }
    }

    return largest.area();
  }

  private static List<Tile> parse(String[] lines) {
    List<Tile> tiles = new ArrayList<>();
    Pattern input = Pattern.compile("(\\d+),(\\d+)");
    for (String line : lines) {
      Matcher m = input.matcher(line);
      if (!m.matches()) {
        throw new IllegalArgumentException(line);
      }
      tiles.add(new Tile(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));
    }
    return tiles;
  }

  private record Tile(int x, int y) {
    public long area(Tile other) {
      long dx = Math.abs(x - other.x) + 1;
      long dy = Math.abs(y - other.y) + 1;
      return dx * dy;
    }

    public Tile min(Tile other) {
      return new Tile(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Tile max(Tile other) {
      return new Tile(Math.max(x, other.x), Math.max(y, other.y));
    }
  }

  private record Rectangle(Tile min, Tile max) {
    public long area() {
      return min.area(max);
    }

    public boolean intersectsWalk(List<Tile> walk) {
      Tile previous = walk.get(walk.size() - 1);
      for (Tile c : walk) {
        if (intersects(previous, c)) {
          return true;
        }
        previous = c;
      }
      return false;
    }

    public boolean intersects(Tile from, Tile to) {
      boolean toLeft = Math.max(from.x(), to.x()) <= min.x();
      boolean toRight = Math.min(from.x(), to.x()) >= max.x();
      boolean below = Math.max(from.y(), to.y()) <= min.y();
      boolean above = Math.min(from.y(), to.y()) >= max.y();
      return !toLeft && !toRight && !above && !below;
    }
  }
}

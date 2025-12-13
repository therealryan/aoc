package dev.flowty.aoc.y25;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Day12 {

  static long part1(String... lines) {
    return new Packing(lines).fitting();
  }

  private static class Packing {
    private List<Shape> shapes = new ArrayList<>();
    private List<Space> spaces = new ArrayList<>();

    public Packing(String... lines) {
      List<String> shape = new ArrayList<>();
      for (String line : lines) {
        if (line.isEmpty() || line.matches("\\d+:")) {
          // shape index or empty
          if (!shape.isEmpty()) {
            shapes.add(new Shape(shape));
            shape.clear();
          }
        } else if (line.matches("[#.]+")) {
          shape.add(line);
        } else if (line.matches(Space.INPUT.pattern())) {
          spaces.add(new Space(line));
        } else {
          throw new IllegalArgumentException("'" + line + "'");
        }
      }
    }

    public int fitting() {
      return (int) spaces.stream()
        .filter(s -> s.canFit(shapes))
        .count();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < shapes.size(); i++) {
        sb.append(i).append(":\n");
        sb.append(shapes.get(i)).append("\n");
      }
      for (Space space : spaces) {
        sb.append(space).append("\n");
      }
      return sb.toString();
    }
  }

  private static class Shape {
    private final boolean[][] occupied;

    private final int width;
    private final int height;

    public Shape(List<String> lines) {
      occupied = new boolean[lines.size()][];
      for (int i = 0; i < occupied.length; i++) {
        String line = lines.get(i);
        occupied[i] = new boolean[line.length()];
        for (int j = 0; j < line.length(); j++) {
          occupied[i][j] = line.charAt(j) == '#';
        }
      }

      height = occupied.length;
      width = Stream.of(occupied)
        .mapToInt(a -> a.length)
        .max()
        .orElseThrow();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (boolean[] line : occupied) {
        for (boolean b : line) {
          sb.append(b ? '#' : '.');
        }
        sb.append("\n");
      }
      return sb.toString();
    }
  }

  private static class Space {
    public static final Pattern INPUT = Pattern.compile("(\\d+)x(\\d+):(( \\d+)+)");
    private final int width;
    private final int height;
    private Map<Integer, Integer> shapeCounts = new TreeMap<>();

    public Space(String def) {
      Matcher m = INPUT.matcher(def);
      if (!m.matches()) {
        throw new IllegalArgumentException(def);
      }
      width = Integer.parseInt(m.group(1));
      height = Integer.parseInt(m.group(2));
      String[] cts = m.group(3).trim().split(" ");
      for (int i = 0; i < cts.length; i++) {
        shapeCounts.put(i, Integer.parseInt(cts[i]));
      }
    }

    public boolean canFit(List<Shape> shapeSelection) {
      // assemble shape list
      List<Shape> toPack = new ArrayList<>();
      shapeCounts.forEach((idx, count) -> IntStream.range(0, count)
        .forEach(i -> toPack.add(shapeSelection.get(idx))));

      // check all have the same square bounds
      Set<Integer> dims = toPack.stream()
        .flatMap(s -> Stream.of(s.width, s.height))
        .collect(toSet());
      if (dims.size() != 1) {
        throw new IllegalArgumentException(dims.toString());
      }

      int dim = dims.iterator().next();
      int rows = height / dim;
      int columns = width / dim;

      return toPack.size() <= rows * columns;
    }

    @Override
    public String toString() {
      return String.format("%sx%s: %s",
        width, height, shapeCounts.values().stream()
          .map(String::valueOf)
          .collect(Collectors.joining(" ")));
    }
  }
}

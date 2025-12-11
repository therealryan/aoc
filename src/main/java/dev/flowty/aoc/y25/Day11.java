package dev.flowty.aoc.y25;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

public class Day11 {

  static long part1(String... lines) {
    return new Graph(lines).pathsBetween("you", "out");
  }

  static long part2(String... lines) {
    return 0;
  }

  private static class Graph {
    private final Map<String, Set<String>> edges = new TreeMap<>();

    public Graph(String... lines) {
      for (String line : lines) {
        Matcher m = Pattern.compile("([a-z]{3}):(( [a-z]{3})+)").matcher(line);
        if (!m.matches()) {
          throw new IllegalArgumentException(line);
        }

        String source = m.group(1);
        for (String sink : m.group(2).split(" ")) {
          edges.computeIfAbsent(source, s -> new TreeSet<>()).add(sink);
        }
      }
    }

    public int pathsBetween(String source, String sink) {
      Map<String, Set<Path>> paths = new HashMap<>();
      Deque<Path> pending = new ArrayDeque<>();
      edges.getOrDefault(source, Collections.emptySet())
        .forEach(n -> pending.add(new Path(n)));

      while (!pending.isEmpty()) {
        Path path = pending.removeFirst();
        paths.computeIfAbsent(path.last(), n -> new HashSet<>()).add(path);
        edges.getOrDefault(path.last(), Collections.emptySet())
          .forEach(n -> pending.add(path.then(n)));
      }

      return paths.get(sink).size();
    }

    @Override
    public String toString() {
      return edges.entrySet().stream()
        .map(e -> e.getKey() + ": " + e.getValue().stream()
          .collect(joining(" ")))
        .collect(joining("\n"));
    }
  }

  private static class Path {
    private final List<String> nodes = new ArrayList<>();

    public Path(String node) {
      nodes.add(node);
    }

    private Path(Path parent) {
      nodes.addAll(parent.nodes);
    }

    public String last() {
      return nodes.get(nodes.size() - 1);
    }

    public Path then(String node) {
      Path p = new Path(this);
      p.nodes.add(node);
      return p;
    }
  }
}

package dev.flowty.aoc.y25;

import java.util.Collections;
import java.util.HashMap;
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
    Graph graph = new Graph(lines);

    return (
      graph.pathsBetween("svr", "fft")
        * graph.pathsBetween("fft", "dac")
        * graph.pathsBetween("dac", "out"))
      + (
      graph.pathsBetween("svr", "dac")
        * graph.pathsBetween("dac", "fft")
        * graph.pathsBetween("fft", "out"));
  }

  private static class Graph {
    private final Map<String, Set<String>> edges = new TreeMap<>();
    private final Map<String, Map<String, Long>> pathsFromTo = new HashMap<>();

    public Graph(String... lines) {
      for (String line : lines) {
        Matcher m = Pattern.compile("([a-z]{3}):(( [a-z]{3})+)").matcher(line);
        if (!m.matches()) {
          throw new IllegalArgumentException(line);
        }

        String source = m.group(1);
        if (source.isEmpty()) {
          throw new IllegalArgumentException(line);
        }
        for (String sink : m.group(2).trim().split(" ")) {
          if (sink.isEmpty()) {
            throw new IllegalArgumentException(line);
          }
          edges.computeIfAbsent(source, s -> new TreeSet<>()).add(sink);
        }
      }
    }

    public long pathsBetween(String source, String sink) {
      return pathsFrom(source).getOrDefault(sink, 0L);
    }

    public Map<String, Long> pathsFrom(String source) {

      if (!pathsFromTo.containsKey(source)) {
        Map<String, Long> pathsTo = new HashMap<>();
        for (String child : edges.getOrDefault(source, Collections.emptySet())) {
          pathsTo.put(child, 1L);
          pathsFrom(child)
            .forEach((g, c) -> pathsTo.merge(g, c, Long::sum));
        }
        pathsFromTo.put(source, pathsTo);
      }
      return pathsFromTo.get(source);
    }

    @Override
    public String toString() {
      return edges.entrySet().stream()
        .map(e -> e.getKey() + ": " + String.join(" ", e.getValue()))
        .collect(joining("\n"));
    }
  }
}

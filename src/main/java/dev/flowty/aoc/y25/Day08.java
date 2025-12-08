package dev.flowty.aoc.y25;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day08 {

  static long part1(int connections, String... lines) {
    return new Circuits(lines)
      .makeShortestConnections(connections)
      .largestProduct(3);
  }

  static long part2(String... lines) {
    Link link = new Circuits(lines).completelyConnect();
    return link.from.x * link.to.x;
  }

  static class Circuits {
    private final Set<Circuit> circuits = new HashSet<>();
    private final Map<JunctionBox, Circuit> membership = new HashMap<>();
    private final List<Link> links = new ArrayList<>();

    Circuits(String... lines) {
      Pattern input = Pattern.compile("(\\d+),(\\d+),(\\d+)");
      List<JunctionBox> boxen = new ArrayList<>();
      for (String line : lines) {
        Matcher m = input.matcher(line);
        if (!m.matches()) {
          throw new IllegalArgumentException(line);
        }
        JunctionBox box = new JunctionBox(
          Long.parseLong(m.group(1)),
          Long.parseLong(m.group(2)),
          Long.parseLong(m.group(3)));
        boxen.add(box);
        Circuit c = new Circuit(box);
        circuits.add(c);
        membership.put(box, c);
      }

      for (int i = 0; i < boxen.size(); i++) {
        for (int j = i + 1; j < boxen.size(); j++) {
          links.add(new Link(boxen.get(i), boxen.get(j)));
        }
      }
      Collections.sort(links, Comparator.comparing(Link::distanceSq));
    }

    Circuits makeShortestConnections(int count) {
      for (Link l : links) {
        connect(l);
        count--;
        if (count == 0) {
          return this;
        }
      }
      throw new IllegalArgumentException();
    }

    Link completelyConnect() {
      for (Link l : links) {
        connect(l);
        if (circuits.size() == 1) {
          return l;
        }
      }
      throw new IllegalStateException();
    }

    private void connect(Link l) {
      Circuit src = membership.get(l.from);
      Circuit sink = membership.get(l.to);
      circuits.remove(src);
      circuits.remove(sink);
      Circuit combined = new Circuit(src, sink);
      circuits.add(combined);
      combined.members.forEach(m -> membership.put(m, combined));
    }

    long largestProduct(int count) {
      Deque<Integer> sizes = circuits.stream()
        .map(Circuit::size)
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toCollection(ArrayDeque::new));
      long product = 1;
      for (int i = 0; i < count; i++) {
        product *= sizes.removeFirst();
      }
      return product;
    }
  }

  private static class Circuit {
    private final Set<JunctionBox> members = new HashSet<>();

    Circuit(JunctionBox box) {
      members.add(box);
    }

    Circuit(Circuit a, Circuit b) {
      members.addAll(a.members);
      members.addAll(b.members);
    }

    public int size() {
      return members.size();
    }
  }

  record JunctionBox(long x, long y, long z) {
  }

  record Link(JunctionBox from, JunctionBox to) {
    long distanceSq() {
      long dx = from.x - to.x;
      long dy = from.y - to.y;
      long dz = from.z - to.z;
      return (dx * dx) + (dy * dy) + (dz * dz);
    }
  }
}

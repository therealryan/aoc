package dev.flowty.aoc.y25;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class Day10 {

  static long part1(String... lines) {
    int sum = 0;
    for (String line : lines) {
      Machine m = new Machine(line);
      sum += m.shortestPathToDesiredLights();
    }
    return sum;
  }

  static long part2(String... lines) {
    return 0; // lol no, this is some maths shit
  }

  private static class Machine {
    private final int lights;
    private final BitSet desired = new BitSet();
    private final List<Button> buttons = new ArrayList<>();
    private final List<Integer> joltages = new ArrayList<>();

    Machine(String def) {
      Matcher dm = Pattern.compile("\\[([#.]+)]").matcher(def);
      if (!dm.find()) {
        throw new IllegalArgumentException(def);
      }
      String dl = dm.group(1);
      lights = dl.length();
      for (int i = 0; i < dl.length(); i++) {
        if (dl.charAt(i) == '#') {
          desired.set(i);
        }
      }

      Matcher bm = Pattern.compile("\\(((\\d+,)*\\d+)\\)").matcher(def);
      while (bm.find()) {
        buttons.add(new Button(bm.group(1)));
      }
      if (buttons.isEmpty()) {
        throw new IllegalArgumentException(def);
      }

      Matcher jm = Pattern.compile("\\{((\\d+,)*\\d+)}").matcher(def);
      if (!jm.find()) {
        throw new IllegalArgumentException(def);
      }
      for (String j : jm.group(1).split(",")) {
        joltages.add(Integer.parseInt(j));
      }
    }

    public int shortestPathToDesiredLights() {
      Map<BitSet, List<Button>> shortest = new HashMap<>();
      BitSet start = new BitSet();
      shortest.put(start, Collections.emptyList());

      Deque<LightState> pending = new ArrayDeque<>();
      pending.add(new LightState(start, Collections.emptyList()));

      while (!shortest.containsKey(desired)) {
        exploreLights(shortest, pending);
      }

      return shortest.get(desired).size();
    }

    private void exploreLights(Map<BitSet, List<Button>> shortest, Deque<LightState> pending) {
      LightState state = pending.removeFirst();
      for (Button b : buttons) {
        List<Button> np = new ArrayList<>(state.path());
        np.add(b);
        BitSet ns = b.push(state.lights());

        List<Button> existing = shortest.get(ns);
        if (existing == null || existing.size() > np.size()) {
          shortest.put(ns, np);
          pending.add(new LightState(ns, np));
        }
      }
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("[");
      for (int i = 0; i < lights; i++) {
        sb.append(desired.get(i) ? '#' : '.');
      }
      sb.append("] ");
      buttons.stream().map(b -> b + " ").forEach(sb::append);
      sb.append("{")
        .append(joltages.stream()
          .map(String::valueOf)
          .collect(joining(",")))
        .append("}");

      return sb.toString();
    }
  }

  private static class Button {
    private final Set<Integer> lights;

    private Button(String lights) {
      this.lights = Stream.of(lights.split(","))
        .map(Integer::parseInt)
        .collect(Collectors.toCollection(TreeSet::new));
    }

    private BitSet push(BitSet current) {
      BitSet after = (BitSet) current.clone();
      for (Integer i : lights) {
        after.flip(i);
      }
      return after;
    }

    private List<Integer> push(List<Integer> joltages) {
      List<Integer> after = new ArrayList<>(joltages);
      for (Integer i : lights) {
        after.set(i, after.get(i) + 1);
      }
      return after;
    }

    @Override
    public String toString() {
      return lights.stream()
        .map(String::valueOf)
        .collect(joining(",", "(", ")"));
    }
  }

  private record LightState(BitSet lights, List<Button> path) {
  }
}

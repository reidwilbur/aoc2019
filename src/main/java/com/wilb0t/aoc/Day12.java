package com.wilb0t.aoc;

import com.google.common.collect.Sets;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.ArithmeticUtils;

public class Day12 {
  static class Moon {
    final String name;
    final int[] pos;
    final int[] vel;

    public Moon(String name, int x, int y, int z, int vx, int vy, int vz) {
      this.name = name;
      pos = new int[] {x, y, z};
      vel = new int[] {vx, vy, vz};
    }

    public Moon(Moon other) {
      this.name = other.name;
      pos = Arrays.copyOf(other.pos, other.pos.length);
      vel = Arrays.copyOf(other.vel, other.vel.length);
    }

    public void step() {
      pos[0] = pos[0] + vel[0];
      pos[1] += vel[1];
      pos[2] += vel[2];
    }

    void updateVel(Moon other) {
      vel[0] += Integer.compare(other.pos[0], pos[0]);
      vel[1] += Integer.compare(other.pos[1], pos[1]);
      vel[2] += Integer.compare(other.pos[2], pos[2]);
    }

    long potential() {
      return Math.abs(pos[0]) + Math.abs(pos[1]) + Math.abs(pos[2]);
    }

    long kinetic() {
      return Math.abs(vel[0]) + Math.abs(vel[1]) + Math.abs(vel[2]);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Moon)) return false;
      Moon moon = (Moon) o;
      return name.equals(moon.name) && Arrays.equals(pos, moon.pos) && Arrays.equals(vel, moon.vel);
    }

    @Override
    public int hashCode() {
      int result = Objects.hash(name);
      result = 31 * result + Arrays.hashCode(pos);
      result = 31 * result + Arrays.hashCode(vel);
      return result;
    }

    @Override
    public String toString() {
      return "Moon{"
          + "name='"
          + name
          + '\''
          + ", pos="
          + Arrays.toString(pos)
          + ", vel="
          + Arrays.toString(vel)
          + '}';
    }
  }

  static void simulate(Set<Moon> moons, int steps) {
    for (int idx = 0; idx < steps; idx++) {
      moons.forEach(
          moon -> {
            var others = Sets.difference(moons, Set.of(moon));
            others.forEach(moon::updateVel);
          });
      moons.forEach(Moon::step);
    }
  }

  static long totalEnergy(Set<Moon> moons) {
    return moons.stream().mapToLong(moon -> moon.kinetic() * moon.potential()).sum();
  }

  static long findCycle(Set<Moon> moons) {
    var startState =
        moons.stream()
            .map(moon -> new AbstractMap.SimpleEntry<>(moon.name, new Moon(moon)))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    var cycleSteps = new HashMap<Integer, Integer>();

    var steps = 1;
    while (cycleSteps.entrySet().size() != 3) {
      moons.forEach(
          moon -> {
            var others = Sets.difference(moons, Set.of(moon));
            others.forEach(moon::updateVel);
          });
      for (var moon : moons) {
        moon.step();
      }
      for (int idx = 0; idx < 3; idx ++) {
        if (isCycle(moons, startState, cycleSteps, idx)) {
          cycleSteps.put(idx, steps);
        }
      }
      steps += 1;
    }

    var distinctCycles = cycleSteps.values().stream().distinct().collect(Collectors.toList());
    return lcm(distinctCycles);
  }

  static boolean isCycle(
      Set<Moon> moons,
      Map<String, Moon> startState,
      Map<Integer, Integer> cycleSteps,
      int idx
  ) {
    return !cycleSteps.containsKey(idx)
        && moons.stream()
        .allMatch(
            moon ->
                startState.get(moon.name).pos[idx] == moon.pos[idx]
                    && startState.get(moon.name).vel[idx] == moon.vel[idx]);
  }

  static long lcm(List<Integer> cycles) {
    long lcm = cycles.get(0);
    for (int cycle : cycles.subList(1, cycles.size())) {
      lcm = (lcm * (long) cycle) / ArithmeticUtils.gcd(lcm, cycle);
    }
    return lcm;
  }
}

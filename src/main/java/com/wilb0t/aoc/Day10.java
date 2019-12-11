package com.wilb0t.aoc;

import com.google.common.collect.Sets;
import io.norberg.automatter.AutoMatter;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.ArithmeticUtils;

public class Day10 {

  @AutoMatter
  interface Asteroid {

    int x();

    int y();

    default int distance(Asteroid other) {
      var x = other.x() - x();
      var y = other.y() - y();
      return (x * x) + (y * y);
    }
  }

  @AutoMatter
  interface AsteroidView {

    int rise();

    int run();
  }

  static Set<Asteroid> toAsteroids(List<String> input) {
    var asteroids = new HashSet<Asteroid>();
    for (int y = 0; y < input.size(); y++) {
      var line = input.get(y);
      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '#') {
          asteroids.add(new AsteroidBuilder().x(x).y(y).build());
        }
      }
    }
    return asteroids;
  }

  static int getVisibleAsteroidCount(Asteroid base, Set<Asteroid> asteroids) {
    var bySlope = getAsteroidViewMap(base, asteroids);
    return bySlope.entrySet().size();
  }

  static Map.Entry<Integer, Asteroid> getLargestAsteroidCount(Set<Asteroid> asteroids) {
    return asteroids.stream()
        .map(base -> new AbstractMap.SimpleEntry<>(getVisibleAsteroidCount(base, asteroids), base))
        .max(Comparator.comparing(Map.Entry::getKey))
        .get();
  }

  static Map<AsteroidView, Deque<Asteroid>> getAsteroidViewMap(
      Asteroid base, Set<Asteroid> asteroids) {
    var toCheck = Sets.difference(asteroids, Set.of(base));

    return toCheck.stream()
        .map(
            asteroid -> {
              var run = asteroid.x() - base.x();
              var rise = asteroid.y() - base.y();
              var gcd = ArithmeticUtils.gcd(run, rise);
              return new AbstractMap.SimpleEntry<>(
                  new AsteroidViewBuilder().rise(rise / gcd).run(run / gcd).build(), asteroid);
            })
        .collect(
            Collectors.groupingBy(
                Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())))
        .entrySet()
        .stream()
        .map(
            e ->
                new AbstractMap.SimpleEntry<>(
                    e.getKey(),
                    new ArrayDeque<>(
                        e.getValue().stream()
                            .sorted(Comparator.comparing(base::distance))
                            .collect(Collectors.toList()))))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  static Asteroid getDestroyedAsteroid(Asteroid base, Set<Asteroid> asteroids, int count) {
    var bySlope = getAsteroidViewMap(base, asteroids);

    var keys =
        bySlope.keySet().stream()
            .sorted(Comparator.comparing(Day10::getAngle))
            .collect(Collectors.toList());

    //System.out.println(keys);
    Asteroid destroyed = null;
    for (int idx = 0; idx < count; idx++) {
      var key = keys.get(idx % keys.size());
      destroyed = bySlope.get(key).pop();
    }

    return destroyed;
  }

  static double getAngle(AsteroidView view) {
    var angle = (Math.PI/2) + Math.atan2(view.rise(),view.run());
    if (angle < 0) {
      angle = angle + (Math.PI * 2);
    }
    return angle;
  }
}

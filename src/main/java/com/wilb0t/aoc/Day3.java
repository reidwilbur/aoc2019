package com.wilb0t.aoc;

import com.google.common.collect.Sets;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day3 {

  static int getClosestIntersection(String wirePath1, String wirePath2) {
    var wirePoints1 = toPoints(wirePath1);
    var wirePoints2 = toPoints(wirePath2);

    var crosses = Sets.intersection(wirePoints1, wirePoints2);

    return crosses.stream()
        .map(point -> Math.abs(point.getKey()) + Math.abs(point.getValue()))
        .sorted()
        .findFirst()
        .get();
  }

  static Set<Map.Entry<Integer, Integer>> toPoints(String path) {
    Map.Entry<Integer, Integer> location = new AbstractMap.SimpleEntry<>(0, 0);
    var points = new HashSet<Map.Entry<Integer, Integer>>();
    for (var seg : path.split(",")) {
      var dir = seg.charAt(0);
      var len = Integer.parseInt(seg.substring(1));
      for (int l = 0; l < len; l++) {
        location = updateLocation(location, dir);
        points.add(location);
      }
    }
    return points;
  }

  static Map.Entry<Integer, Integer> updateLocation(
      Map.Entry<Integer, Integer> location, char dir) {
    if (dir == 'U') {
      return new AbstractMap.SimpleEntry<>(location.getKey(), location.getValue() + 1);
    } else if (dir == 'D') {
      return new AbstractMap.SimpleEntry<>(location.getKey(), location.getValue() - 1);
    } else if (dir == 'L') {
      return new AbstractMap.SimpleEntry<>(location.getKey() - 1, location.getValue());
    } else if (dir == 'R') {
      return new AbstractMap.SimpleEntry<>(location.getKey() + 1, location.getValue());
    }
    throw new RuntimeException("Unknown direction " + dir);
  }
}

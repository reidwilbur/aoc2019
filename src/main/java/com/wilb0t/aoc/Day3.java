package com.wilb0t.aoc;

import com.google.common.collect.Sets;
import io.norberg.automatter.AutoMatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day3 {

  @AutoMatter
  public interface Point {
    int x();

    int y();
  }

  static int getClosestIntersection(String wirePath1, String wirePath2) {
    var wirePoints1 = toPoints(wirePath1);
    var wirePoints2 = toPoints(wirePath2);

    var crosses = Sets.intersection(wirePoints1, wirePoints2);

    return crosses.stream()
        .map(point -> Math.abs(point.x()) + Math.abs(point.y()))
        .sorted()
        .findFirst()
        .get();
  }

  static int getShortestPathIntersection(String wirePath1, String wirePath2) {
    var wirePoints1 = toPointsWithPathLen(wirePath1);
    var wirePoints2 = toPointsWithPathLen(wirePath2);

    var crosses = Sets.intersection(wirePoints1.keySet(), wirePoints2.keySet());

    return crosses.stream()
        .map(p -> wirePoints1.get(p) + wirePoints2.get(p))
        .sorted()
        .findFirst()
        .get();
  }

  static Set<Point> toPoints(String path) {
    var location = new PointBuilder().x(0).y(0).build();
    var points = new HashSet<Point>();
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

  static Map<Point, Integer> toPointsWithPathLen(String path) {
    var location = new PointBuilder().x(0).y(0).build();
    var points = new HashMap<Point, Integer>();
    var pathLen = 0;
    for (var seg : path.split(",")) {
      var dir = seg.charAt(0);
      var len = Integer.parseInt(seg.substring(1));
      for (int l = 0; l < len; l++) {
        location = updateLocation(location, dir);
        pathLen += 1;
        if (!points.containsKey(location)) {
          points.put(location, pathLen);
        }
      }
    }
    return points;
  }

  static Point updateLocation(Point location, char dir) {
    if (dir == 'U') {
      return PointBuilder.from(location).y(location.y() + 1).build();
    } else if (dir == 'D') {
      return PointBuilder.from(location).y(location.y() - 1).build();
    } else if (dir == 'L') {
      return PointBuilder.from(location).x(location.x() - 1).build();
    } else if (dir == 'R') {
      return PointBuilder.from(location).x(location.x() + 1).build();
    }
    throw new RuntimeException("Unknown direction " + dir);
  }
}

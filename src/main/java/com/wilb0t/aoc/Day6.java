package com.wilb0t.aoc;

import com.google.common.collect.Sets;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6 {

  static Map<String, Set<String>> toChildMap(List<String> edges) {
    return edges.stream()
        .map(edgeStr -> edgeStr.split("\\)"))
        .map(edgeArr -> new AbstractMap.SimpleEntry<>(edgeArr[0], edgeArr[1]))
        .collect(
            Collectors.groupingBy(
                Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toSet())));
  }

  static Map<String, String> toParMap(List<String> edges) {
    return edges.stream()
        .map(edgeStr -> edgeStr.split("\\)"))
        .map(edgeArr -> new AbstractMap.SimpleEntry<>(edgeArr[1], edgeArr[0]))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  static int getOrbitCount(String base, List<String> edges) {
    var childMap = toChildMap(edges);

    var dequeue = new ArrayDeque<Map.Entry<String, Integer>>();
    dequeue.add(new AbstractMap.SimpleEntry<>(base, 0));
    int count = 0;
    while (!dequeue.isEmpty()) {
      var node = dequeue.pop();
      var name = node.getKey();
      var dist = node.getValue();
      count += dist;
      childMap
          .getOrDefault(name, Set.of())
          .forEach(chld -> dequeue.add(new AbstractMap.SimpleEntry<>(chld, dist + 1)));
    }
    return count;
  }

  static int getTxferCount(String base, String n1, String n2, List<String> edges) {
    var parMap = toParMap(edges);

    var n1Path = getPath(base, n1, parMap);
    var n2Path = getPath(base, n2, parMap);

    var common = Sets.intersection(n1Path, n2Path);

    return n1Path.size() + n2Path.size() - (2 * common.size());
  }

  static Set<String> getPath(String base, String toFind, Map<String, String> parMap) {
    var path = new HashSet<String>();
    var node = toFind;
    while (!node.equals(base)) {
      node = parMap.get(node);
      path.add(node);
    }
    return path;
  }
}

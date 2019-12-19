package com.wilb0t.aoc;

import io.norberg.automatter.AutoMatter;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Day18 {

/*

                      root
               a,2                          A,2
         B,2        root,2
     a,2   c,2

########################
#f.D.E.e.............@.#
######################.#
#d.....................#
########################
 */

  @AutoMatter
  interface Node {
    int dist();

    String name();

    default boolean isUnlocked(Set<Node> visited) {
      if (name().toUpperCase(Locale.ENGLISH).equals(name())) {
        return visited.stream().anyMatch(n -> n.name().equals(name().toLowerCase(Locale.ENGLISH)));
      }
      return true;
    }
  }

  int getMinSteps(Node start, Map<Node, List<Node>> nodeMap) {
    Set<Node> visited = new HashSet<>();

    var workingNodes = new ArrayDeque<Node>();
    workingNodes.add(start);
//    while (!workingNodes.isEmpty()) {
//
//    }
    return 0;
  }
}

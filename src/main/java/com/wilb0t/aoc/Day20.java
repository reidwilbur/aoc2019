package com.wilb0t.aoc;

import com.google.common.collect.Sets;
import io.norberg.automatter.AutoMatter;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day20 {

  static final char PATH = '.';

  @AutoMatter
  interface Pos {
    int x();

    int y();

    static Pos of(int x, int y) {
      return new PosBuilder().x(x).y(y).build();
    }

    default Optional<Character> tile(List<String> map) {
      if (y() >= 0 && y() < map.size()
          && x() >= 0 && x() < map.get(0).length()
      ) {
        return Optional.of(map.get(y()).charAt(x()));
      }
      return Optional.empty();
    }

    default Pos next(Dir d) {
      switch (d) {
        case N: return of(x(), y() - 1);
        case S: return of(x(), y() + 1);
        case E: return of(x() + 1, y());
        case W: return of(x() - 1, y());
        default:
          throw new RuntimeException("Invalid dir " + d);
      }
    }
  }

  @AutoMatter
  interface Portal {
    String name();

    Set<Pos> pstns();

    static boolean isPortalTile(Pos p, List<String> map) {
      return p.tile(map)
          .map(curTile -> curTile >= 'A' && curTile <= 'Z')
          .orElse(false);
    }

    static Optional<Portal> getPortal(Pos pos, List<String> map) {
      if (isPortalTile(pos, map)) {
        var otherPos = EnumSet.allOf(Dir.class).stream()
            .map(pos::next)
            .filter(p -> isPortalTile(p, map))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No other portal char for pos " + pos));

        var name = Stream.of(pos, otherPos)
            .sorted(Comparator.comparing(Pos::y).thenComparing(Pos::x))
            .flatMap(p -> p.tile(map).map(String::valueOf).stream())
            .collect(Collectors.joining());

        var portalPos = Stream.of(pos, otherPos)
            .filter(p -> EnumSet.allOf(Dir.class).stream()
                .flatMap(d -> p.next(d).tile(map).stream())
                .anyMatch(c -> c == PATH))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No path char for portal " + name));

        return Optional.of(new PortalBuilder().name(name).pstns(portalPos).build());
      }
      return Optional.empty();
    }

    static Set<Portal> getPortals(List<String> map) {
      return IntStream.range(0, map.size())
          .mapToObj(y -> IntStream.range(0, map.get(0).length()).mapToObj(x -> Pos.of(x, y)))
          .flatMap(Function.identity())
          .flatMap(pos -> getPortal(pos, map).stream())
          .collect(Collectors.groupingBy(Portal::name))
          .entrySet().stream()
          .map(e ->
              new PortalBuilder()
                  .name(e.getKey())
                  .pstns(e.getValue().stream()
                      .flatMap(p -> p.pstns().stream())
                      .collect(Collectors.toSet()))
                  .build())
          .collect(Collectors.toSet());
    }
  }

  enum Dir {
    N, S, W, E;

    Dir opposite() {
      switch (this) {
        case N: return S;
        case S: return N;
        case E: return W;
        case W: return E;
      }
      throw new RuntimeException("No opposite for dir " + this);
    }
  }
  
  static int getDist(String start, String end, List<String> map) {
    var portals = Portal.getPortals(map);
    var posToPortal = portals.stream()
        .flatMap(p -> p.pstns().stream().map(pos -> new AbstractMap.SimpleEntry<>(pos, p)))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    var startPortal = portals.stream().filter(p -> p.name().equals(start)).findFirst().get();
    var endPortal = portals.stream().filter(p -> p.name().equals(end)).findFirst().get();
    
    var visited = new HashSet<Pos>();
    var queue = new ArrayDeque<Map.Entry<Pos, Integer>>();
    queue.addAll(
        startPortal.pstns().stream()
            .map(pos -> new AbstractMap.SimpleEntry<>(pos, -1))
            .collect(Collectors.toList()));
    while (!queue.isEmpty()) {
      var entry = queue.pop();
      var pos = entry.getKey();
      //display(pos, visited, map);
      visited.add(pos);
      var dist = entry.getValue();
      var nps = EnumSet.allOf(Dir.class).stream()
          .map(pos::next)
          .filter(npos -> validStep(npos, posToPortal, map))
          .filter(npos -> !visited.contains(npos))
          .collect(Collectors.toSet());
      if (!Sets.intersection(nps, endPortal.pstns()).isEmpty()) {
        return dist;
      }
      nps = nps.stream()
          .flatMap(npos -> {
            if (posToPortal.containsKey(npos)) {
                  return posToPortal.get(npos).pstns().stream()
                      .filter(nnpos -> !nnpos.equals(npos))
                      .flatMap(nnpos -> 
                          EnumSet.allOf(Dir.class).stream()
                              .map(nnpos::next)
                              .filter(nnnpos -> validStep(nnnpos, posToPortal, map)));
            } else {
              return Stream.of(npos);
            }
          }).collect(Collectors.toSet());
          
      nps.forEach(npos -> queue.add(new AbstractMap.SimpleEntry<>(npos, dist + 1)));
    }
    
    return -1;
  }
  
  static boolean validStep(Pos pos, Map<Pos, Portal> posToPortal, List<String> map) {
    return pos.tile(map)
        .map(t -> posToPortal.containsKey(pos) || t == PATH)
        .orElse(false);
  }
  
  static void display(Pos curPos, Set<Pos> visited, List<String> map) {
    IntStream.range(0, map.size())
        .mapToObj(y -> IntStream.range(0, map.get(0).length()).mapToObj(x -> Pos.of(x, y)))
        .flatMap(Function.identity())
        .forEach(pos -> {
          if (pos.x() == 0) {
            System.out.println();
          }
          if (pos.equals(curPos)) {
            System.out.print('@');
          } else if (visited.contains(pos)) {
            System.out.print('*');
          } else { 
            System.out.print(map.get(pos.y()).charAt(pos.x()));
          }
        });
    System.out.println();
  }

//  static Map<Portal, List<Map.Entry<Portal, Integer>>> toAdjMap(List<String> map) {
//    var portals = Portal.getPortals(map);
//    
//  }
  
//  static List<Map.Entry<Portal, Integer>> getAdj(
//      Portal portal, 
//      Set<Portal> portals, 
//      List<String> map
//  ) {
//    var stack = new ArrayDeque<Map.Entry<Pos, Integer>>();
//    var adj = new ArrayList<Map.Entry<Portal, Integer>>();
//    portal.pstns().forEach(pos -> stack.add(new AbstractMap.SimpleEntry<>(pos, 0)));
//    var others = Sets.difference(Set.of(portal), portals);
//    while (!stack.isEmpty()) {
//      
//    }
//    return adj;
//  }
}

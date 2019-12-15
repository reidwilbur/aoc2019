package com.wilb0t.aoc;

import io.norberg.automatter.AutoMatter;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day15 {

  static final long DROID_WALL = 0;
  static final long DROID_MOVE = 1;
  static final long DROID_OXY = 2;

  @AutoMatter
  interface Loc {
    int x();

    int y();

    static Loc of(int x, int y) {
      return new LocBuilder().x(x).y(y).build();
    }

    default Loc next(Dir d) {
      switch (d) {
        case N: return of(x(), y() + 1);
        case S: return of(x(), y() - 1);
        case E: return of(x() + 1, y());
        case W: return of(x() - 1, y());
        default:
          throw new RuntimeException("Invalid dir " + d);
      }
    }
  }

  @AutoMatter
  interface Cmd {
    Dir dir();

    boolean isBacktrack();

    static Cmd of(Dir d) {
      return new CmdBuilder().dir(d).isBacktrack(false).build();
    }

    static Cmd of(Dir d, boolean backtrack) {
      return new CmdBuilder().dir(d).isBacktrack(backtrack).build();
    }
  }

  enum Dir {
    N, S, W, E;

    long toRobotCmd() {
      return ordinal() + 1;
    }

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

  static Map<Loc, Long> getSectionMap(List<Long> code) throws InterruptedException {
    var computer = new Day9.Computer();

    var input = new LinkedBlockingDeque<Long>();
    var output = new LinkedBlockingDeque<Long>();

    var exec = Executors.newFixedThreadPool(1);
    var compExec = CompletableFuture.runAsync(() -> computer.exec("droid", code, input, output));

    // dfs to record the full map
    var map = new HashMap<Loc, Long>();
    var stack =
        Arrays.stream(Dir.values()).map(Cmd::of).collect(Collectors.toCollection(ArrayDeque::new));
    var loc = Loc.of(0, 0);
    map.put(loc, 1L);
    while (!stack.isEmpty()) {
      var cmd = stack.pop();
      var nextLoc = loc.next(cmd.dir());
      if (cmd.isBacktrack()) {
        input.add(cmd.dir().toRobotCmd());
        output.take();
        loc = nextLoc;
      } else {
        input.add(cmd.dir().toRobotCmd());
        var droidStat = output.take();
        map.put(nextLoc, droidStat);
        if (droidStat != DROID_WALL) {
          loc = nextLoc;
          stack.push(Cmd.of(cmd.dir().opposite(), true));
          Arrays.stream(Dir.values())
              .filter(d -> !map.containsKey(nextLoc.next(d)) && d != cmd.dir().opposite())
              .forEach(d -> stack.push(Cmd.of(d)));
        }
      }
    }

    exec.shutdownNow();

    return map;
  }

  static int minStepsToOxy(Map<Loc, Long> map) {
    var start = Loc.of(0, 0);

    var queue = new ArrayDeque<>(List.of(new AbstractMap.SimpleEntry<>(start, 0)));

    var hist = new HashSet<>(List.of(start));

    while (!queue.isEmpty()) {
      var entry = queue.pop();
      var loc = entry.getKey();
      hist.add(loc);
      var dist = entry.getValue();
      var mapVal = map.get(loc);
      if (mapVal == DROID_OXY) {
        return entry.getValue();
      } else {
        Arrays.stream(Dir.values())
            .map(loc::next)
            .filter(Predicate.not(hist::contains))
            .filter(map::containsKey)
            .filter(nextLoc -> map.get(nextLoc) != DROID_WALL)
            .map(nextLoc -> new AbstractMap.SimpleEntry<>(nextLoc, dist + 1))
            .forEach(queue::add);
      }
    }
    return -1;
  }

  static int timeToFill(Map<Loc, Long> map) {
    var start =
        map.entrySet().stream().filter(e -> e.getValue() == DROID_OXY).findFirst().get().getKey();

    var queue = new ArrayDeque<>(List.of(new AbstractMap.SimpleEntry<>(start, 0)));

    var hist = new HashSet<>(List.of(start));

    var maxDist = 0;
    while (!queue.isEmpty()) {
      var entry = queue.pop();
      var loc = entry.getKey();
      hist.add(loc);
      var dist = entry.getValue();
      maxDist = Math.max(dist, maxDist);
      Arrays.stream(Dir.values())
          .map(loc::next)
          .filter(Predicate.not(hist::contains))
          .filter(map::containsKey)
          .filter(nextLoc -> map.get(nextLoc) != DROID_WALL)
          .map(nextLoc -> new AbstractMap.SimpleEntry<>(nextLoc, dist + 1))
          .forEach(queue::add);
    }
    return maxDist;
  }

  static void printMap(Map<Loc, Long> map, Loc loc) {
    var xstats = map.keySet().stream().mapToLong(Loc::x).summaryStatistics();
    var ystats = map.keySet().stream().mapToLong(Loc::y).summaryStatistics();

    for (long y = ystats.getMin(); y <= ystats.getMax(); y++) {
      for (long x = xstats.getMin(); x <= xstats.getMax(); x++) {
        var mapVal = map.getOrDefault(Loc.of((int) x, (int) y), -1L);
        var mapChar =
            (mapVal == DROID_WALL)
                ? '█'
                : (mapVal == DROID_MOVE) ? '.' : (mapVal == DROID_OXY) ? 'O' : ' ';
        if (loc.x() == x && loc.y() == y) {
          System.out.print('@');
        } else {
          System.out.print(mapChar);
        }
      }
      System.out.println();
    }
  }
}

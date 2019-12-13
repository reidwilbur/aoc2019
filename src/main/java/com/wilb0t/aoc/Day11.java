package com.wilb0t.aoc;

import io.norberg.automatter.AutoMatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class Day11 {

  @AutoMatter
  interface Location {
    Long x();
    Long y();

    default Location nextLoc(Direction dir) {
      switch (dir) {
        case UP:
          return LocationBuilder.from(this).y(y() - 1).build();
        case RT:
          return LocationBuilder.from(this).x(x() + 1).build();
        case DN:
          return LocationBuilder.from(this).y(y() + 1).build();
        case LT:
          return LocationBuilder.from(this).x(x() - 1).build();
        default:
          throw new RuntimeException("Invalid direction " + dir);
      }
    }
  }

  enum Direction {
    UP, RT, DN, LT;

    Direction nextDir(long turn) {
      if (turn == 0L) {
        return values()[(ordinal() + values().length - 1) % values().length];
      }
      return values()[(ordinal() + 1) % values().length];
    }
  }

  static Map<Location, Long> execRobot(List<Long> code, long startColor) {
    var computer = new Day9.Computer();

    var input = new LinkedBlockingDeque<Long>();
    var output = new LinkedBlockingDeque<Long>();
    var mem = new ConcurrentHashMap<Location, Long>();
    mem.put(new LocationBuilder().x(0L).y(0L).build(), startColor);

    var exec = Executors.newFixedThreadPool(2);
    var compExec = CompletableFuture.runAsync(() -> computer.exec("a", code, input, output), exec);
    var robotExec = CompletableFuture.runAsync(() -> robot(mem, input, output), exec);

    compExec.join();

    output.add(Long.MIN_VALUE);
    robotExec.join();

    exec.shutdownNow();

    return mem;
  }

  static void printMsg(List<Long> code, long startColor) {
    var mem = execRobot(code, startColor);

    var xStats =
        mem.keySet()
            .stream()
            .mapToLong(Location::x)
            .summaryStatistics();

    var yStats =
        mem.keySet()
            .stream()
            .mapToLong(Location::y)
            .summaryStatistics();

    for (long y = yStats.getMin(); y <= yStats.getMax(); y++) {
      for (long x = xStats.getMin(); x <= xStats.getMax(); x++) {
        var color = mem.getOrDefault(new LocationBuilder().x(x).y(y).build(), 0L);
        System.out.print(color == 1L ? '█' : ' ');
      }
      System.out.println();
    }
  }

  static void robot(
      Map<Location, Long> mem,
      BlockingDeque<Long> compIn,
      BlockingDeque<Long> compOut
  ) {
    var loc = new LocationBuilder().x(0L).y(0L).build();
    var dir = Direction.UP;

    while (true) {
      compIn.add(mem.getOrDefault(loc, 0L));
      try {
        var color = compOut.take();
        if (color == Long.MIN_VALUE) {
          return;
        }
        mem.put(loc, color);

        var turn = compOut.take();
        if (turn == Long.MIN_VALUE) {
          return;
        }
        dir = dir.nextDir(turn);
        loc = loc.nextLoc(dir);
      } catch (InterruptedException e) {
        System.out.println("Could not read from output: " + e);
      }
    }
  }
}

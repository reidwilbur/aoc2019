package com.wilb0t.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day17 {

  static List<Character> getMap(List<Long> code) {
    var input = new LinkedBlockingDeque<Long>();
    var output = new LinkedBlockingDeque<Long>();

    var computer = new Day9.Computer();

    computer.exec("robot", code, input, output);

    return output.stream().map(l -> (char) l.intValue()).collect(Collectors.toList());
  }

  static int getAlignmentParams(List<Long> code) {
    var map = getMap(code);

    var stride = map.indexOf((char) 10) + 1;

    //printMap(map);

    return IntStream.range(0, map.size())
        .filter(idx -> map.get(idx) == '#')
        .filter(idx -> {
          var lt = ((idx % stride) - 1 < 0) ? ' ' : map.get(idx - 1);
          var rt = ((idx % stride) + 1 >= stride) ? ' ' : map.get(idx + 1);
          var up = (idx - stride < 0) ? ' ' : map.get(idx - stride);
          var dn = (idx + stride >= map.size()) ? ' ' : map.get(idx + stride);
          return Stream.of(lt, rt, up, dn).allMatch(c -> c == '#');
        })
        .map(idx -> (idx % stride) * (idx / stride))
        .sum();
  }

  enum Dir {
    UP, RT, DN, LT;

    Optional<Integer> nextIdx(List<Character> map, int idx, int stride) {
      switch (this) {
        case UP: return (idx - stride < 0) ? Optional.empty() : Optional.of(idx - stride);
        case RT: return ((idx % stride) + 1 >= stride) ? Optional.empty() : Optional.of(idx + 1);
        case DN: return (idx + stride >= map.size()) ? Optional.empty() : Optional.of(idx + stride);
        case LT: return ((idx % stride) - 1 < 0) ? Optional.empty() : Optional.of(idx - 1);
        default: throw new RuntimeException("Invalid dir " + this);
      }
    }

    Optional<Dir> nextDir(List<Character> map, int idx, int stride) {
      var dirs = List.of(
          this,
          values()[(ordinal() - 1 + values().length) % values().length],
          values()[(ordinal() + 1) % values().length]);

      var filtered = dirs.stream()
          .filter(d -> d.nextIdx(map, idx, stride).filter(i -> map.get(i) == '#').isPresent())
          .collect(Collectors.toList());

      return filtered.stream().findFirst();
    }

    static Dir toDir(char robot) {
      switch (robot) {
        case '<': return LT;
        case '^': return UP;
        case '>': return RT;
        case 'v': return DN;
        default: throw new RuntimeException("Invalid robot char " + robot);
      }
    }

    char toRobot() {
      switch (this) {
        case UP: return '^';
        case RT: return '>';
        case DN: return 'v';
        case LT: return '<';
        default: throw new RuntimeException("Invalid dir " + this);
      }
    }

    Optional<String> getTurnDir(Dir d) {
      if (this == d) {
        return Optional.empty();
      }
      if (this == LT && d == UP) {
        return Optional.of("R");
      }
      if (this == UP && d == LT) {
        return Optional.of("L");
      }
      var diff = d.ordinal() - ordinal();
      return Optional.of((diff > 0) ? "R" : "L");
    }
  }

  static List<String> toCmds(List<Character> map) {
    final var stride = map.indexOf((char) 10) + 1;

    var robotChars = Set.of('<', '^', '>', 'v');
    var robotIdx = IntStream.range(0, map.size())
        .filter(idx -> robotChars.contains(map.get(idx)))
        .findFirst()
        .getAsInt();

    var dirOpt = Optional.of(Dir.toDir(map.get(robotIdx)));
    var dist = 0;
    var cmds = new ArrayList<String>();
    while (true) {
      //printMap(map, robotIdx, dirOpt.get());
      var ridx = robotIdx;
      var nextDirOpt = dirOpt.flatMap(dir -> dir.nextDir(map, ridx, stride));
      if (nextDirOpt.isEmpty()) {
        if (dist > 0) {
          cmds.add(String.valueOf(dist + 1));
        }
        return cmds;
      } else {
        var nextDir = nextDirOpt.get();
        robotIdx = nextDir.nextIdx(map, robotIdx, stride).get();
        var dir = dirOpt.get();
        var turnOpt = dir.getTurnDir(nextDir);
        if (turnOpt.isPresent()) {
          if (dist > 0) {
            cmds.add(String.valueOf(dist + 1));
          }
          dist = 0;
          cmds.add(turnOpt.get());
        } else {
          dist += 1;
        }
        dirOpt = nextDirOpt;
      }
    }
  }

  static long getDust(List<Long> code) throws InterruptedException {
    // not proud of this, just eye-balled it from my cmd output
    // L,4,R,8,L,6,L,10,
    // L,6,R,8,R,10,L,6,L,6,
    // L,4,R,8,L,6,L,10,
    // L,6,R,8,R,10,L,6,L,6,
    // L,4,L,4,L,10,
    // L,4,L,4,L,10,
    // L,6,R,8,R,10,L,6,L,6,
    // L,4,R,8,L,6,L,10,
    // L,6,R,8,R,10,L,6,L,6,
    // L,4,L,4,L,10
    var mainFun = List.of(
        "A", "B", "A", "B", "C", "C", "B", "A", "B", "C"
    );
    var fnA = "L,4,R,8,L,6,L,10\n";
    var fnB = "L,6,R,8,R,10,L,6,L,6\n";
    var fnC = "L,4,L,4,L,10\n";

    var fnInput =
        String.join(",", mainFun)
            .chars()
            .mapToLong(i -> (long) i)
            .boxed()
            .collect(Collectors.toList());
    fnInput.add((long) '\n');
    fnInput.addAll(fnA.chars().mapToLong(i -> (long) i).boxed().collect(Collectors.toList()));
    fnInput.addAll(fnB.chars().mapToLong(i -> (long) i).boxed().collect(Collectors.toList()));
    fnInput.addAll(fnC.chars().mapToLong(i -> (long) i).boxed().collect(Collectors.toList()));
    fnInput.addAll("n\n".chars().mapToLong(i -> (long) i).boxed().collect(Collectors.toList()));

    var dustCode = new ArrayList<>(code);
    dustCode.set(0, 2L);

    var input = new LinkedBlockingDeque<>(fnInput);
    var output = new LinkedBlockingDeque<Long>();

    var computer = new Day9.Computer();

    computer.exec("robot", dustCode, input, output);

    var dbg = new ArrayList<Long>();
    output.drainTo(dbg);

    //printOutput(dbg);

    return dbg.get(dbg.size() - 1);
  }

  static void printOutput(List<Long> output) {
    for (var c : output) {
      if (c == '\n') {
        System.out.println();
      } else {
        System.out.print((char) c.intValue());
      }
    }
  }

  static void printMap(List<Character> map, int robotIdx, Dir d) {
    for (var idx : IntStream.range(0, map.size()).toArray()) {
      if (map.get(idx) == 10) {
        System.out.println();
      } else {
        System.out.print((idx == robotIdx) ? d.toRobot() : map.get(idx));
      }
    }
    System.out.println();
  }
}

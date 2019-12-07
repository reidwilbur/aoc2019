package com.wilb0t.aoc;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class Day7 {

  static int getMaxSignal(List<Integer> code) {
    var phaseSettings = permute(Set.of(0, 1, 2, 3, 4));
    var maxSig = Integer.MIN_VALUE;
    for (var setting : phaseSettings) {
      var sig = getSignal(code, setting);
      maxSig = Math.max(sig, maxSig);
    }
    return maxSig;
  }

  static int getMaxFeedbackSignal(List<Integer> code) {
    var phaseSettings = permute(Set.of(5, 6, 7, 8, 9));
    var maxSig = Integer.MIN_VALUE;
    for (var setting : phaseSettings) {
      var sig = getFeedbackSignal(code, setting);
      maxSig = Math.max(sig, maxSig);
    }
    return maxSig;
  }

  static int getSignal(List<Integer> code, List<Integer> setting) {
    var computer = new Day5.Computer();
    var outA = computer.exec(new ArrayList<>(code), List.of(setting.get(0), 0).iterator()).get(0);
    var outB = computer.exec(new ArrayList<>(code), List.of(setting.get(1), outA).iterator()).get(0);
    var outC = computer.exec(new ArrayList<>(code), List.of(setting.get(2), outB).iterator()).get(0);
    var outD = computer.exec(new ArrayList<>(code), List.of(setting.get(3), outC).iterator()).get(0);
    return computer.exec(new ArrayList<>(code), List.of(setting.get(4), outD).iterator()).get(0);
  }

  static int getFeedbackSignal(List<Integer> code, List<Integer> setting) {
    var computer = new Day5.Computer();
    var inA = new LinkedBlockingDeque<>(List.of(setting.get(0), 0));
    var inB = new LinkedBlockingDeque<>(List.of(setting.get(1)));
    var inC = new LinkedBlockingDeque<>(List.of(setting.get(2)));
    var inD = new LinkedBlockingDeque<>(List.of(setting.get(3)));
    var inE = new LinkedBlockingDeque<>(List.of(setting.get(4)));

    var exec = Executors.newFixedThreadPool(5);

    var execA = CompletableFuture.runAsync(() -> computer.exec("A", new ArrayList<>(code), inA, inB), exec);
    var execB = CompletableFuture.runAsync(() -> computer.exec("B", new ArrayList<>(code), inB, inC), exec);
    var execC = CompletableFuture.runAsync(() -> computer.exec("C", new ArrayList<>(code), inC, inD), exec);
    var execD = CompletableFuture.runAsync(() -> computer.exec("D", new ArrayList<>(code), inD, inE), exec);
    var execE = CompletableFuture.runAsync(() -> computer.exec("E", new ArrayList<>(code), inE, inA), exec);

    var execAll = CompletableFuture.allOf(execA, execB, execC, execD, execE);
    execAll.join();

    return inA.peek();
  }

  static List<List<Integer>> permute(Set<Integer> nums) {
    if (nums.size() == 1) {
      return List.of(List.of(nums.iterator().next()));
    } else {
      var perms = new ArrayList<List<Integer>>();
      nums.forEach(num -> {
        var rest = Sets.difference(nums, Set.of(num));
        var subPerms = permute(rest);
        subPerms.forEach(perm -> {
          var fullPerm = new ArrayList<Integer>();
          fullPerm.add(num);
          fullPerm.addAll(perm);
          perms.add(fullPerm);
        });
      });
      return perms;
    }
  }
}

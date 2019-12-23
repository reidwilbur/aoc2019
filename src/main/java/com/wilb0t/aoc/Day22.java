package com.wilb0t.aoc;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day22 {
  
  static long shuffle(List<String> steps, long size, long idx) {
    var work = idx;

    for (String step: steps) {
      if (step.startsWith("deal into")) {
        work = dealIntoNewStack(size, work);
      } else if (step.startsWith("cut")) {
        var cut = Integer.parseInt(step.split(" ")[1]);
        work = cutCards(cut, size, work);
      } else if (step.startsWith("deal with")) {
        var inc = Integer.parseInt(step.split(" ")[3]);
        work = dealWithInc(inc, size, work);
      } else {
        throw new RuntimeException("Step is too hot to handle: " + step);
      }
    }

    return work;
  }
  
  static long dealIntoNewStack(long size, long idx) {
    return (size - 1) - idx;
  }
  
  static long cutCards(long cut, long size, long idx) {
    return (idx - cut + size) % size;
  }
  
  static long dealWithInc(long inc, long size, long idx) {
    return (idx * inc) % size;
  }
  
  static int findRepeat(List<String> steps) {
    var size = 119315717514047L;
   
    var idx = 0L;
    var itrIdx = 0L;
    var lastItrIdx = 0L;
    var itrCount = 0;
    var diffList = new ArrayList<Long>();
    while (true) {
      itrIdx = shuffle(steps, size, itrIdx);
      itrCount += 1;
      //System.out.println(String.format("% 20d % 20d", itrIdx, lastItrIdx - itrIdx));
      var diff = Math.abs(lastItrIdx - itrIdx);
      if (itrIdx == idx || itrCount > 1000 || diffList.contains(diff)) {
        diffList.forEach(d -> System.out.println(String.format("% 20d", d)));
        //System.out.println(String.format("% 20d % 20d", itrIdx, lastItrIdx - itrIdx));
        return itrCount;
      }
      diffList.add(Math.abs(lastItrIdx - itrIdx));
      lastItrIdx = itrIdx;
    }
  }
}

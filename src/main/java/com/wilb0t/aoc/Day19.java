package com.wilb0t.aoc;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

public class Day19 {

  static int scan(List<Long> code) throws InterruptedException {
    var input = new LinkedBlockingDeque<Long>();
    var output = new LinkedBlockingDeque<Long>();

    var computer = new Day9.Computer();
    int count = 0;
    for (int y = 0; y < 50; y++) {
      for (int x = 0; x < 50; x++) {
        input.add((long) x);
        input.add((long) y);
        computer.exec("drone", code, input, output);
        var sample = output.take();
        count += (sample == 1) ? 1 : 0;
      }
    }
    return count;
  }

  static List<Integer> getLineVals(List<Long> code, long yBot) throws InterruptedException {
    var input = new LinkedBlockingDeque<Long>();
    var output = new LinkedBlockingDeque<Long>();

    var computer = new Day9.Computer();

    var xLeft = 0;
    var x = 0;
    var lastSample = 0L;
    while (xLeft == 0) {
      input.add((long) x);
      input.add(yBot);

      computer.exec("drone", code, input, output);
      var sample = output.take();

      if (lastSample == 0L && sample == 1L) {
        xLeft = x;
      }
      x += 1;
      lastSample = sample;
    }

    input.clear();
    output.clear();

    var yTop = yBot - 99;
    var xRight = xLeft + 99;
    input.add((long) xRight);
    input.add(yTop);
    computer.exec("drone", code, input, output);
    var ysamp = output.take();
    return List.of(xLeft, (int) yTop, ysamp.intValue());
  }

  static Map.Entry<Integer, Integer> search(List<Long> code) throws InterruptedException {
    var yBot = 99;

    var yHi = 0;
    var yLo = 0;

    while (yHi == 0) {
      var vals = getLineVals(code, yBot);
      if (vals.get(2) == 1) {
        yHi = yBot;
        yLo = yBot / 2;
      }
      yBot *= 2;
    }

    var boxFits = 0;
    while (yHi - yLo > 1) {
      var yMid = yLo + ((yHi - yLo) / 2);
      var vals = getLineVals(code, yMid);
      if (vals.get(2) != boxFits) {
        yHi = yMid;
      } else {
        yLo = yMid;
      }
    }

    var lineVals = getLineVals(code, yHi);
    return new AbstractMap.SimpleEntry<>(lineVals.get(0), lineVals.get(1));
  }
}

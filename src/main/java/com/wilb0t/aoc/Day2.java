package com.wilb0t.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day2 {
  static class Computer {
    static final int ADD = 1;
    static final int MUL = 2;
    static final int HLT = 99;

    public List<Integer> exec(List<Integer> code) {
      var ip = 0;
      while (code.get(ip) != HLT) {
        var opCode = code.get(ip);
        var in1 = code.get(ip + 1);
        var in2 = code.get(ip + 2);
        var out = code.get(ip + 3);

        switch (opCode) {
          case ADD:
            code.set(out, code.get(in1) + code.get(in2));
            break;
          case MUL:
            code.set(out, code.get(in1) * code.get(in2));
            break;
          default:
            throw new IllegalArgumentException("Invalid opcode " + opCode + " at ip " + ip);
        }
        ip += 4;
      }
      return code;
    }

    public int findNounVerb(List<Integer> code, int result) {
      return IntStream.range(0, 100)
          .flatMap(
              noun ->
                  IntStream.range(0, 100)
                      .map(
                          verb -> {
                            var execCode = new ArrayList<>(code);
                            execCode.set(1, noun);
                            execCode.set(2, verb);
                            var res = exec(execCode).get(0);
                            if (res == result) {
                              return (100 * noun) + verb;
                            } else {
                              return 0;
                            }
                          }))
          .filter(res -> res != 0)
          .findFirst()
          .getAsInt();
    }
  }
}

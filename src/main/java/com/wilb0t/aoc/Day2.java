package com.wilb0t.aoc;

import java.util.ArrayList;
import java.util.List;

public class Day2 {
  static class Computer {
    static final int ADD = 1;
    static final int MUL = 2;
    static final int HLT = 99;

    public List<Integer> exec(List<Integer> initCode) {
      var code = new ArrayList<>(initCode);
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
  }
}

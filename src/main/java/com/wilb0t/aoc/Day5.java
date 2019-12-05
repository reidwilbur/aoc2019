package com.wilb0t.aoc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day5 {
  static class Computer {
    static final String ADD = "01";
    static final String MUL = "02";
    static final String IN = "03";
    static final String OUT = "04";
    static final int HLT = 99;

    public List<Integer> exec(List<Integer> code, Iterator<Integer> input) {
      var ip = 0;
      var output = new ArrayList<Integer>();
      while (code.get(ip) != HLT) {
        var instr = String.format("%05d", code.get(ip));
        var opCode = instr.substring(3);
        var im3 = instr.charAt(0) == '1';
        var im2 = instr.charAt(1) == '1';
        var im1 = instr.charAt(2) == '1';

        switch (opCode) {
          case ADD:
            {
              var p1 = code.get(ip + 1);
              var p2 = code.get(ip + 2);
              var p3 = code.get(ip + 3);

              var in1 = im1 ? p1 : code.get(p1);
              var in2 = im2 ? p2 : code.get(p2);

              code.set(p3, in1 + in2);
              ip += 4;
              break;
            }
          case MUL:
            {
              var p1 = code.get(ip + 1);
              var p2 = code.get(ip + 2);
              var p3 = code.get(ip + 3);

              var in1 = im1 ? p1 : code.get(p1);
              var in2 = im2 ? p2 : code.get(p2);

              p3 = code.get(ip + 3);
              code.set(p3, in1 * in2);
              ip += 4;
              break;
            }
          case IN: {
            var p1 = code.get(ip + 1);

            code.set(p1, input.next());
            ip += 2;
            break;
          }
          case OUT: {
            var p1 = code.get(ip + 1);
            var in1 = im1 ? p1 : code.get(p1);

            output.add(in1);
            ip += 2;
            break;
          }
          default:
            throw new IllegalArgumentException("Invalid opcode " + opCode + " at ip " + ip);
        }
      }
      return output;
    }
  }
}

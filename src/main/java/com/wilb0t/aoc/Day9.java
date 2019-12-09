package com.wilb0t.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day9 {
  static class Instr {
    static final char MODE_POS = '0';
    static final char MODE_IMM = '1';
    static final char MODE_REL = '2';

    private final String instr;
    private final int ip;
    private final int base;
    private final OpCode opCode;
    private final List<Long> mem;

    Instr(int ip, int base, List<Long> mem) {
      instr = String.format("%05d", mem.get(ip));
      this.ip = ip;
      this.mem = mem;
      this.base = base;
      this.opCode = OpCode.toOpcode(ip, instr.substring(3));
    }

    OpCode opcode() {
      return opCode;
    }

    Long param(int idx) {
      return mem.get(ip + idx);
    }

    char mode(int idx) {
      return instr.charAt(3 - idx);
    }

    Long in(int idx) {
      var param = param(idx);
      var mode = mode(idx);
      if (mode == MODE_REL) {
        return mem.get(param.intValue() + base);
      } else if (mode == MODE_IMM) {
        return param;
      } else {
        return mem.get(param.intValue());
      }
    }

    Long out(int idx) {
      var param = param(idx);
      var mode = mode(idx);
      if (mode == MODE_POS) {
        return param;
      } else {
        return param + base;
      }
    }

    @Override
    public String toString() {
      if (opCode.size() == 2) {
        return String.format("%s %s:%s:%s %d:%d",
            opCode, mode(1), mode(2), mode(3), param(1), in(1));
      } else if (opCode.size() == 3) {
        return String.format("%s %s:%s:%s %d:%d %d:%d",
            opCode, mode(1), mode(2), mode(3), param(1), in(1), param(2), in(2));
      } else {
        return String.format("%s %s:%s:%s %d:%d %d:%d %d",
            opCode, mode(1), mode(2), mode(3), param(1), in(1), param(2), in(2), param(3));
      }
    }

    int size() {
      return opCode.size();
    }
  }

  enum OpCode {
    ADD("01", 4),
    MUL("02", 4),
    IN("03", 2),
    OUT("04", 2),
    JIT("05", 3),
    JIF("06", 3),
    LT("07", 4),
    EQ("08",4),
    RBO("09", 2);

    private final String code;
    private final int size;

    static OpCode toOpcode(int ip, String str) {
      return Stream.of(values()).filter(oc -> oc.code.equals(str)).findFirst().orElseThrow(
          () -> new RuntimeException("Invalid opcode " + str + " at ip " + ip)
      );
    }

    OpCode(String code, int size) {
      this.code = code;
      this.size = size;
    }

    int size() {
      return size;
    }
  }

  static class Computer {
    static final int HLT = 99;

    public void exec(
        String name,
        List<Long> code,
        BlockingDeque<Long> input,
        BlockingDeque<Long> output) {
      var ip = 0;
      var base = 0;
      var mem = new ArrayList<>(code);
      IntStream.range(0, code.size() * 100).forEach(i -> mem.add(0L));
      while (mem.get(ip) != HLT) {
        var instr = new Day9.Instr(ip, base, mem);

        //System.out.println(instr.toString());
        switch (instr.opcode()) {
          case ADD:
            mem.set(instr.out(3).intValue(), instr.in(1) + instr.in(2));
            ip += instr.size();
            break;
          case MUL:
            mem.set(instr.out(3).intValue(), instr.in(1) * instr.in(2));
            ip += instr.size();
            break;
          case IN:
            try {
              mem.set(instr.out(1).intValue(), input.take());
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
            ip += instr.size();
            break;
          case OUT:
            output.add(instr.in(1));
            ip += instr.size();
            break;
          case JIT:
            ip = (int) (instr.in(1) != 0 ? instr.in(2) : ip + instr.size());
            break;
          case JIF:
            ip = (int) (instr.in(1) == 0 ? instr.in(2) : ip + instr.size());
            break;
          case LT:
            mem.set(instr.out(3).intValue(), instr.in(1) < instr.in(2) ? 1L : 0L);
            ip += instr.size();
            break;
          case EQ:
            mem.set(instr.out(3).intValue(), instr.in(1).equals(instr.in(2)) ? 1L : 0L);
            ip += instr.size();
            break;
          case RBO:
            base += instr.in(1);
            ip += instr.size();
            break;
        }
      }
    }
  }
}

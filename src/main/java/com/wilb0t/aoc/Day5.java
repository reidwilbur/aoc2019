package com.wilb0t.aoc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day5 {

  static class Instr {

    private final String instr;
    private final boolean imm1;
    private final boolean imm2;
    private final boolean imm3;
    private final int ip;
    private final List<Integer> code;

    Instr(int ip, List<Integer> code) {
      instr = String.format("%05d", code.get(ip));
      imm3 = instr.charAt(0) == '1';
      imm2 = instr.charAt(1) == '1';
      imm1 = instr.charAt(2) == '1';
      this.ip = ip;
      this.code = code;
    }

    String opcode() {
      return instr.substring(3);
    }

    Integer p1() {
      return code.get(ip + 1);
    }

    Integer p2() {
      return code.get(ip + 2);
    }

    Integer p3() {
      return code.get(ip + 3);
    }

    Integer in1() {
      var param = code.get(ip + 1);
      return imm1 ? param : code.get(param);
    }

    Integer in2() {
      var param = code.get(ip + 2);
      return imm2 ? param : code.get(param);
    }

    Integer in3() {
      var param = code.get(ip + 3);
      return imm3 ? param : code.get(param);
    }
  }

  static class Computer {
    static final String ADD = "01";
    static final String MUL = "02";
    static final String IN = "03";
    static final String OUT = "04";
    static final String JIT = "05";
    static final String JIF = "06";
    static final String LT = "07";
    static final String EQ = "08";
    static final int HLT = 99;

    public List<Integer> exec(List<Integer> code, Iterator<Integer> input) {
      var ip = 0;
      var output = new ArrayList<Integer>();
      while (code.get(ip) != HLT) {
        var instr = new Instr(ip, code);

        switch (instr.opcode()) {
          case ADD:
            code.set(instr.p3(), instr.in1() + instr.in2());
            ip += 4;
            break;
          case MUL:
            code.set(instr.p3(), instr.in1() * instr.in2());
            ip += 4;
            break;
          case IN:
            code.set(instr.p1(), input.next());
            ip += 2;
            break;
          case OUT:
            output.add(instr.in1());
            ip += 2;
            break;
          case JIT:
            ip = instr.in1() != 0 ? instr.in2() : ip + 3;
            break;
          case JIF:
            ip = instr.in1() == 0 ? instr.in2() : ip + 3;
            break;
          case LT:
            code.set(instr.p3(), instr.in1() < instr.in2() ? 1 : 0);
            ip += 4;
            break;
          case EQ:
            code.set(instr.p3(), instr.in1().equals(instr.in2()) ? 1 : 0);
            ip += 4;
            break;
          default:
            throw new IllegalArgumentException("Invalid opcode " + instr.opcode() + " at ip " + ip);
        }
      }
      return output;
    }
  }
}

package com.wilb0t.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class Day21 {
  
  
  static long getDamageWalk(List<Long> code) {
  /*
   * ABCD J
   * 1111 0  
   * 1110 0
   * 1101 1  1101 = a and b and (not c) and d
   * 1100 0  
   * 1011 1  1011 = a and (not b) and c and d
   * 1010 0  
   * 1001 1  1001 = a and (not b) and (not c) and d
   * 1000 0  
   * 0xxx 1  0xxx = not a
   * 
   * using prod of sums and some factoring
   * 
   * (A * D * ( (B * (NOT C)) + ((NOT B) * C) + ((NOT B) * (NOT C)) ) ) + (NOT A)
   */
    
    var cmds = List.of(
        "NOT C T",
        "NOT B J",
        "AND T J",  // (NOT B) * (NOT C)  have to do this one first coz of the 2 nots
        
        "NOT C T",
        "AND B T",
        "OR T J",  // (B * (NOT C)) + (sum)
        
        "NOT B T",
        "AND C T",
        "OR T J",  // ((NOT B) * C) + (sum)
        
        "AND A J", // A * (sum)
        "AND D J", // B * (sum)
        
        "NOT A T", // 
        "OR T J",  // NOT A + (prods)
        
        "WALK"
    );
   
    var comp = new Day9.Computer();
    
    var input = new LinkedBlockingDeque<Long>();
    var output = new LinkedBlockingDeque<Long>();
    
    cmds.forEach(cmd -> (cmd + "\n").chars().forEach(c -> input.add((long) c)));
    
    comp.exec("droid", code, input, output);
    
    var out = new ArrayList<Long>();
    output.drainTo(out);
    
    //print(out);
    
    return out.get(out.size() - 1);
  }

  static long getDamageRun(List<Long> code) {
    
    /*
     * ABCDEFGHI J
     * 110101001 0
     * 110101010 0
     * 110101100 1
     * 110101101 1
     * 110101110 1
     * 110101111 1 =  a and b and (not c) and d and (not e) and f and g
     * 1101001     =  a and b and (not c) and d and (not e) and (not f) and g
     * 110110010
     *
     * 10110       =  a and (not b) and c and d and (not e)
     * 10x10       =  a and (not b) and d and (not e)
     * 
     * 100101      =  a and (not b) and (not c) and d and (not e) and f
     * 
     * 110110010
     * 
     * 0xxx 1  0xxx = not a
     *
     * AB!C ( D!EFG + !DE!FG + D!E ) + A!BCD + !A
     * AB!C (D!E + !DE!FG)  + A!BCD + !A
     * 
     * A B !C D !E + A !B !C D !E F + !A
     * A B !C D !E + A !B !C D !E F + !A
     * A !C D !E (B + !B F) + !A
     * 
     * A !B D !E + A B !C D !E + !A
     * A D (!E (!B + B !C)) + !A
     * 
     * A !B D !E + A B !C D + !A
     * A D (!E !B + B !C) + !A
     */

    var cmds = List.of(
        "NOT E T",
        "NOT B J",
        "AND T J",
        
        "NOT C T",
        "AND B T",
        "OR T J",
        
        "AND A J",
        "AND D J",
        
        "NOT A T",
        "OR T J",

        "RUN"
    );

    var comp = new Day9.Computer();

    var input = new LinkedBlockingDeque<Long>();
    var output = new LinkedBlockingDeque<Long>();

    cmds.forEach(cmd -> (cmd + "\n").chars().forEach(c -> input.add((long) c)));

    comp.exec("droid", code, input, output);

    var out = new ArrayList<Long>();
    output.drainTo(out);

    print(out);

    return out.get(out.size() - 1);
  }


  static void print(List<Long> output) {
    for (var c : output) {
      if  (c.intValue() == '\n') {
        System.out.println();
      } else {
        System.out.print((char) c.intValue());
      }
    }
  }
}

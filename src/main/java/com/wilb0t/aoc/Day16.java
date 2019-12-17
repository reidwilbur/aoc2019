package com.wilb0t.aoc;

import java.util.Arrays;

public class Day16 {

  static final int[] PATTERN = new int[] {0, 1, 0, -1};

  static int[] fft(int[] input, int phases) {
    int[] work = Arrays.copyOf(input, input.length);
    for (int phase = 1; phase <= phases; phase++) {
      for (int pos = 0; pos < input.length; pos++) {
        var val = 0;
        for (int idx = pos; idx < input.length; idx++) {
          var patVal = getPatternVal(pos + 1, idx);
          var posVal = work[idx];
          val += posVal * patVal;
        }
        work[pos] = Math.abs(val) % 10;
      }
    }

    return work;
  }

  static int[] fft(int[] input, int inputRepeats, int phases) {
    // each phase calculation is a matrix multiply like the example below
    // [ 1  0 -1  0  1  0 -1  0 ]   [ 1 ]
    // [ 0  1  1  0  0 -1 -1  0 ]   [ 2 ]
    // [ 0  0  1  1  1  0  0  0 ]   [ 3 ]
    // [ 0  0  0  1  1  1  1  0 ] * [ 4 ]
    // [ 0  0  0  0  1  1  1  1 ]   [ 5 ]
    // [ 0  0  0  0  0  1  1  1 ]   [ 6 ]
    // [ 0  0  0  0  0  0  1  1 ]   [ 7 ]
    // [ 0  0  0  0  0  0  0  1 ]   [ 8 ]
    // for the second half of the signal, each digit can be calculated
    // using just the digit and the digit to the right, with the last digit
    // just being the value as supplied in the signal
    // the offset specified for this problem points in the last 10%
    // of the txformed signal, so can accelerate by only calculating
    // from the end of the replicated signal to the offset for each phase
    // the final result will be in the head of the final array result
    // this puzzle is mostly based on lucky observation of calculation and is
    // kindof a gotcha riddle and is dumb
    var size = input.length * inputRepeats;
    var skip = input[6];
    for (int idx = 5; idx >= 0; idx--) {
      var digit = 1;
      for (int pidx = 0; pidx < 6 - idx; pidx++) {
        digit *= 10;
      }
      skip += input[idx] * digit;
    }
    int[] work = new int[size - skip];
    for (int idx = 0; idx < work.length; idx++) {
      work[idx] = input[(idx + skip) % input.length];
    }
    for (int phase = 1; phase <= phases; phase++) {
      for (int pos = work.length - 2; pos >= 0; pos--) {
        work[pos] = (work[pos] + work[pos + 1]) % 10;
      }
    }

    return Arrays.copyOfRange(work, 0, 8);
  }


  static int[] toInts(String input) {
    return input
        .chars()
        .map(c -> c - '0')
        .toArray();
  }

  static int getPatternVal(int pos, int idx) {
    return PATTERN[((idx + 1) / pos) % PATTERN.length];
  }
}

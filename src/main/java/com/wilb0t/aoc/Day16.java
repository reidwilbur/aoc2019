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
    var size = input.length * inputRepeats;
    var offset = input[6];
    for (int idx = 5; idx >= 0; idx--) {
      var digit = 1;
      for (int pidx = 0; pidx < 6 - idx; pidx++) {
        digit *= 10;
      }
      offset += input[idx] * digit;
    }
    System.out.println("Size " + size);
    System.out.println("Offset " + offset);
    System.out.println("Work " + Arrays.toString(Arrays.copyOfRange(input, 0, 8)));
    offset = offset % size;
    System.out.println("Offset " + offset);
    int[] work = new int[size - offset + 1];
    System.out.println("Work size " + work.length);
    for (int idx = 0; idx < work.length; idx++) {
      work[idx] = input[(idx + offset - 1) % input.length];
    }
    System.out.println("Start calc");
    for (int phase = 1; phase <= phases; phase++) {
      for (int pos = offset - 1; pos < size; pos++) {
        var val = 0;
        for (int idx = pos; idx < size; idx++) {
          var patVal = getPatternVal(pos + 1, idx);
          var posVal = work[idx - pos];
          val += posVal * patVal;
        }
        work[pos - (offset - 1)] = Math.abs(val) % 10;
      }
      System.out.println("Finished phase " + phase);
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

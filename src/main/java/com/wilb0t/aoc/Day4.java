package com.wilb0t.aoc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Day4 {

  static List<String> getPasswds(int start, int end) {
    return IntStream.range(start, end)
        .mapToObj(String::valueOf)
        .filter(Day4::hasAdjRepeat)
        .filter(Day4::digitsInc)
        .collect(Collectors.toList());
  }

  static List<String> getPasswdsPart2(int start, int end) {
    return IntStream.range(start, end)
        .mapToObj(String::valueOf)
        .filter(Day4::hasDouble)
        .filter(Day4::digitsInc)
        .collect(Collectors.toList());
  }

  static boolean hasAdjRepeat(String pw) {
    for (int i = 0; i < pw.length() - 1; i++) {
      if (pw.charAt(i) == pw.charAt(i + 1)) {
        return true;
      }
    }
    return false;
  }

  static boolean hasDouble(String pw) {
    var paddedPw = "_" + pw + "_";
    for (int i = 1; i < paddedPw.length() - 2; i++) {
      if (paddedPw.charAt(i - 1) != paddedPw.charAt(i)
          && paddedPw.charAt(i) == paddedPw.charAt(i + 1)
          && paddedPw.charAt(i + 1) != paddedPw.charAt(i + 2)) {
        return true;
      }
    }
    return false;
  }

  static boolean digitsInc(String pw) {
    for (int i = 0; i < pw.length() - 1; i++) {
      if (pw.charAt(i) > pw.charAt(i + 1)) {
        return false;
      }
    }
    return true;
  }
}

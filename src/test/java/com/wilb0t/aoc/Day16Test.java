package com.wilb0t.aoc;

import static com.wilb0t.aoc.Day15.Loc;
import static com.wilb0t.aoc.Day15.getSectionMap;
import static com.wilb0t.aoc.Day15.minStepsToOxy;
import static com.wilb0t.aoc.Day15.timeToFill;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day16Test {

  static final int[] SIGNAL =
      Day16.toInts("59704176224151213770484189932636989396016853707543672704688031159981571127975101449262562108536062222616286393177775420275833561490214618092338108958319534766917790598728831388012618201701341130599267905059417956666371111749252733037090364984971914108277005170417001289652084308389839318318592713462923155468396822247189750655575623017333088246364350280299985979331660143758996484413769438651303748536351772868104792161361952505811489060546839032499706132682563962136170941039904873411038529684473891392104152677551989278815089949043159200373061921992851799948057507078358356630228490883482290389217471790233756775862302710944760078623023456856105493");

  @BeforeAll
  static void initAll() throws Exception {
//    code =
//        Files.readAllLines(
//            Path.of(Day1Test.class.getResource("/Day15_input1.txt").toURI()),
//            StandardCharsets.UTF_8)
//            .stream()
//            .flatMap(s -> Stream.of(s.split(",")))
//            .map(Long::parseLong)
//            .collect(Collectors.toList());
  }

  @BeforeEach
  void init() {
  }

  @Test
  void testGetPatternVal() {
    var patVals = IntStream.range(0, 8).map(idx -> Day16.getPatternVal(1, idx)).toArray();
    assertArrayEquals(new int[]{1, 0, -1, 0, 1, 0, -1, 0}, patVals);

    patVals = IntStream.range(0, 8).map(idx -> Day16.getPatternVal(2, idx)).toArray();
    assertArrayEquals(new int[]{0, 1, 1, 0, 0, -1, -1, 0}, patVals);

    patVals = IntStream.range(0, 8).map(idx -> Day16.getPatternVal(3, idx)).toArray();
    assertArrayEquals(new int[]{0, 0, 1, 1, 1, 0, 0, 0}, patVals);

    patVals = IntStream.range(0, 8).map(idx -> Day16.getPatternVal(4, idx)).toArray();
    assertArrayEquals(new int[]{0, 0, 0, 1, 1, 1, 1, 0}, patVals);
  }

  @Test
  void testFft() {
    var exp = new int[]{4,8,2,2,6,1,5,8};
    var fft = Day16.fft(new int[]{1,2,3,4,5,6,7,8}, 1);
    assertArrayEquals(exp, fft);

    exp = new int[]{3,4,0,4,0,4,3,8};
    fft = Day16.fft(new int[]{1,2,3,4,5,6,7,8}, 2);
    assertArrayEquals(exp, fft);

    exp = new int[]{0,3,4,1,5,5,1,8};
    fft = Day16.fft(new int[]{1,2,3,4,5,6,7,8}, 3);
    assertArrayEquals(exp, fft);

    exp = new int[]{0,1,0,2,9,4,9,8};
    fft = Day16.fft(new int[]{1,2,3,4,5,6,7,8}, 4);
    assertArrayEquals(exp, fft);

    exp = new int[]{2,4,1,7,6,1,7,6};
    fft = Day16.fft(Day16.toInts("80871224585914546619083218645595"), 100);
    assertArrayEquals(exp, Arrays.copyOfRange(fft, 0, 8));
  }

  @Test
  void testFft_Signal() {
    var exp = new int[]{2, 8, 4, 3, 0, 1, 4, 6};
    var fft = Day16.fft(SIGNAL, 100);
    var first8 = Arrays.copyOfRange(fft, 0, 8);
    System.out.println(Arrays.toString(first8));
    assertArrayEquals(exp, first8);
  }

//  @Test
//  void testFft2_Signal() {
//    var exp = new int[]{2, 8, 4, 3, 0, 1, 4, 6};
//    var fft = Day16.fft(SIGNAL, 10000, 100);
//    //var first8 = Arrays.copyOfRange(fft, 0, 8);
//    System.out.println(Arrays.toString(fft));
//    assertArrayEquals(exp, fft);
//  }
}

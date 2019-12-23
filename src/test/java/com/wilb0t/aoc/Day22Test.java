package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.google.common.collect.Lists;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day22Test {
 
  private static List<String> steps;

  @BeforeAll
  static void initAll() throws Exception {
    steps =
        Files.readAllLines(
            Path.of(Day1Test.class.getResource("/Day22_input1.txt").toURI()),
            StandardCharsets.UTF_8);
  }

  @BeforeEach
  void init() {
  }

  @Test
  void testShuffleParse_Case4() {
    var steps = List.of(
        "deal into new stack",
        "cut -2",
        "deal with increment 7",
        "cut 8",
        "cut -4",
        "deal with increment 7",
        "cut 3",
        "deal with increment 9",
        "deal with increment 3",
        "cut -1 "
    );
    assertThat(Day22.shuffle(steps, 10, 9), is(0L));
    assertThat(Day22.shuffle(steps, 10, 6), is(9L));
  }
  
  @Test
  void testDealIntoNewStack_Part2() {
    assertThat(Day22.dealIntoNewStack(10, 0), is(9L));
    assertThat(Day22.dealIntoNewStack(10, 9), is(0L));
  }
  
  @Test
  void testCut_Part2() {
    assertThat(Day22.cutCards(3, 10, 0), is(7L));
    assertThat(Day22.cutCards(3, 10, 9), is(6L));
  }
  
  @Test
  void testDealWithInc_Part2() {
    assertThat(Day22.dealWithInc(3, 10, 0), is(0L));
    assertThat(Day22.dealWithInc(3, 10, 9), is(7L));
  }
  
  @Test
  void testShuffle_Part1() {
    var shuffled = Day22.shuffle(steps, 10007, 2019);
    assertThat(shuffled, is(8326L));
  }

  @Test
  void testFindRepeat() {
    assertThat(Day22.findRepeat(steps), is(0L));
  }
}

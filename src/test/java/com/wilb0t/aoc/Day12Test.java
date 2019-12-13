package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day12Test {

  private Set<Day12.Moon> input1;

  private Set<Day12.Moon> testInput1;

  private Set<Day12.Moon> testInput2;

  @BeforeEach
  void init() {
    input1 =
        Set.of(
            new Day12.Moon("Io", -16, -1, -12, 0, 0, 0),
            new Day12.Moon("Europa", 0, -4, -17, 0, 0, 0),
            new Day12.Moon("Ganymede", -11, 11, 0, 0, 0, 0),
            new Day12.Moon("Callisto", 2, 2, -6, 0, 0, 0)
        );

    testInput1 =
        Set.of(
            new Day12.Moon("Io", -1, 0, 2, 0, 0, 0),
            new Day12.Moon("Europa", 2, -10, -7, 0, 0, 0),
            new Day12.Moon("Ganymede", 4, -8, 8, 0, 0, 0),
            new Day12.Moon("Callisto", 3, 5, -1, 0, 0, 0)
        );

    testInput2 =
        Set.of(
            new Day12.Moon("Io", -8, -10, 0, 0, 0, 0),
            new Day12.Moon("Europa", 5, 5, 10, 0, 0, 0),
            new Day12.Moon("Ganymede", 2, -7, 3, 0, 0, 0),
            new Day12.Moon("Callisto", 9, -8, -3, 0, 0, 0)
        );
  }

  @Test
  void testStep_Case1() {
    var exp = Set.of(
        new Day12.Moon("Io", 2, -1, 1, 3, -1, -1),
        new Day12.Moon("Europa", 3, -7, -4, 1, 3, 3),
        new Day12.Moon("Ganymede", 1, -7, 5, -3, 1, -3),
        new Day12.Moon("Callisto", 2, 2, 0, -1, -3, 1)
    );

    Day12.simulate(testInput1, 1);
    assertThat(testInput1, is(exp));
  }

  @Test
  void testTotalEnergy_Case1() {
    Day12.simulate(testInput1, 10);
    assertThat(Day12.totalEnergy(testInput1), is(179L));
  }

  @Test
  void testTotalEnergy_Input1() {
    Day12.simulate(input1, 1000);
    assertThat(Day12.totalEnergy(input1), is(5517L));
  }

  @Test
  void testFindCycle_Case1() {
    var repeat = Day12.findCycle(testInput1);
    assertThat(repeat, is(2772L));
  }

  @Test
  void testFindCycle_Case2() {
    var repeat = Day12.findCycle(testInput2);
    assertThat(repeat, is(4686774924L));
  }

  @Test
  void testFindCycle_Input1() {
    var repeat = Day12.findCycle(input1);
    assertThat(repeat, is(303070460651184L));
  }
}

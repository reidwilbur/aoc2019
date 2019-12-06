package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day6Test {

  private static final List<String> testInput =
      List.of(
          "COM)B",
          "B)C",
          "C)D",
          "D)E",
          "E)F",
          "B)G",
          "G)H",
          "D)I",
          "E)J",
          "J)K",
          "K)L"
      );

  private static final List<String> testInput2 =
      List.of(
          "COM)B",
          "B)C",
          "C)D",
          "D)E",
          "E)F",
          "B)G",
          "G)H",
          "D)I",
          "E)J",
          "J)K",
          "K)L",
          "K)YOU",
          "I)SAN"
      );

  private static List<String> input1;

  @BeforeAll
  static void initAll() throws Exception {
    input1 = Files.readAllLines(
        Path.of(Day1Test.class.getResource("/Day6_input1.txt").toURI()),
        StandardCharsets.UTF_8
    );
  }

  @BeforeEach
  void init() {
  }

  @Test
  void testToChildMap() {
    var expMap = Map.of(
        "COM", Set.of("B"),
        "B", Set.of("C", "G"),
        "C", Set.of("D"),
        "D", Set.of("E", "I"),
        "E", Set.of("F", "J"),
        "G", Set.of("H"),
        "J", Set.of("K"),
        "K", Set.of("L")
    );
    assertThat(Day6.toChildMap(testInput), is(expMap));
  }

  @Test
  void testToParMap() {
    var expMap = Map.ofEntries(
        new AbstractMap.SimpleEntry<>("B", "COM"),
        new AbstractMap.SimpleEntry<>("C", "B"),
        new AbstractMap.SimpleEntry<>("D", "C"),
        new AbstractMap.SimpleEntry<>("E", "D"),
        new AbstractMap.SimpleEntry<>("F", "E"),
        new AbstractMap.SimpleEntry<>("G", "B"),
        new AbstractMap.SimpleEntry<>("H", "G"),
        new AbstractMap.SimpleEntry<>("I", "D"),
        new AbstractMap.SimpleEntry<>("J", "E"),
        new AbstractMap.SimpleEntry<>("K", "J"),
        new AbstractMap.SimpleEntry<>("L", "K")
    );
    assertThat(Day6.toParMap(testInput), is(expMap));
  }

  @Test
  void testGetOrbitCount_Case1() {
    assertThat(Day6.getOrbitCount("COM", testInput), is(42));
  }

  @Test
  void testGetOrbitCount_Part1() {
    assertThat(Day6.getOrbitCount("COM", input1), is(621125));
  }

  @Test
  void testTxfrCount_Case1() {
    assertThat(Day6.getTxferCount("COM", "SAN", "YOU", testInput2), is(4));
  }

  @Test
  void testTxfrCount_Part2() {
    assertThat(Day6.getTxferCount("COM", "SAN", "YOU", input1), is(550));
  }
}

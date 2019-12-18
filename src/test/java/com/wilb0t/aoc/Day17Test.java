package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day17Test {

  static List<Long> code;

  static List<Character> testMap =
      List.of(
          "#######...#####\n",
          "#.....#...#...#\n",
          "#.....#...#...#\n",
          "......#...#...#\n",
          "......#...###.#\n",
          "......#.....#.#\n",
          "^########...#.#\n",
          "......#.#...#.#\n",
          "......#########\n",
          "........#...#..\n",
          "....#########..\n",
          "....#...#......\n",
          "....#...#......\n",
          "....#...#......\n",
          "....#####......\n"
      ).stream()
      .flatMap(line -> line.chars().mapToObj(i -> (char) i))
      .collect(Collectors.toList());


  @BeforeAll
  static void initAll() throws Exception {
    code =
        Files.readAllLines(
            Path.of(Day1Test.class.getResource("/Day17_input1.txt").toURI()),
            StandardCharsets.UTF_8)
            .stream()
            .flatMap(s -> Stream.of(s.split(",")))
            .map(Long::parseLong)
            .collect(Collectors.toList());
  }

  @BeforeEach
  void init() {
  }

  @Test
  void testGetAlignmentParams() {
    assertThat(Day17.getAlignmentParams(code), is(3192));
  }

  @Test
  void testToCmds() {
    assertThat(
        Day17.toCmds(testMap),
        is(List.of("R","8","R","8","R","4","R","4","R","8","L","6","L","2","R","4","R","4","R","8","R","8","R","8","L","6","L","2"))
    );
  }

  @Test
  void testToCmds_Input1() {
    var map = Day17.getMap(code);
    var cmds = Day17.toCmds(map);
    assertThat(
        cmds,
        is(List.of("L","4","R","8","L","6","L","10","L","6","R","8","R","10","L","6","L","6","L","4","R","8","L","6","L","10","L","6","R","8","R","10","L","6","L","6","L","4","L","4","L","10","L","4","L","4","L","10","L","6","R","8","R","10","L","6","L","6","L","4","R","8","L","6","L","10","L","6","R","8","R","10","L","6","L","6","L","4","L","4","L","10"))
    );
  }

  @Test
  void testGetDust() throws InterruptedException {
    assertThat(Day17.getDust(code), is(684691L));
  }
}

package com.wilb0t.aoc;

import static com.wilb0t.aoc.Day15.Loc;
import static com.wilb0t.aoc.Day15.getSectionMap;
import static com.wilb0t.aoc.Day15.minStepsToOxy;
import static com.wilb0t.aoc.Day15.timeToFill;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day15Test {

  static List<Long> code;

  static Map<Loc, Long> map;

  @BeforeAll
  static void initAll() throws Exception {
    code =
        Files.readAllLines(
            Path.of(Day1Test.class.getResource("/Day15_input1.txt").toURI()),
            StandardCharsets.UTF_8)
            .stream()
            .flatMap(s -> Stream.of(s.split(",")))
            .map(Long::parseLong)
            .collect(Collectors.toList());

    map = getSectionMap(code);
  }

  @BeforeEach
  void init() {
  }

  @Test
  void testMinStepsToOxy() throws InterruptedException {
    //Day15.printMap(map, Day15.Loc.of(0,0));
    assertThat(minStepsToOxy(map), is(294));
  }

  @Test
  void testTimeToFill() {
    assertThat(timeToFill(map), is(388));
  }
}

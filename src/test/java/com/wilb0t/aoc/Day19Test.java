package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day19Test {

  private static List<Long> code;

  @BeforeAll
  static void initAll() throws Exception {
    code =
        Files.readAllLines(
            Path.of(Day1Test.class.getResource("/Day19_input1.txt").toURI()),
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
  void testScan() throws InterruptedException {
    assertThat(Day19.scan(code), is(156));
  }

  @Test
  void testGetLine() throws InterruptedException {
    var coords = Day19.search(code);
    assertThat(coords, is(new AbstractMap.SimpleEntry<>(261, 980)));
    assertThat(coords.getKey() * 10000 + coords.getValue(), is(2610980));
  }
}

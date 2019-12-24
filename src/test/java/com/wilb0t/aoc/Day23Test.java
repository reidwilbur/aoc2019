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

class Day23Test {
 
  private static List<Long> code;

  @BeforeAll
  static void initAll() throws Exception {
    code =
        Files.readAllLines(
            Path.of(Day1Test.class.getResource("/Day23_input1.txt").toURI()),
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
  void testRunNetwork() {
    assertThat(Day23.runNetwork(code), is(18513L));
  }

  @Test
  void testRunNetworkWithNat() {
    assertThat(Day23.runNetworkWithNat(code), is(13286L));
  }
}

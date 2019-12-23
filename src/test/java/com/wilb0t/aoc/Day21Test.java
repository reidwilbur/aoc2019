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

class Day21Test {
  
  private static List<Long> code;

  @BeforeAll
  static void initAll() throws Exception {
    code =
        Files.readAllLines(
            Path.of(Day1Test.class.getResource("/Day21_input1.txt").toURI()),
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
  void testGetDamageWalk() {
    assertThat(Day21.getDamageWalk(code), is(19357544L));
  }
  
  @Test
  void testGetDamageRun() {
    assertThat(Day21.getDamageRun(code), is(19357544L));
  }
}

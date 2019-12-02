package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day1Test {

  private static List<Long> input1;

  @BeforeAll
  static void initAll() throws Exception {
    input1 = Files.readAllLines(
        Path.of(Day1Test.class.getResource("/Day1_input1.txt").toURI()),
        StandardCharsets.UTF_8
    ).stream().map(Long::parseLong).collect(Collectors.toList());
  }

  @BeforeEach
  void init() {
  }

  @Test
  void test_getFuelForModule() {
    assertThat(Day1.getFuelForModule(12), is(2L));
    assertThat(Day1.getFuelForModule(14), is(2L));
    assertThat(Day1.getFuelForModule(1969), is(654L));
    assertThat(Day1.getFuelForModule(100756), is(33583L));
  }

  @Test
  void test_Part1() {
    assertThat(Day1.getFuelForModules(input1), is(3347838L));
  }

  @Test
  void test_getTotalFuelForModule() {
    assertThat(Day1.getTotalFuelForModule(14), is(2L));
    assertThat(Day1.getTotalFuelForModule(1969), is(966L));
    assertThat(Day1.getTotalFuelForModule(100756), is(50346L));
  }

  @Test
  void test_Part2() {
    assertThat(Day1.getTotalFuelForModules(input1), is(5018888L));
  }
}

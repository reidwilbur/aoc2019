package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.Lists;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day5Test {

  private static List<Integer> code;

  private Day5.Computer computer;

  @BeforeAll
  static void initAll() throws Exception {
    code =
        Files.readAllLines(
                Path.of(Day1Test.class.getResource("/Day5_input1.txt").toURI()),
                StandardCharsets.UTF_8)
            .stream()
            .flatMap(s -> Stream.of(s.split(",")))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
  }

  @BeforeEach
  void init() {
    computer = new Day5.Computer();
  }

  @Test
  void testExec_case1() {
    var code = Lists.newArrayList(3, 0, 4, 0, 99);
    assertThat(computer.exec(code, List.of(33).iterator()), is(List.of(33)));
  }

  @Test
  void testExec_case2() {
    var code = Lists.newArrayList(1002, 4, 3, 4, 33);
    assertThat(computer.exec(code, List.of(0).iterator()), is(List.of()));
  }

  @Test
  void testExec_case3() {
    var code = Lists.newArrayList(1101, 100, -1, 4, 0);
    assertThat(computer.exec(code, List.of(0).iterator()), is(List.of()));
  }

  @Test
  void testPart1() {
    var localCode = new ArrayList<>(code);
    var output = computer.exec(localCode, List.of(1).iterator());
    assertTrue(output.subList(0, output.size() - 1).stream().allMatch(code -> code.equals(0)));
    assertThat(output.get(output.size() - 1), is(4601506));
  }

  @Test
  void testExec_part2_case1() {
    var code = Lists.newArrayList(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8);
    assertThat(computer.exec(new ArrayList<>(code), List.of(0).iterator()), is(List.of(0)));
    assertThat(computer.exec(new ArrayList<>(code), List.of(8).iterator()), is(List.of(1)));
  }

  @Test
  void testExec_part2_case2() {
    var code = Lists.newArrayList(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8);
    assertThat(computer.exec(new ArrayList<>(code), List.of(0).iterator()), is(List.of(1)));
    assertThat(computer.exec(new ArrayList<>(code), List.of(8).iterator()), is(List.of(0)));
  }

  @Test
  void testExec_part2_case3() {
    var code = Lists.newArrayList(3, 3, 1108, -1, 8, 3, 4, 3, 99);
    assertThat(computer.exec(new ArrayList<>(code), List.of(0).iterator()), is(List.of(0)));
    assertThat(computer.exec(new ArrayList<>(code), List.of(8).iterator()), is(List.of(1)));
  }

  @Test
  void testExec_part2_case4() {
    var code = Lists.newArrayList(3, 3, 1107, -1, 8, 3, 4, 3, 99);
    assertThat(computer.exec(new ArrayList<>(code), List.of(0).iterator()), is(List.of(1)));
    assertThat(computer.exec(new ArrayList<>(code), List.of(8).iterator()), is(List.of(0)));
  }

  @Test
  void testExec_part2_case5() {
    var code = Lists.newArrayList(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9);
    assertThat(computer.exec(new ArrayList<>(code), List.of(0).iterator()), is(List.of(0)));
    assertThat(computer.exec(new ArrayList<>(code), List.of(2).iterator()), is(List.of(1)));
  }

  @Test
  void testExec_part2_case6() {
    var code = Lists.newArrayList(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1);
    assertThat(computer.exec(new ArrayList<>(code), List.of(0).iterator()), is(List.of(0)));
    assertThat(computer.exec(new ArrayList<>(code), List.of(2).iterator()), is(List.of(1)));
  }

  @Test
  void testExec_part2_case7() {
    var code =
        Lists.newArrayList(
            3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31, 1106, 0, 36, 98, 0,
            0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104, 999, 1105, 1, 46, 1101, 1000, 1, 20, 4,
            20, 1105, 1, 46, 98, 99);
    assertThat(computer.exec(new ArrayList<>(code), List.of(0).iterator()), is(List.of(999)));
    assertThat(computer.exec(new ArrayList<>(code), List.of(8).iterator()), is(List.of(1000)));
    assertThat(computer.exec(new ArrayList<>(code), List.of(9).iterator()), is(List.of(1001)));
  }

  @Test
  void testPart2() {
    var localCode = new ArrayList<>(code);
    var output = computer.exec(localCode, List.of(5).iterator());
    assertTrue(output.subList(0, output.size() - 1).stream().allMatch(code -> code.equals(0)));
    assertThat(output.get(output.size() - 1), is(5525561));
  }
}

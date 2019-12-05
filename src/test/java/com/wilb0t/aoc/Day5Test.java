package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.Lists;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
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
    var localCode = new ArrayList<Integer>(code);
    var output = computer.exec(localCode, List.of(1).iterator());
    assertTrue(output.subList(0, output.size() - 1).stream().allMatch(code -> code.equals(0)));
    assertThat(output.get(output.size() - 1), is(4601506));
  }
}

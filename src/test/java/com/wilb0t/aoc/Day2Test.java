package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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

public class Day2Test {

  private static List<Integer> input1;

  private Day2.Computer computer;

  @BeforeAll
  static void initAll() throws Exception {
    input1 =
        Files.readAllLines(
                Path.of(Day1Test.class.getResource("/Day2_input1.txt").toURI()),
                StandardCharsets.UTF_8)
            .stream()
            .flatMap(s -> Stream.of(s.split(",")))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
  }

  @BeforeEach
  void init() {
    computer = new Day2.Computer();
  }

  @Test
  void testExec_case1() {
    var code = Lists.newArrayList(1, 0, 0, 0, 99);
    assertThat(computer.exec(code), is(List.of(2, 0, 0, 0, 99)));
  }

  @Test
  void testExec_case2() {
    var code = Lists.newArrayList(2, 3, 0, 3, 99);
    assertThat(computer.exec(code), is(List.of(2, 3, 0, 6, 99)));
  }

  @Test
  void testExec_case3() {
    var code = Lists.newArrayList(2, 4, 4, 5, 99, 0);
    assertThat(computer.exec(code), is(List.of(2, 4, 4, 5, 99, 9801)));
  }

  @Test
  void testExec_case4() {
    var code = Lists.newArrayList(1, 1, 1, 4, 99, 5, 6, 0, 99);
    assertThat(computer.exec(code), is(List.of(30, 1, 1, 4, 2, 5, 6, 0, 99)));
  }

  @Test
  void testExec_input1() {
    var code = new ArrayList<>(input1);
    code.set(1, 12);
    code.set(2, 2);
    assertThat(computer.exec(code).get(0), is(5434663));
  }

  @Test
  void testFindNounVerb() {
    assertThat(computer.findNounVerb(input1, 19690720), is(4559));
  }
}

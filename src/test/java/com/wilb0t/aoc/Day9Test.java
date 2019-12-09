package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day9Test {

  private static List<Long> code;

  private Day9.Computer computer;
  private BlockingDeque<Long> input;
  private BlockingDeque<Long> output;

  @BeforeAll
  static void initAll() throws Exception {
    code =
        Files.readAllLines(
            Path.of(Day1Test.class.getResource("/Day9_input1.txt").toURI()),
            StandardCharsets.UTF_8)
            .stream()
            .flatMap(s -> Stream.of(s.split(",")))
            .map(Long::parseLong)
            .collect(Collectors.toList());
  }

  @BeforeEach
  void init() {
    computer = new Day9.Computer();
    input = new LinkedBlockingDeque<>();
    output = new LinkedBlockingDeque<>();
  }

  @Test
  void testExec_Case1() {
    var code = List.of(109L,1L,204L,-1L,1001L,100L,1L,100L,1008L,100L,16L,101L,1006L,101L,0L,99L);

    computer.exec("A", code, input, output);

    var outputVals = new ArrayList<>();
    output.drainTo(outputVals);
    assertThat(outputVals, is(code));
  }

  @Test
  void testExec_Case2() {
    var code = List.of(1102L,34915192L,34915192L,7L,4L,7L,99L,0L);

    computer.exec("A", code, input, output);

    assertThat(output.pop().toString().length(), is(16));
  }

  @Test
  void testExec_Case3() {
    var code = List.of(104L,1125899906842624L,99L);

    computer.exec("A", code, input, output);

    var outputVals = new ArrayList<>();
    output.drainTo(outputVals);
    assertThat(outputVals, is(List.of(1125899906842624L)));
  }

  @Test
  void testExec_Part1() {
    input.add(1L);
    computer.exec("A", code, input, output);

    var outputVals = new ArrayList<>();
    output.drainTo(outputVals);
    assertThat(outputVals, is(List.of(3533056970L)));
  }

  @Test
  void testExec_Part2() {
    input.add(2L);
    computer.exec("A", code, input, output);

    var outputVals = new ArrayList<>();
    output.drainTo(outputVals);
    assertThat(outputVals, is(List.of(72852L)));
  }
}

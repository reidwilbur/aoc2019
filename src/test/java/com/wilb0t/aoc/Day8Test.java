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

public class Day8Test {

  private static List<Character> input1;

  @BeforeAll
  static void initAll() throws Exception {
    input1 = Files.readAllLines(
        Path.of(Day1Test.class.getResource("/Day8_input1.txt").toURI()),
        StandardCharsets.UTF_8
    ).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
  }

  @BeforeEach
  void init() {
  }

  @Test
  void testVerify_Part1() {
    assertThat(Day8.verify(25, 6, input1), is(1330L));
  }

  @Test
  void testPrintImg() {
    Day8.printImg(25, 6, input1);
  }
}

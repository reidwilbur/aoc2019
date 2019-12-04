package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day4Test {

  private static final int start = 264793;
  private static final int end = 803935;

  @BeforeEach
  void init() {}

  @Test
  void testFilters_Case1() {
    var pswd = 111111;
    assertTrue(Day4.hasAdjRepeat(String.valueOf(pswd)));
    assertTrue(Day4.digitsInc(String.valueOf(pswd)));
    assertThat(Day4.getPasswds(pswd, pswd + 1), is(List.of(String.valueOf(pswd))));
  }

  @Test
  void testFilters_Case2() {
    var pswd = 223450;
    assertTrue(Day4.hasAdjRepeat(String.valueOf(pswd)));
    assertFalse(Day4.digitsInc(String.valueOf(pswd)));
    assertThat(Day4.getPasswds(pswd, pswd + 1), is(List.of()));
  }

  @Test
  void testFilters_Case3() {
    var pswd = 123789;
    assertFalse(Day4.hasAdjRepeat(String.valueOf(pswd)));
    assertTrue(Day4.digitsInc(String.valueOf(pswd)));
    assertThat(Day4.getPasswds(pswd, pswd + 1), is(List.of()));
  }

  @Test
  void testGetPasswds_Part1() {
    assertThat(Day4.getPasswds(start, end).size(), is(966));
  }

  @Test
  void testFilters_Double_Case1() {
    var pswd = 112233;
    assertTrue(Day4.hasDouble(String.valueOf(pswd)));
    assertTrue(Day4.digitsInc(String.valueOf(pswd)));
    assertThat(Day4.getPasswdsPart2(pswd, pswd + 1), is(List.of("112233")));
  }

  @Test
  void testFilters_Double_Case2() {
    var pswd = 123444;
    assertFalse(Day4.hasDouble(String.valueOf(pswd)));
    assertTrue(Day4.digitsInc(String.valueOf(pswd)));
    assertThat(Day4.getPasswdsPart2(pswd, pswd + 1), is(List.of()));
  }

  @Test
  void testHasDouble() {
    assertFalse(Day4.hasDouble("111111"));
    assertFalse(Day4.hasDouble("111444"));
    assertFalse(Day4.hasDouble("111234"));
    assertFalse(Day4.hasDouble("122234"));
    assertFalse(Day4.hasDouble("123334"));
    assertFalse(Day4.hasDouble("123444"));
    assertFalse(Day4.hasDouble("123456"));
    assertTrue(Day4.hasDouble("111122"));
    assertTrue(Day4.hasDouble("112233"));
    assertTrue(Day4.hasDouble("123344"));
    assertTrue(Day4.hasDouble("123455"));
  }

  @Test
  void testPart2_Case3() {
    var pswd = 111122;
    assertThat(Day4.getPasswdsPart2(pswd, pswd + 1), is(List.of("111122")));
  }

  @Test
  void testGetPasswds_Part2() {
    assertThat(Day4.getPasswdsPart2(start, end).size(), is(628));
  }
}

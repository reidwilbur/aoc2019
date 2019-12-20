package com.wilb0t.aoc;

import static com.wilb0t.aoc.Day20.Pos;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day20Test {

  private static List<String> map;
  private static List<String> testMap1;

  @BeforeAll
  static void initAll() throws Exception {
    map =
        Files.readAllLines(
            Path.of(Day1Test.class.getResource("/Day20_input1.txt").toURI()),
            StandardCharsets.UTF_8);

    testMap1 =
        Files.readAllLines(
            Path.of(Day1Test.class.getResource("/Day20_testinput1.txt").toURI()),
            StandardCharsets.UTF_8);
  }

  @BeforeEach
  void init() {
  }

  @Test
  void testGetPortals_TestMap1() {
    var portals = Day20.Portal.getPortals(testMap1);
    var exp = Set.of(
        new PortalBuilder().name("AA").pstns(Pos.of(9, 1)).build(),
        new PortalBuilder().name("BC").pstns(Pos.of(9, 7), Pos.of(1,8)).build(),
        new PortalBuilder().name("DE").pstns(Pos.of(7, 10), Pos.of(1,13)).build(),
        new PortalBuilder().name("FG").pstns(Pos.of(1, 15), Pos.of(11,11)).build(),
        new PortalBuilder().name("ZZ").pstns(Pos.of(13, 17)).build()
    );
    assertThat(portals, is(exp));
  }
  
  @Test
  public void testGetDist_TestMap1() {
    assertThat(Day20.getDist("AA", "ZZ", testMap1), is(23));
  }

  @Test
  public void testGetDist_Map1() {
    assertThat(Day20.getDist("AA", "ZZ", map), is(684));
  }
}

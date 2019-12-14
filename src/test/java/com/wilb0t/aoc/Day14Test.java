package com.wilb0t.aoc;

import static com.wilb0t.aoc.Day14.Chem.chem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Day14Test {

  static Map<Day14.Chem, List<Day14.Chem>> input1;

  @BeforeAll
  static void initAll() throws Exception {
    input1 =
        Day14.toMap(
            Files.readAllLines(
                Path.of(Day1Test.class.getResource("/Day14_input1.txt").toURI()),
                StandardCharsets.UTF_8));
  }

  @BeforeEach
  void init() {
  }

  @Test
  void testToMap() {
    var lines = List.of(
        "10 ORE => 10 A",
        "1 ORE => 1 B",
        "7 A, 1 B => 1 C",
        "7 A, 1 C => 1 D",
        "7 A, 1 D => 1 E",
        "7 A, 1 E => 1 FUEL"
    );
    var exp = Map.of(
        chem("A", 10), List.of(chem("ORE", 10)),
        chem("B", 1), List.of(chem("ORE", 1)),
        chem("C", 1), List.of(chem("A", 7), chem("B", 1)),
        chem("D", 1), List.of(chem("A", 7), chem("C", 1)),
        chem("E", 1), List.of(chem("A", 7), chem("D", 1)),
        chem("FUEL", 1), List.of(chem("A", 7), chem("E", 1))
    );

    assertThat(Day14.toMap(lines), is(exp));
  }

  @Test
  void testGetOreForChem_Case1() {
    var rxns = Map.of(
        chem("A", 10), List.of(chem("ORE", 10)),
        chem("B", 1), List.of(chem("ORE", 1)),
        chem("C", 1), List.of(chem("A", 7), chem("B", 1)),
        chem("D", 1), List.of(chem("A", 7), chem("C", 1)),
        chem("E", 1), List.of(chem("A", 7), chem("D", 1)),
        chem("FUEL", 1), List.of(chem("A", 7), chem("E", 1))
    );

    assertThat(Day14.getOreForChem("FUEL", 1, rxns, new HashMap<>()), is(31L));
  }

  @Test
  void testGetOreForChem_Input1() {
    assertThat(
        Day14.getOreForChem("FUEL", 1, input1, new HashMap<>()),
        is(374457L)
    );
  }

  @Test
  void testGetFuelForOre() {
    assertThat(Day14.getFuelForOre(Day14.ONE_TL, input1), is(3568888L));
  }
}

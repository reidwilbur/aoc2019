package com.wilb0t.aoc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day10Test {

  private static Set<Day10.Asteroid> input1;

  private static Set<Day10.Asteroid> destroyInput;

  private static Set<Day10.Asteroid> destroyInput2;

  @BeforeAll
  static void initAll() throws Exception {
    input1 =
        Day10.toAsteroids(
            Files.readAllLines(
                Path.of(Day1Test.class.getResource("/Day10_input1.txt").toURI()),
                StandardCharsets.UTF_8)
        );


    destroyInput =
        Day10.toAsteroids(
            List.of(
                ".#..##.###...#######",
                "##.############..##.",
                ".#.######.########.#",
                ".###.#######.####.#.",
                "#####.##.#.##.###.##",
                "..#####..#.#########",
                "####################",
                "#.####....###.#.#.##",
                "##.#################",
                "#####.##.###..####..",
                "..######..##.#######",
                "####.##.####...##..#",
                ".#####..#.######.###",
                "##...#.##########...",
                "#.##########.#######",
                ".####.#.###.###.#.##",
                "....##.##.###..#####",
                ".#.#.###########.###",
                "#.#.#.#####.####.###",
                "###.##.####.##.#..##"
            ));

    destroyInput2 =
        Day10.toAsteroids(
            List.of(
                ".#....#####...#..",
                "##...##.#####..##",
                "##...#...#.#####.",
                "..#.....#...###..", //8,3
                "..#.#.....#....##"
            ));
  }

  @BeforeEach
  void init() {
  }

  @Test
  void testToAsteroids() {
    var input = List.of(
        ".#..#",
        ".....",
        "#####",
        "....#",
        "...##"
    );

    var exp = Set.of(
        new AsteroidBuilder().y(0).x(1).build(),
        new AsteroidBuilder().y(0).x(4).build(),
        new AsteroidBuilder().y(2).x(0).build(),
        new AsteroidBuilder().y(2).x(1).build(),
        new AsteroidBuilder().y(2).x(2).build(),
        new AsteroidBuilder().y(2).x(3).build(),
        new AsteroidBuilder().y(2).x(4).build(),
        new AsteroidBuilder().y(3).x(4).build(),
        new AsteroidBuilder().y(4).x(3).build(),
        new AsteroidBuilder().y(4).x(4).build()
    );
    assertThat(Day10.toAsteroids(input), is(exp));
  }

  @Test
  void testGetVisibleAsteroidCount_Case1() {
    var input = List.of(
        ".#..#",
        ".....",
        "#####",
        "....#",
        "...##"
    );

    var base = new AsteroidBuilder().x(1).y(0).build();
    assertThat(Day10.getVisibleAsteroidCount(base, Day10.toAsteroids(input)), is(7));

    base = new AsteroidBuilder().x(3).y(4).build();
    assertThat(Day10.getVisibleAsteroidCount(base, Day10.toAsteroids(input)), is(8));
  }

  @Test
  void testGetVisibleAsteroidCount_input1() {
    assertThat(Day10.getLargestAsteroidCount(input1), is(new AbstractMap.SimpleEntry<>(319, asteroid(31, 20))));
  }

  @Test
  void testGetDestroyedAsteroid_Part2() {
    var base = asteroid(31, 20);
    assertThat(Day10.getDestroyedAsteroid(base, input1, 200), is(asteroid(5, 17)));
  }

  @Test
  void testGetDestroyedAsteroid_TestInput1() {
    var base = asteroid(11, 13);
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput, 1), is(asteroid(11, 12)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput, 2), is(asteroid(12, 1)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput, 100), is(asteroid(10, 16)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput, 200), is(asteroid(8, 2)));
  }

  @Test
  void testGetDestroyedAsteroid_TestInput2() {
    var base = asteroid(8, 3);
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 1), is(asteroid(8, 1)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 2), is(asteroid(9, 0)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 3), is(asteroid(9, 1)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 4), is(asteroid(10, 0)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 5), is(asteroid(9, 2)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 6), is(asteroid(11, 1)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 7), is(asteroid(12, 1)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 8), is(asteroid(11, 2)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 9), is(asteroid(15, 1)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 10), is(asteroid(12, 2)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 11), is(asteroid(13, 2)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 12), is(asteroid(14, 2)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 13), is(asteroid(15, 2)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 14), is(asteroid(12, 3)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 15), is(asteroid(16, 4)));
    assertThat(Day10.getDestroyedAsteroid(base, destroyInput2, 16), is(asteroid(15, 4)));
  }

  Day10.Asteroid asteroid(int x, int y) {
    return new AsteroidBuilder().x(x).y(y).build();
  }
}

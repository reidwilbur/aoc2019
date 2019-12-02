package com.wilb0t.aoc;

import java.util.List;

class Day1 {

  static long getFuelForModule(long mass) {
    return Math.max((mass / 3) - 2, 0);
  }

  static long getFuelForModules(List<Long> masses) {
    return masses.stream().mapToLong(Day1::getFuelForModule).sum();
  }

  static long getTotalFuelForModule(long mass) {
    if (mass > 0) {
      var totalFuel = getFuelForModule(mass);
      return totalFuel + getTotalFuelForModule(totalFuel);
    } else {
      return 0;
    }
  }

  static long getTotalFuelForModules(List<Long> masses) {
    return masses.stream().mapToLong(Day1::getTotalFuelForModule).sum();
  }
}

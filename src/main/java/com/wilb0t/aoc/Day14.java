package com.wilb0t.aoc;

import io.norberg.automatter.AutoMatter;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14 {

  static final long ONE_TL = 1_000_000_000_000L;

  @AutoMatter
  interface Chem {
    int amt();

    String name();

    static Chem chem(String name, int amt) {
      return new ChemBuilder().name(name).amt(amt).build();
    }

    static Chem chem(String chemStr) {
      var parts = chemStr.split(" ");
      return new ChemBuilder().name(parts[1]).amt(Integer.parseInt(parts[0])).build();
    }
  }

  static Map<Chem, List<Chem>> toMap(List<String> input) {
    return input.stream()
        .map(
            line -> {
              var split = line.split(" => ");
              var chem = Chem.chem(split[1]);
              var chemStrs = split[0].split(", ");
              var chems = Arrays.stream(chemStrs).map(Chem::chem).collect(Collectors.toList());
              return new AbstractMap.SimpleEntry<>(chem, chems);
            })
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  static long getOreForChem(
      String name,
      long amt,
      Map<Chem, List<Chem>> reactions,
      Map<String, Long> chemAcc
  ) {
    var reaction =
        reactions.entrySet().stream().filter(r -> r.getKey().name().equals(name)).findFirst().get();
    var accAmt = chemAcc.getOrDefault(name, 0L);

    var needAmt = amt;
    if (accAmt <= needAmt) {
      needAmt -= accAmt;
      chemAcc.put(name, 0L);
    } else {
      chemAcc.put(name, accAmt - needAmt);
      needAmt = 0L;
    }

    if (needAmt == 0L) {
      return 0L;
    }

    var reactAmt = reaction.getKey().amt();
    var numReactions = getReactionCount(reactAmt, needAmt);
    var resultAmt = numReactions * reactAmt;
    chemAcc.put(name, resultAmt - needAmt);

    if (reaction.getValue().get(0).name().equals("ORE")) {
      return numReactions * reaction.getValue().get(0).amt();
    } else {
      var oreAmt = 0L;
      for (var chem : reaction.getValue()) {
        var chemAmt = numReactions * chem.amt();
        oreAmt += getOreForChem(chem.name(), chemAmt, reactions, chemAcc);
      }
      return oreAmt;
    }
  }

  static long getFuelForOre(long oreAmt, Map<Chem, List<Chem>> reactions) {
    long fuelAmt = 2L;
    long oreAmtCalc = 0L;
    // get bounds for binary search
    while (oreAmtCalc < oreAmt) {
      oreAmtCalc = getOreForChem("FUEL", fuelAmt, reactions, new HashMap<>());
      fuelAmt = fuelAmt * 2;
    }
    long lowFuelAmt = fuelAmt / 4;
    long hiFuelAmt = fuelAmt / 2;
    // binary search using bounds above for highest fuelAmt needing less than oreAmt
    while (hiFuelAmt != lowFuelAmt) {
      fuelAmt = ((hiFuelAmt - lowFuelAmt) / 2) + lowFuelAmt;
      oreAmtCalc = getOreForChem("FUEL", fuelAmt, reactions, new HashMap<>());
      if (oreAmtCalc < oreAmt) {
        lowFuelAmt = fuelAmt;
      } else {
        hiFuelAmt = fuelAmt - 1;
      }
    }
    return lowFuelAmt;
  }

  static long getReactionCount(long reactAmt, long amt) {
    return (amt / reactAmt) + ((amt % reactAmt > 0) ? 1 : 0);
  }
}

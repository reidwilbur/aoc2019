package com.wilb0t.aoc;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day23 {
  
  static class NonBlockingInput extends LinkedBlockingDeque<Long> {
    @Override
    public Long take() {
      return Optional.ofNullable(pollFirst()).orElse(-1L);
    }
   
    public synchronized void add(Collection<Long> input) {
      addAll(input);
    }
  }
  
  static long runNetwork(List<Long> code) {
    final var nicCount = 50;
    
    var inputs =
        IntStream.range(0, nicCount)
            .mapToObj(i -> {
              var input = new NonBlockingInput();
              input.add((long) i);
              return input;
            })
            .collect(Collectors.toList());
    
    var outputs =
        IntStream.range(0, nicCount)
            .mapToObj(i -> new LinkedBlockingDeque<Long>())
            .collect(Collectors.toList());
    
    var nics = 
        IntStream.range(0, nicCount)
            .mapToObj(i -> new Day9.Computer())
            .collect(Collectors.toList());
    
    var exec = Executors.newFixedThreadPool(nicCount + 1);
    
    var router = 
        CompletableFuture.supplyAsync(
            () -> route(outputs, inputs),
            exec
        );
    
    var nicExecs = 
        IntStream.range(0, nicCount)
          .mapToObj(
              i -> 
                  CompletableFuture.runAsync(
                      () -> nics.get(i).exec(String.valueOf(i), code, inputs.get(i), outputs.get(i)), 
                      exec)
          )
          .collect(Collectors.toList());
  
    var yval = router.join();
    
    exec.shutdownNow();
    
    return yval;
  }
  
  static long route(
      List<LinkedBlockingDeque<Long>> outputs,
      List<NonBlockingInput> inputs
  ) {
    while (true) {
      for (var output : outputs) {
        if (output.size() >= 3) {
          try {
            var addr = output.take();
            var x = output.take();
            var y = output.take();
            if (addr >= 0L && addr < 50L) {
              inputs.get(addr.intValue()).add(List.of(x, y));
            } else if (addr == 255L) {
              return y;
            }
          } catch (InterruptedException e) {
            System.out.println("Router interupted " + e.toString());
          }
        }
      }
    }

  }

  static long runNetworkWithNat(List<Long> code) {
    final var nicCount = 50;

    var inputs =
        IntStream.range(0, nicCount)
            .mapToObj(i -> {
              var input = new NonBlockingInput();
              input.add((long) i);
              return input;
            })
            .collect(Collectors.toList());

    var outputs =
        IntStream.range(0, nicCount)
            .mapToObj(i -> new LinkedBlockingDeque<Long>())
            .collect(Collectors.toList());

    var nics =
        IntStream.range(0, nicCount)
            .mapToObj(i -> new Day9.Computer())
            .collect(Collectors.toList());
    
    var natInput = new LinkedBlockingDeque<Map.Entry<Long, Long>>();

    var exec = Executors.newFixedThreadPool(nicCount + 2);
    
    var router =
        CompletableFuture.runAsync(
            () -> routeWithNat(outputs, inputs, natInput),
            exec
        );
    
    var natVal = 
        CompletableFuture.supplyAsync(
            () -> nat(natInput, inputs, outputs), 
            exec
        );

    var nicExecs =
        IntStream.range(0, nicCount)
            .mapToObj(
                i ->
                    CompletableFuture.runAsync(
                        () -> nics.get(i).exec(String.valueOf(i), code, inputs.get(i), outputs.get(i)),
                        exec)
            )
            .collect(Collectors.toList());

    var natYVal = natVal.join();
    
    exec.shutdownNow();
    
    return natYVal;
  }
  
  static void routeWithNat(
      List<LinkedBlockingDeque<Long>> outputs,
      List<NonBlockingInput> inputs,
      LinkedBlockingDeque<Map.Entry<Long, Long>> natInput
  ) {
    while (true) {
      outputs.forEach(output -> {
        if (output.size() >= 3) {
          try {
            var addr = output.take();
            var x = output.take();
            var y = output.take();
            if (addr == 255) {
              natInput.add(new AbstractMap.SimpleEntry<>(x, y));
            } else {
              inputs.get(addr.intValue()).addAll(List.of(x, y));
            }
          } catch (InterruptedException e) {
          }
        }
      });
    }
  }
  
  static long nat(
      LinkedBlockingDeque<Map.Entry<Long, Long>> natInput, 
      List<NonBlockingInput> inputs, 
      List<LinkedBlockingDeque<Long>> outputs
  ) {
    var lastYval = Long.MIN_VALUE;
    while (true) {
      var networkPossiblyIdle = 
          inputs.stream().allMatch(NonBlockingInput::isEmpty) 
              && outputs.stream().allMatch(LinkedBlockingDeque::isEmpty);
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
      var networkIdle = 
          inputs.stream().allMatch(NonBlockingInput::isEmpty) 
              && outputs.stream().allMatch(LinkedBlockingDeque::isEmpty);
      if (networkPossiblyIdle && networkIdle && !natInput.isEmpty()) {
        synchronized (natInput) {
          Map.Entry<Long, Long> natVal = null;
          try {
            natVal = natInput.takeLast();
          } catch (InterruptedException e) {
          }
          natInput.clear();
          if (lastYval == natVal.getValue()) {
            return natVal.getValue();
          } else {
            lastYval = natVal.getValue();
          }
          inputs.get(0).add(List.of(natVal.getKey(), natVal.getValue()));
        }
      }
    }
  }
}

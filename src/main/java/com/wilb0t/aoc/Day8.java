package com.wilb0t.aoc;

import java.util.List;

public class Day8 {

  static long verify(int width, int height, List<Character> imgData) {
    var minZeroes = Long.MAX_VALUE;
    var minOnes = Long.MIN_VALUE;
    var minTwos = Long.MIN_VALUE;
    var stride = width * height;
    var layers = imgData.size() / (width * height);
    for (int layer = 0; layer < layers; layer ++) {
      var ofs = layer * stride;
      var layerData = imgData.subList(ofs, ofs + stride);
      var zeroes = layerData.stream().filter(c -> c.equals('0')).count();
      if (zeroes < minZeroes) {
        minZeroes = zeroes;
        minOnes = layerData.stream().filter(c -> c.equals('1')).count();
        minTwos = layerData.stream().filter(c -> c.equals('2')).count();
      }
    }
    return minOnes * minTwos;
  }

  static void printImg(int width, int height, List<Character> imgData) {
    var layers = imgData.size() / (width * height);
    var stride = width * height;
    for (int ofs = 0; ofs < stride; ofs++) {
      if (ofs % width == 0) {
        System.out.println();
      }
      for (int layer = 0; layer < layers; layer++) {
        var layerPixel = imgData.get((layer * stride) + ofs);
        if (layerPixel != '2') {
          System.out.print(layerPixel == '1' ? '█' : ' ');
          break;
        }
      }
    }
  }
}

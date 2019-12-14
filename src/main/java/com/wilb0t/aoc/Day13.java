package com.wilb0t.aoc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Day13 {

  static long getBlockCount(List<Long> code) {
    var input = new LinkedBlockingDeque<Long>();
    var output = new LinkedBlockingDeque<Long>();

    var computer = new Day9.Computer();
    computer.exec("a", code, input, output);

    var tiles = new ArrayList<Long>();
    output.drainTo(tiles);

    return IntStream.range(0, tiles.size() / 3)
        .mapToObj(i -> tiles.get(i * 3 + 2))
        .filter(tile -> tile == 2)
        .count();
  }

  static void runGame(List<Long> code) {
    var input = new LinkedBlockingDeque<Long>();
    var output = new LinkedBlockingDeque<Long>();

    var computer = new Day9.Computer();

    var exec = Executors.newFixedThreadPool(3);
    var freePlay = new ArrayList<>(code);
    freePlay.set(0, 2L);
    var compExec = CompletableFuture.runAsync(() -> computer.exec("a", freePlay, input, output));
    var view = new View(input, output);
    var dispExec = CompletableFuture.runAsync(view::readTiles);

    compExec.join();
    output.add(Long.MIN_VALUE);
    exec.shutdownNow();
  }

  static class View extends Canvas {

    static final int TILE_SIZE = 25;
    static final int WIDTH = 38;
    static final int HEIGHT = 22;

    static final Color[] BLOCK_COLORS =
        new Color[] {
          new Color(200, 72, 72),
          new Color(198, 108, 58),
          new Color(180, 122, 48),
          new Color(162, 162, 42),
          new Color(72, 160, 72),
          new Color(66, 72, 200)
        };

    final BlockingDeque<Long> compOut;
    final BlockingDeque<Long> compIn;
    final Map<Map.Entry<Long, Long>, Long> tiles;

    volatile long ballx = 0;
    volatile long paddlex = 0;
    volatile long score = 0;

    public View(BlockingDeque<Long> compIn, BlockingDeque<Long> compOut) {
      this.tiles = new ConcurrentHashMap<>();
      this.compOut = compOut;
      this.compIn = compIn;
      var frame = new JFrame("Intcode Arcade");
      this.setSize(TILE_SIZE * WIDTH, TILE_SIZE * HEIGHT);
      this.setBackground(Color.BLACK);
      frame.add(this);
      frame.pack();
      frame.setVisible(true);
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      this.createBufferStrategy(3);
    }

    void readTiles() {
      while (true) {
        try {
          var x = compOut.take();
          var y = compOut.take();
          var tile = compOut.take();
          if (x == Long.MIN_VALUE || y == Long.MIN_VALUE || tile == Long.MIN_VALUE) {
            return;
          }
          if (x == -1 && y == 0L) {
            score = tile;
            System.out.println("Score " + score);
          } else {
            tiles.put(new AbstractMap.SimpleEntry<>(x, y), tile);
          }
          if (tile == 3L) {
            paddlex = x.intValue();
          }
          if (tile == 4L) {
            ballx = x.intValue();
            compIn.add((long) Long.compare(ballx, paddlex));
          }
          var bufstrat = this.getBufferStrategy();
          var graphics = this.getBufferStrategy().getDrawGraphics();
          this.paint(graphics);
          graphics.dispose();
          bufstrat.show();
          Thread.sleep(1);
        } catch (InterruptedException e) {
          System.out.println("Exception handling comp input " + e);
        }
      }
    }

    @Override
    public void paint(Graphics g) {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, this.getWidth(), this.getHeight());
      tiles.forEach(
          (loc, tile) -> {
            switch (tile.intValue()) {
              case 1:
                drawWall(g, loc.getKey().intValue(), loc.getValue().intValue());
                break;
              case 2:
                drawBlock(g, loc.getKey().intValue(), loc.getValue().intValue());
                break;
              case 3:
                drawPaddle(g, loc.getKey().intValue(), loc.getValue().intValue());
                break;
              case 4:
                drawBall(g, loc.getKey().intValue(), loc.getValue().intValue());
                break;
            }
          });
      drawScore(g);
    }

    void drawWall(Graphics g, int x, int y) {
      g.setColor(Color.GRAY);
      g.fillRect(TILE_SIZE * x, TILE_SIZE * y, TILE_SIZE, TILE_SIZE);
    }

    void drawBlock(Graphics g, int x, int y) {
      g.setColor(getBlockColor(y));
      g.fillRect(TILE_SIZE * x, TILE_SIZE * y, TILE_SIZE, TILE_SIZE);
    }

    void drawPaddle(Graphics g, int x, int y) {
      g.setColor(new Color(200, 72, 72));
      g.fillRect(TILE_SIZE * x, TILE_SIZE * y, TILE_SIZE, TILE_SIZE);
    }

    void drawBall(Graphics g, int x, int y) {
      g.setColor(new Color(200, 72, 72));
      g.fillOval(TILE_SIZE * x, TILE_SIZE * y, TILE_SIZE, TILE_SIZE);
    }

    void drawScore(Graphics g) {
      g.setColor(Color.WHITE);
      g.setFont(new Font("Impact", Font.PLAIN, 18));
      g.drawString("Score: " + score, TILE_SIZE, (TILE_SIZE * (HEIGHT)));
    }

    Color getBlockColor(int y) {
      var idx = ((((100 * (y - 2)) / 11) * (11 - BLOCK_COLORS.length)) / 100) % BLOCK_COLORS.length;
      return BLOCK_COLORS[idx];
    }
  }

  public static void main(String... args) {
    try {
      var code =
          Files.readAllLines(
                  Path.of(Day13.class.getResource("/Day13_input1.txt").toURI()),
                  StandardCharsets.UTF_8)
              .stream()
              .flatMap(s -> Stream.of(s.split(",")))
              .map(Long::parseLong)
              .collect(Collectors.toList());

      runGame(code);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return;
    }
  }
}

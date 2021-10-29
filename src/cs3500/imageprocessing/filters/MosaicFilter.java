package cs3500.imageprocessing.filters;

import cs3500.imageprocessing.model.Channel;
import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.Pixel;
import cs3500.imageprocessing.model.PixelImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Represents a filter to make an image a mosaic. Clusters the pixels in an image to their nearest
 * seeds, making all pixels the average color of their cluster.
 */
public class MosaicFilter implements ImageFilter {

  private final int numSeeds;

  /**
   * Constructs a new {@code MosaicFilter} object with the given number of seeds.
   * @param numSeeds the number of seeds to use for the mosaic
   * @throws IllegalArgumentException if the number of seeds is not positive
   */
  public MosaicFilter(int numSeeds) throws IllegalArgumentException {
    if (numSeeds <= 0) {
      throw new IllegalArgumentException("The number of seeds must be positive.");
    }
    this.numSeeds = numSeeds;
  }

  @Override
  public void apply(ImageModel model) {
    if (model == null || numSeeds > model.getWidth() * model.getHeight()) {
      throw new IllegalArgumentException("Invalid.");
    }

    int width = model.getWidth();
    int height = model.getHeight();
    Random rand = new Random();
    Map<Posn, List<Pixel>> seeds = new HashMap<>();
    for (int i = 0; i < numSeeds; i++) {
      int randX = rand.nextInt(width);
      int randY = rand.nextInt(height);
      Posn seed = new Posn(randX, randY);
      if (!seeds.containsKey(seed)) {
        seeds.put(seed, new ArrayList<>());
      } else {
        i--;
      }
    }

    Map<Posn, Posn> pixelsToSeeds = new HashMap<>();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Set<Entry<Posn, List<Pixel>>> entries = seeds.entrySet();
        Posn pixel = new Posn(i, j);
        Posn minDistSeed = null;
        double min = -1;
        for (Entry<Posn, List<Pixel>> entry : entries) {
          double cur = entry.getKey().distanceBetween(pixel);
          if (min == -1) {
            min = cur;
            minDistSeed = entry.getKey();
          }
          if (cur < min) {
            min = cur;
            minDistSeed = entry.getKey();
          }
        }
        seeds.get(minDistSeed).add(model.getPixelAt(j, i));
        pixelsToSeeds.put(new Posn(j, i), minDistSeed);
      }
    }

    Map<Posn, Pixel> seedToAvgPixel = new HashMap<>();
    Set<Entry<Posn, List<Pixel>>> entries = seeds.entrySet();
    for (Entry<Posn, List<Pixel>> entry : entries) {
      int sumRed = 0;
      int sumGreen = 0;
      int sumBlue = 0;

      List<Pixel> pixels = entry.getValue();
      for (Pixel p : pixels) {
        sumRed += p.getChannelValue(Channel.RED);
        sumGreen += p.getChannelValue(Channel.GREEN);
        sumBlue += p.getChannelValue(Channel.BLUE);
      }
      int numPixels = pixels.size();
      sumRed = Math.min(255, sumRed / numPixels);
      sumGreen = Math.min(255, sumGreen / numPixels);
      sumBlue = Math.min(255, sumBlue / numPixels);

      seedToAvgPixel.put(entry.getKey(), new PixelImpl(sumRed, sumGreen, sumBlue));
    }

    List<List<Pixel>> pixels = new ArrayList<>();
    for (int i = 0; i < height; i++) {
      pixels.add(new ArrayList<>());
      for (int j = 0; j < width; j++) {
        Pixel p = seedToAvgPixel.get(pixelsToSeeds.get(new Posn(i, j)));
        pixels.get(i).add(new PixelImpl(p));
      }
    }

    model.storeImage(pixels);
  }

  /**
   * Class to represent a coordinate position.
   */
  private static class Posn {
    public int x;
    public int y;

    /**
     * Constructs a new {@code Posn} object.
     *
     * @param x the x value of the posn
     * @param y the y value of the posn
     */
    public Posn(int x, int y) {
      this.x = x;
      this.y = y;
    }

    /**
     * Produces the distance between two Posn.
     *
     * @param that the other posn
     * @return the distance between this position and the given position
     * @throws IllegalArgumentException if the given posn is null
     */
    public double distanceBetween(Posn that) throws IllegalArgumentException {
      if (that == null) {
        throw new IllegalArgumentException("Posn cannot be null.");
      }

      double distance;
      distance = Math.abs(Math.sqrt(Math.pow(that.x - x, 2) + Math.pow(that.y - y, 2)));

      return distance;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Posn)) {
        return false;
      }
      Posn other = (Posn) obj;
      return other.x == x && other.y == y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }
  }
}

package cs3500.imageprocessing.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the creation of a checkerboard image. Can be used to produce a configurable image of a
 * checkerboard which is returned as a list of lists of pixels.
 */
public class CheckerboardImageCreator implements ImageCreator {

  private final int tileSize;
  private final int numTiles;
  private final Color color1;
  private final Color color2;

  /**
   * Constructs a new {@code ChckerboardImageCreator} object.
   *
   * @param tileSize the size of each tile in the checkerboard in pixels
   * @param numTiles the length/width of the square checkerboard in tiles
   * @param color1   the color of some of the tiles
   * @param color2   the color of the other tiles
   * @throws IllegalArgumentException if the tileSize or numTiles is less than 0, or if either color
   *                                  is null
   */
  public CheckerboardImageCreator(int tileSize, int numTiles, Color color1, Color color2)
      throws IllegalArgumentException {
    if (color1 == null || color2 == null) {
      throw new IllegalArgumentException("Colors must not be null.");
    }
    if (tileSize < 0 || numTiles < 0) {
      throw new IllegalArgumentException("Must have non-negative tileSize and numTiles.");
    }
    this.tileSize = tileSize;
    this.numTiles = numTiles;
    this.color1 = color1;
    this.color2 = color2;
  }

  @Override
  public List<List<Pixel>> create() {
    List<List<Pixel>> result = new ArrayList<>();

    Color c;

    for (int i = 0; i < numTiles * tileSize; i++) {
      result.add(new ArrayList<>());
      for (int j = 0; j < numTiles * tileSize; j++) {
        int tileLocation = (i / tileSize) + (j / tileSize);

        if (tileLocation % 2 == 0) {
          c = color1;
        } else {
          c = color2;
        }

        result.get(i).add(new PixelImpl(c));
      }
    }

    return result;
  }
}

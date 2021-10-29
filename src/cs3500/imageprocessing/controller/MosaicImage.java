package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.filters.MosaicFilter;
import cs3500.imageprocessing.model.LayeredImageModel;

/**
 * Represents a command to apply the mosaic filter to a given {@link LayeredImageModel}.
 */
public class MosaicImage implements ImageProcessingCommand {

  private final int numSeeds;

  /**
   * Constructs a new {@code MosaicImage} object with the given number of seeds.
   *
   * @param numSeeds the number of seeds to be used for this mosaic filter
   */
  public MosaicImage(int numSeeds) {
    this.numSeeds = numSeeds;
  }

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    if (m == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    m.applyFilter(new MosaicFilter(numSeeds));
  }
}

package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.filters.GreyscaleFilter;
import cs3500.imageprocessing.model.LayeredImageModel;

/**
 * Represents a command that applies a grayscale filter to the image of a given {@link
 * LayeredImageModel}.
 */
public class GrayscaleImage implements ImageProcessingCommand {

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    m.applyFilter(new GreyscaleFilter());
  }
}

package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.filters.SepiaFilter;
import cs3500.imageprocessing.model.LayeredImageModel;

/**
 * Represents a command that applies the sepia filter to the image of a given {@link
 * LayeredImageModel}.
 */
public class SepiaImage implements ImageProcessingCommand {

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    m.applyFilter(new SepiaFilter());
  }
}

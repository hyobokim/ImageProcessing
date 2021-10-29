package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.filters.BlurFilter;
import cs3500.imageprocessing.model.LayeredImageModel;

/**
 * Represents a command that blurs the image of a given {@link LayeredImageModel}.
 */
public class BlurImage implements ImageProcessingCommand {

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    m.applyFilter(new BlurFilter());
  }
}

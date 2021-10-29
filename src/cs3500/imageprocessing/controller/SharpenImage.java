package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.filters.SharpenFilter;
import cs3500.imageprocessing.model.LayeredImageModel;

/**
 * Represents a command that sharpens the image of a given {@link LayeredImageModel}.
 */
public class SharpenImage implements ImageProcessingCommand {

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    m.applyFilter(new SharpenFilter());
  }
}

package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.filters.ResizeFilter;
import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.LayeredImageModel;

/**
 * Represents a command to downscale the images in a given {@link LayeredImageModel} to a given
 * height and width.
 */
public class ResizeLayer implements ImageProcessingCommand {

  private final int width;
  private final int height;

  /**
   * Constructs a new {@code ResizeLayer} object with the given height and width values.
   *
   * @param width the width of the downscaled image
   * @param height the height of the downscaled image
   */
  public ResizeLayer(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {

    if (width > m.getWidth() || height > m.getHeight()) {
      throw new IllegalArgumentException(
          "New width and height must be less than original dimensions.");
    }

    for (ImageModel layer : m) {
      layer.applyFilter(new ResizeFilter(width, height));
    }
  }
}

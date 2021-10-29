package cs3500.imageprocessing.filters;

import cs3500.imageprocessing.model.ImageModel;

/**
 * Represents an image filter that can be applied to a given {@code ImageModel}.
 */
public interface ImageFilter {

  /**
   * Applies a filter to an image.
   *
   * @param model the image to apply the filter to
   */
  public void apply(ImageModel model);
}

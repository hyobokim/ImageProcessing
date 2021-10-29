package cs3500.imageprocessing.model;

import java.util.List;

/**
 * Represents an operation to produce an image as a list of lists
 * of pixels. Can be used to create various images programmatically.
 */
public interface ImageCreator {

  /**
   * Produces a new image programmatically.
   *
   * @return a list of lists of pixels representing the new image
   */
  public List<List<Pixel>> create();
}

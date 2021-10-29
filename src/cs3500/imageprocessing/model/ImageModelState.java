package cs3500.imageprocessing.model;

/**
 * Represents the operations necessary to query the state of an image model.
 */
public interface ImageModelState {

  /**
   * Produces the pixel at given coordinates in the image.
   *
   * @param x the column of the pixel
   * @param y the row of the pixel
   * @return the pixel at the given coordinates
   * @throws IllegalArgumentException if there is no pixel at the given position
   */
  public Pixel getPixelAt(int x, int y) throws IllegalArgumentException;

  /**
   * Produces the height of the image, or 0 if there is no image.
   *
   * @return the height of the image
   */
  public int getHeight();

  /**
   * Produces the width of the image, or 0 if there is no image.
   *
   * @return the width of the image
   */
  public int getWidth();
}

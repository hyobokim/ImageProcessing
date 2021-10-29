package cs3500.imageprocessing.model;

import cs3500.imageprocessing.filters.ImageFilter;
import java.util.List;

/**
 * Represents an image providing various operations to manipulate this image. The image can be
 * produced programmatically, or imported from a file. The image can also be exported to a file.
 */
public interface ImageModel extends ImageModelState {

  /**
   * Applies the given kernel to each of the image's pixels, for the given channel. The kernel must
   * be a NxN grid where N is odd.
   *
   * @param kernel  the kernel to be applied
   * @param channel the channel to apply the kernel to
   * @throws IllegalArgumentException if the kernel is null or not of size NxN where N is odd, or
   *                                  the channel is null
   */
  public void applyKernel(List<List<Double>> kernel, Channel channel)
      throws IllegalArgumentException;

  /**
   * Applies a linear transformation to each of the images pixels based on a given matrix. The given
   * matrix must be a grid of size 3x3.
   *
   * @param matrix the matrix for the linear transformation
   * @throws IllegalArgumentException if the given matrix is null or not of size 3x3
   */
  public void applyLinearTransformation(List<List<Double>> matrix) throws IllegalArgumentException;

  /**
   * Creates and stores an image programmatically based on a given object that
   * specifies how the image should be created.
   *
   * @param creator the object that is used to create the image
   * @throws IllegalArgumentException if the given creator is null
   */
  public void createImage(ImageCreator creator) throws IllegalArgumentException;

  /**
   * Applies a filter to the image, storing the updated image, based on a given
   * object that specifies and applies the filter.
   *
   * @param filter the filter to apply
   * @throws IllegalArgumentException if the given filter is null
   */
  public void applyFilter(ImageFilter filter) throws IllegalArgumentException;

  /**
   * Stores a given list of lists of pixels as the image for the model.
   *
   * @param image the image to be stored as a list of lists of pixels
   * @throws IllegalArgumentException if the given image is not a rectangle or is null
   */
  public void storeImage(List<List<Pixel>> image) throws IllegalArgumentException;
}

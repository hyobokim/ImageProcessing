package cs3500.imageprocessing.model;

import java.util.List;

/**
 * Represents a singular pixel in an image with RGB values. Provides operations
 * for getting and setting channel values as well as applying matrix-based linear
 * transformations.
 */
public interface Pixel {

  /**
   * Applies a linear transformation to a pixel based on the given matrix.
   *
   * @param matrix the values for the transformation
   * @throws IllegalArgumentException if the given matrix is not 3 by 3 or is null
   */
  public void applyLinearTransformation(List<List<Double>> matrix) throws IllegalArgumentException;

  /**
   * Produces the value of the specified channel for this pixel.
   *
   * @param channel the channel of this pixel whose value to obtain
   * @return the value of the specified channel for this pixel
   * @throws IllegalArgumentException if the given channel is null
   */
  public int getChannelValue(Channel channel) throws IllegalArgumentException;

  /**
   * Sets the value of the given channel in this pixel to the given value.
   *
   * @param channel the channel to be set
   * @param val     the value to set the channel to
   * @throws IllegalArgumentException if the given channel is null or if the channel value is
   *                                  invalid, outside of [0, 255]
   */
  public void setChannel(Channel channel, int val) throws IllegalArgumentException;

  /**
   * Produces a string representing this pixel. Each channel value is present in the order
   * red, green, blue, each separated by newlines.
   *
   * @return a string of each of this pixel's channel values separated by newlines
   */
  public String toString();
}

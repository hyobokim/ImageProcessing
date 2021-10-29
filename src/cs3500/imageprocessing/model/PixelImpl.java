package cs3500.imageprocessing.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a singular pixel in an image with RGB values.
 */
public class PixelImpl implements Pixel {

  private int red;
  private int green;
  private int blue;

  /**
   * Constructs a new {@code Pixel} object.
   *
   * @param red   the red value of this pixel [0-255]
   * @param green the green value of this pixel [0-255]
   * @param blue  the blue value of this pixel [0-255]
   * @throws IllegalArgumentException if any color value is not between 0 and 255 inclusive
   */
  public PixelImpl(int red, int green, int blue) throws IllegalArgumentException {
    if (!checkValidValues(red, green, blue)) {
      throw new IllegalArgumentException("Invalid color value.");
    }
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  /**
   * Copy constructor that constructs a new {@code cs3500.imageprocessing.Pixel} object with the
   * same RGB values as the given pixel.
   *
   * @param that pixel to be copied
   */
  public PixelImpl(Pixel that) throws IllegalArgumentException {
    if (that == null) {
      throw new IllegalArgumentException("Pixel cannot be null.");
    }

    this.red = that.getChannelValue(Channel.RED);
    this.green = that.getChannelValue(Channel.GREEN);
    this.blue = that.getChannelValue(Channel.BLUE);
  }

  /**
   * Constructs a new {@Code Pixel} using a Java Color class as reference for its RGB values.
   *
   * @param c the Java color to make this pixel
   */
  public PixelImpl(Color c) {
    if (c == null) {
      throw new IllegalArgumentException("Color can't be null");
    }
    if (!checkValidValues(c.getRed(), c.getGreen(), c.getBlue())) {
      throw new IllegalArgumentException("Invalid color value");
    }

    this.red = c.getRed();
    this.green = c.getGreen();
    this.blue = c.getBlue();
  }

  @Override
  public void applyLinearTransformation(List<List<Double>> matrix) throws IllegalArgumentException {
    if (matrix == null || matrix.size() != 3) {
      throw new IllegalArgumentException("Invalid matrix.");
    }

    List<Integer> newValues = new ArrayList<>(3);
    for (List<Double> row : matrix) {
      if (row.size() != 3) {
        throw new IllegalArgumentException("Invalid matrix.");
      }
      newValues.add((int) (row.get(0) * red + row.get(1) * green + row.get(2) * blue));
    }

    red = Math.max(0, Math.min(255, newValues.get(0)));
    green = Math.max(0, Math.min(255, newValues.get(1)));
    blue = Math.max(0, Math.min(255, newValues.get(2)));
  }

  @Override
  public int getChannelValue(Channel channel) throws IllegalArgumentException {
    if (channel == null) {
      throw new IllegalArgumentException("Channel cannot be null.");
    }
    switch (channel) {
      case RED:
        return red;
      case GREEN:
        return green;
      case BLUE:
        return blue;
      default:
        throw new IllegalArgumentException("Invalid channel.");
    }
  }

  @Override
  public void setChannel(Channel channel, int val) throws IllegalArgumentException {
    if (channel == null) {
      throw new IllegalArgumentException("Channel cannot be null.");
    }
    if (!checkValidValues(val)) {
      throw new IllegalArgumentException("Invalid channel value.");
    }
    switch (channel) {
      case RED:
        red = val;
        break;
      case GREEN:
        green = val;
        break;
      case BLUE:
        blue = val;
        break;
      default:
        throw new IllegalArgumentException("Invalid channel.");
    }
  }

  /**
   * Determines whether the given values are valid channel value within the range [0, 255].
   *
   * @param args the values to be checked
   * @return true if the given values are all valid, false if any are not
   */
  private boolean checkValidValues(int... args) {
    for (int i : args) {
      if (i < 0 || i > 255) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return red + "\n" + green + "\n" + blue;
  }

  /**
   * Determines whether this pixel and the given object are equal. An object is equal to this pixel
   * if it is an instance of {@code PixelImpl} and has the same color values for all channels as
   * this pixel.
   *
   * @param obj the object to check for equality
   * @return true if this pixel and the given object are equal, false if not
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof PixelImpl)) {
      return false;
    }
    PixelImpl other = (PixelImpl) obj;
    return this.red == other.red && this.green == other.green && this.blue == other.blue;
  }

  @Override
  public int hashCode() {
    return red * 1000000 + green * 1000 + blue;
  }
}

package cs3500.imageprocessing.filters;

import cs3500.imageprocessing.model.Channel;
import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.Pixel;
import cs3500.imageprocessing.model.PixelImpl;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a filter that resizes an image. Resizes an existing image to given dimensions using a
 * mathematical formula.
 */
public class ResizeFilter implements ImageFilter {

  private final int width;
  private final int height;

  /**
   * Constructs a new {@code ResizeFilter} object with the given width and height.
   *
   * @param width  the new width of the resized image
   * @param height the new height of the resized image
   */
  public ResizeFilter(int width, int height) throws IllegalArgumentException {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Width and height must be positive.");
    }

    this.width = width;
    this.height = height;
  }

  @Override
  public void apply(ImageModel model) {

    List<List<Pixel>> newImage = new ArrayList<>();
    for (int i = 0; i < height; i++) {
      newImage.add(new ArrayList<>());
      for (int j = 0; j < width; j++) {
        float row = model.getHeight() * ((float) i / height);
        float col = model.getWidth() * ((float) j / width);

        // if x and y are integer values and correspond directly to a pixel
        if (col - (int) col == 0 || row - (int) row == 0) {
          newImage.get(i).add(model.getPixelAt((int) row, (int) col));
          continue;
        }

        Pixel p1 = model.getPixelAt((int) Math.floor(row), (int) Math.floor(col));
        Pixel p2 = model.getPixelAt((int) Math.floor(row), (int) Math.ceil(col));
        Pixel p3 = model.getPixelAt((int) Math.ceil(row), (int) Math.floor(col));
        Pixel p4 = model.getPixelAt((int) Math.ceil(row), (int) Math.ceil(col));

        double mRed = p2.getChannelValue(Channel.RED) * (col - Math.floor(col))
            + p1.getChannelValue(Channel.RED) * (Math.ceil(col) - col);
        double nRed = p4.getChannelValue(Channel.RED) * (col - Math.floor(col))
            + p3.getChannelValue(Channel.RED) * (Math.ceil(col) - col);
        int red = (int) (nRed * (row - Math.floor(row)) + mRed * (Math.ceil(row) - row));

        double mGreen = p2.getChannelValue(Channel.GREEN) * (col - Math.floor(col))
            + p1.getChannelValue(Channel.GREEN) * (Math.ceil(col) - col);
        double nGreen = p4.getChannelValue(Channel.GREEN) * (col - Math.floor(col))
            + p3.getChannelValue(Channel.GREEN) * (Math.ceil(col) - col);
        int green = (int) (nGreen * (row - Math.floor(row)) + mGreen * (Math.ceil(row) - row));

        double mBlue = p2.getChannelValue(Channel.BLUE) * (col - Math.floor(col))
            + p1.getChannelValue(Channel.BLUE) * (Math.ceil(col) - col);
        double nBlue = p4.getChannelValue(Channel.BLUE) * (col - Math.floor(col))
            + p3.getChannelValue(Channel.BLUE) * (Math.ceil(col) - col);
        int blue = (int) (nBlue * (row - Math.floor(row)) + mBlue * (Math.ceil(row) - row));

        newImage.get(i).add(new PixelImpl(red, green, blue));
      }
    }

    model.storeImage(newImage);
  }
}

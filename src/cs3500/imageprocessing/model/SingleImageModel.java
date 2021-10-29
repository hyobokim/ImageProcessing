package cs3500.imageprocessing.model;

import cs3500.imageprocessing.filters.ImageFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a model for an image processing application that holds a single image as a list of
 * lists of pixels. Provides all operations specified in the {@link ImageModel} interface to load,
 * modify and query the state of images.
 */
public class SingleImageModel implements ImageModel {

  /*
   * Class invariant: image will always be a rectangular list of lists.
   * The size of all the sublists will always be the same.
   */
  private List<List<Pixel>> image;

  /**
   * Copy constructor that constructs a new instance of {@code SingleImageModel}
   * with the same data as the given {@code ImageModel}.
   *
   * @param that the ImageModel to be copied
   */
  public SingleImageModel(ImageModel that) {
    this();
    for (int i = 0; i < that.getHeight(); i++) {
      image.add(new ArrayList<>());
      for (int j = 0; j < that.getWidth(); j++) {
        image.get(i).add(that.getPixelAt(i, j));
      }
    }
  }

  /**
   * Constructs a new {@code SingleImageModel} containing a blank image to start.
   */
  public SingleImageModel() {
    image = new ArrayList<>();
  }

  @Override
  public void applyKernel(List<List<Double>> kernel, Channel channel)
      throws IllegalArgumentException {
    if (kernel == null || channel == null) {
      throw new IllegalArgumentException("Kernel and channel cannot be null.");
    }

    List<List<Pixel>> newImage = new ArrayList<>();
    for (int i = 0; i < image.size(); i++) {
      newImage.add(new ArrayList<>());
      for (int j = 0; j < image.get(i).size(); j++) {
        int newChannelVal = applyKernelToPixel(kernel, channel, i, j);
        Pixel newPixel = new PixelImpl(image.get(i).get(j));
        newPixel.setChannel(channel, newChannelVal);
        newImage.get(i).add(newPixel);
      }
    }
    this.image = newImage;
  }

  /**
   * Applies the given kernel to the given channel of the pixel at the given row and column
   * coordinates.
   *
   * @param kernel  the kernel to be applied
   * @param channel the channel that the kernel is being applied to
   * @param row     the row in the image of the pixel
   * @param column  the column in the image of the pixel
   * @return the new value of the pixel, after the kernel has been applied
   * @throws IllegalArgumentException if the kernel is invalid
   */
  private int applyKernelToPixel(
      List<List<Double>> kernel, Channel channel, int row, int column)
      throws IllegalArgumentException {
    ensureValidListOfLists(kernel);
    int kernelSize = kernel.size();
    if (kernelSize % 2 == 0) {
      throw new IllegalArgumentException("Kernel must have odd dimensions.");
    }

    double sum = 0;
    int center = kernel.size() / 2;
    for (int i = 0; i < kernelSize; i++) {
      int kernelRowSize = kernel.get(i).size();
      for (int j = 0; j < kernelRowSize; j++) {
        if (kernelRowSize != kernelSize) {
          throw new IllegalArgumentException("Kernel height and width must be equal.");
        }
        int newRow = row - center + i;
        int newColumn = column - center + j;
        if (newRow < 0 || newRow >= image.size() || newColumn < 0 || newColumn >= image.get(row)
            .size()) {
          continue;
        }
        Pixel pixel = image.get(newRow).get(newColumn);
        sum += pixel.getChannelValue(channel) * kernel.get(i).get(j);
      }
    }
    return Math.max(0, Math.min(255, (int) sum));
  }

  @Override
  public void applyLinearTransformation(List<List<Double>> matrix) throws IllegalArgumentException {
    ensureValidListOfLists(matrix);
    for (List<Pixel> row : image) {
      for (Pixel pixel : row) {
        pixel.applyLinearTransformation(matrix);
      }
    }
  }

  @Override
  public void createImage(ImageCreator creator) throws IllegalArgumentException {
    if (creator == null) {
      throw new IllegalArgumentException("Creator cannot be null.");
    }
    storeImage(creator.create());
  }

  @Override
  public void applyFilter(ImageFilter filter) throws IllegalArgumentException {
    if (filter == null) {
      throw new IllegalArgumentException("Filter cannot be null.");
    }
    filter.apply(this);
  }

  @Override
  public void storeImage(List<List<Pixel>> image) throws IllegalArgumentException {
    ensureValidListOfLists(image);
    List<List<Pixel>> newImage = new ArrayList<>();
    for (List<Pixel> row : image) {
      if (row.size() != image.get(0).size()) {
        throw new IllegalArgumentException("Malformed image.");
      }
      newImage.add(new ArrayList<Pixel>(row));
    }
    this.image = newImage;
  }

  /**
   * Ensures that a given list of lists is valid. Validity is defined as being non-null, containing
   * no null elements, and no lists within the outer list contain null elements.
   *
   * @throws IllegalArgumentException if non-valid
   */
  private <K> void ensureValidListOfLists(List<List<K>> list) throws IllegalArgumentException {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null.");
    }
    for (List<K> l : list) {
      if (l == null) {
        throw new IllegalArgumentException("List cannot contain null elements.");
      }
      for (K e : l) {
        if (e == null) {
          throw new IllegalArgumentException("List cannot contain list with null elements.");
        }
      }
    }
  }

  @Override
  public Pixel getPixelAt(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= getHeight() || col < 0 || col >= getWidth()) {
      throw new IllegalArgumentException("No pixel here.");
    }
    return new PixelImpl(image.get(row).get(col));
  }

  @Override
  public int getHeight() {
    return image.size();
  }

  @Override
  public int getWidth() {
    if (image.size() == 0) {
      return 0;
    }
    return image.get(0).size();
  }
}

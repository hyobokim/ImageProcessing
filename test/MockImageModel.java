import cs3500.imageprocessing.model.Channel;
import cs3500.imageprocessing.model.ImageCreator;
import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.Pixel;
import cs3500.imageprocessing.filters.ImageFilter;
import cs3500.imageprocessing.model.PixelImpl;
import java.awt.Color;
import java.util.List;

/**
 * Mock class to log the arguments passed to an ImageModel's applyKernel
 * and applyLinearTransformation methods.
 */
public class MockImageModel implements ImageModel {

  protected StringBuilder log;

  /**
   * Constructs a new {@code MockImageModel} object.
   *
   * @param log the log for arguments
   */
  public MockImageModel(StringBuilder log) {
    this.log = log;
  }

  /**
   * Logs the arguments passed to applyKernel.
   *
   * @param kernel  the kernel to be logged
   * @param channel the channel to be logged
   */
  @Override
  public void applyKernel(List<List<Double>> kernel, Channel channel) {
    log.append("Kernel: ");
    for (List<Double> l : kernel) {
      for (Double d : l) {
        log.append(d).append(" ");
      }
    }
    log.append("Channel: ").append(channel).append(" ");
  }

  /**
   * Logs the arguments passed to applyLinearTransformation.
   *
   * @param matrix the matrix to be logged
   */
  @Override
  public void applyLinearTransformation(List<List<Double>> matrix) {
    log.append("Matrix: ");
    for (List<Double> l : matrix) {
      for (Double d : l) {
        log.append(d).append(" ");
      }
    }
  }

  @Override
  public void createImage(ImageCreator creator) throws IllegalArgumentException {
    log.append("Creator: ").append(creator.getClass());
  }

  @Override
  public void applyFilter(ImageFilter filter) throws IllegalArgumentException {
    log.append("Filter: ").append(filter.getClass());
  }

  @Override
  public void storeImage(List<List<Pixel>> image) throws IllegalArgumentException {
    log.append("storeImage");
  }

  @Override
  public Pixel getPixelAt(int x, int y) throws IllegalArgumentException {
    // returns a dummy pixel for this mock
    return new PixelImpl(Color.RED);
  }

  @Override
  public int getHeight() {
    // returns a dummy value for this mock
    return 10;
  }

  @Override
  public int getWidth() {
    // returns a dummy value for this mock
    return 10;
  }
}

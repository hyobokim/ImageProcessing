package cs3500.imageprocessing.filters;

import cs3500.imageprocessing.model.ImageModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a filter that turns an image to greyscale. Contains a specific matrix
 * that can be applied as a linear transformation to achieve this effect.
 */
public class GreyscaleFilter implements ImageFilter {
  private static final List<List<Double>> matrix = new ArrayList<>(
      Arrays.asList(Arrays.asList(0.2126, 0.7152, 0.0722),
          Arrays.asList(0.2126, 0.7152, 0.0722),
          Arrays.asList(0.2126, 0.7152, 0.0722)));

  @Override
  public void apply(ImageModel model) {
    model.applyLinearTransformation(matrix);
  }
}

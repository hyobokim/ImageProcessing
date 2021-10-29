package cs3500.imageprocessing.filters;

import cs3500.imageprocessing.model.ImageModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a filter that turns an image to sepia shades. Contains a specific matrix
 * that can be applied as a linear transformation to achieve this effect.
 */
public class SepiaFilter implements ImageFilter {
  private static final List<List<Double>> matrix = new ArrayList<>(
      Arrays.asList(Arrays.asList(0.393, 0.769, 0.189),
          Arrays.asList(0.349, 0.686, 0.168),
          Arrays.asList(0.272, 0.534, 0.131)));

  @Override
  public void apply(ImageModel model) {
    model.applyLinearTransformation(matrix);
  }
}

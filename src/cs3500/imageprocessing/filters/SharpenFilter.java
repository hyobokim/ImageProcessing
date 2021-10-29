package cs3500.imageprocessing.filters;

import cs3500.imageprocessing.model.Channel;
import cs3500.imageprocessing.model.ImageModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a filter that sharpens an image, increasing contrast. Contains a specific
 * kernel that can be applied to each channel of each pixel to achieve this effect.
 */
public class SharpenFilter implements ImageFilter {
  private static final List<List<Double>> kernel = new ArrayList<>(
      Arrays.asList(Arrays.asList(-0.125, -0.125, -0.125, -0.125, -0.125),
          Arrays.asList(-0.125, 0.25, 0.25, 0.25, -0.125),
          Arrays.asList(-0.125, 0.25, 1.0, 0.25, -0.125),
          Arrays.asList(-0.125, 0.25, 0.25, 0.25, -0.125),
          Arrays.asList(-0.125, -0.125, -0.125, -0.125, -0.125)));

  @Override
  public void apply(ImageModel model) {
    model.applyKernel(kernel, Channel.RED);
    model.applyKernel(kernel, Channel.GREEN);
    model.applyKernel(kernel, Channel.BLUE);
  }
}

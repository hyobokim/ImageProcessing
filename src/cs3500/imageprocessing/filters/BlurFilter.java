package cs3500.imageprocessing.filters;

import cs3500.imageprocessing.model.Channel;
import cs3500.imageprocessing.model.ImageModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a filter that blurs an image. Contains a specific kernel
 * that can be applied to each channel of each pixel to achieve a simple blur effect.
 */
public class BlurFilter implements ImageFilter {
  private static final List<List<Double>> kernel = new ArrayList<>(
      Arrays.asList(Arrays.asList(0.0625, 0.125, 0.0625),
          Arrays.asList(0.125, 0.25, 0.125),
          Arrays.asList(0.0625, 0.125, 0.0625)));

  @Override
  public void apply(ImageModel model) {
    model.applyKernel(kernel, Channel.RED);
    model.applyKernel(kernel, Channel.GREEN);
    model.applyKernel(kernel, Channel.BLUE);
  }
}

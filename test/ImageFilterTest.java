import static org.junit.Assert.assertEquals;

import cs3500.imageprocessing.filters.MosaicFilter;
import cs3500.imageprocessing.filters.ResizeFilter;
import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.filters.BlurFilter;
import cs3500.imageprocessing.filters.GreyscaleFilter;
import cs3500.imageprocessing.filters.SepiaFilter;
import cs3500.imageprocessing.filters.SharpenFilter;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the public facing functionality of the classes that implement the {@code
 * ImageFilter} interface.
 */
public class ImageFilterTest {

  private ImageModel mockModel;
  private StringBuilder log;
  private static final String blurCall = "Kernel: 0.0625 0.125 0.0625 0.125 0.25 0.125 "
      + "0.0625 0.125 0.0625 Channel: RED Kernel: 0.0625 0.125 0.0625 0.125 0.25 "
      + "0.125 0.0625 0.125 0.0625 Channel: GREEN Kernel: 0.0625 0.125 0.0625 0.125"
      + " 0.25 0.125 0.0625 0.125 0.0625 Channel: BLUE ";
  private static final String greyscaleCall = "Matrix: 0.2126 0.7152 0.0722"
      + " 0.2126 0.7152 0.0722 0.2126 0.7152 0.0722 ";
  private static final String sepiaCall = "Matrix: 0.393 0.769 0.189 0.349"
      + " 0.686 0.168 0.272 0.534 0.131 ";
  private static final String sharpenCall = "Kernel: -0.125 -0.125 -0.125 -0.125 -0.125 "
      + "-0.125 0.25 0.25 0.25 -0.125 -0.125 0.25 1.0 0.25 -0.125 -0.125 0.25 0.25"
      + " 0.25 -0.125 -0.125 -0.125 -0.125 -0.125 -0.125 Channel: RED Kernel: -0.125"
      + " -0.125 -0.125 -0.125 -0.125 -0.125 0.25 0.25 0.25 -0.125 -0.125 0.25 1.0 "
      + "0.25 -0.125 -0.125 0.25 0.25 0.25 -0.125 -0.125 -0.125 -0.125 -0.125 -0.125 "
      + "Channel: GREEN Kernel: -0.125 -0.125 -0.125 -0.125 -0.125 -0.125 0.25 0.25 "
      + "0.25 -0.125 -0.125 0.25 1.0 0.25 -0.125 -0.125 0.25 0.25 0.25 -0.125 -0.125 "
      + "-0.125 -0.125 -0.125 -0.125 Channel: BLUE ";

  @Before
  public void setup() {
    log = new StringBuilder();
    mockModel = new MockImageModel(log);
  }

  @Test
  public void testBlurFilterApply() {
    new BlurFilter().apply(mockModel);
    assertEquals(blurCall, log.toString());
  }

  @Test
  public void testSepiaFilterApply() {
    new SepiaFilter().apply(mockModel);
    assertEquals(sepiaCall, log.toString());
  }

  @Test
  public void testSharpenFilterApply() {
    new SharpenFilter().apply(mockModel);
    assertEquals(sharpenCall, log.toString());
  }

  @Test
  public void testGreyscaleFilterApply() {
    new GreyscaleFilter().apply(mockModel);
    assertEquals(greyscaleCall, log.toString());
  }

  @Test
  public void testMosaicFilterApply() {
    new MosaicFilter(10).apply(mockModel);
    assertEquals("storeImage", log.toString());
  }

  @Test
  public void testResizeFilterApply() {
    new ResizeFilter(10, 10).apply(mockModel);
    assertEquals("storeImage", log.toString());
  }
}

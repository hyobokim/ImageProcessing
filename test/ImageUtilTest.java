import static org.junit.Assert.assertEquals;

import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.ImageUtil;
import cs3500.imageprocessing.model.Pixel;
import cs3500.imageprocessing.model.PixelImpl;
import cs3500.imageprocessing.model.SingleImageModel;
import java.awt.Color;
import java.util.List;
import org.junit.Test;

/**
 * Class to test the public facing functionality of {@link ImageUtil}.
 */
public class ImageUtilTest {

  @Test
  public void testReadPPM() {
    List<List<Pixel>> lp = ImageUtil.readPPM("res/TestImage.ppm");
    assertEquals(2, lp.size());
    assertEquals(2, lp.get(0).size());
    assertEquals(2, lp.get(1).size());
    assertEquals(new PixelImpl(Color.WHITE), lp.get(0).get(0));
    assertEquals(new PixelImpl(Color.BLACK), lp.get(0).get(1));
    assertEquals(new PixelImpl(Color.BLACK), lp.get(1).get(0));
    assertEquals(new PixelImpl(Color.WHITE), lp.get(1).get(1));
  }

  @Test
  public void testWritePPM() {
    ImageModel model = new SingleImageModel();
    model.storeImage(ImageUtil.readPPM("res/TestImage.ppm"));
    ImageUtil.writePPM(model, "res/WrittenPPM.ppm");
    List<List<Pixel>> lp = ImageUtil.readPPM("res/WrittenPPM.ppm");
    assertEquals(2, lp.size());
    assertEquals(2, lp.get(0).size());
    assertEquals(2, lp.get(1).size());
    assertEquals(new PixelImpl(Color.WHITE), lp.get(0).get(0));
    assertEquals(new PixelImpl(Color.BLACK), lp.get(0).get(1));
    assertEquals(new PixelImpl(Color.BLACK), lp.get(1).get(0));
    assertEquals(new PixelImpl(Color.WHITE), lp.get(1).get(1));
  }
}

import static org.junit.Assert.assertEquals;

import cs3500.imageprocessing.model.CheckerboardImageCreator;
import cs3500.imageprocessing.model.ImageCreator;
import cs3500.imageprocessing.model.Pixel;
import cs3500.imageprocessing.model.PixelImpl;
import java.awt.Color;
import java.util.List;
import org.junit.Test;

/**
 * Class to test the public facing functionality of {@link CheckerboardImageCreator}.
 */
public class CheckerboardImageCreatorTest {

  private final ImageCreator creator = new CheckerboardImageCreator(
      1,
      2,
      new Color(23, 10, 54),
      new Color(20, 40, 60));

  @Test(expected = IllegalArgumentException.class)
  public void testCheckerboardImageCreatorConstructorNullColor1() {
    new CheckerboardImageCreator(1, 1, null, Color.BLACK);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckerboardImageCreatorConstructorNullColor2() {
    new CheckerboardImageCreator(1, 1, Color.GREEN, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckerboardImageCreatorConstructorNegativeTileSize() {
    new CheckerboardImageCreator(-1, 1, Color.GREEN, Color.RED);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckerboardImageCreatorConstructorNegativeNumTiles() {
    new CheckerboardImageCreator(1, -1, Color.GREEN, Color.ORANGE);
  }

  @Test
  public void testCheckerboardImageCreatorValid() {
    List<List<Pixel>> result = creator.create();
    assertEquals(2, result.size());
    assertEquals(2, result.get(0).size());
    assertEquals(2, result.get(1).size());
    assertEquals(new PixelImpl(23, 10, 54), result.get(0).get(0));
    assertEquals(new PixelImpl(20, 40, 60), result.get(0).get(1));
    assertEquals(new PixelImpl(20, 40, 60), result.get(1).get(0));
    assertEquals(new PixelImpl(23, 10, 54), result.get(1).get(1));
  }
}

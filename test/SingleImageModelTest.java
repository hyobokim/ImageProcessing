import static org.junit.Assert.assertEquals;

import cs3500.imageprocessing.model.Channel;
import cs3500.imageprocessing.model.CheckerboardImageCreator;
import cs3500.imageprocessing.model.ImageCreator;
import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.ImageUtil;
import cs3500.imageprocessing.model.Pixel;
import cs3500.imageprocessing.model.PixelImpl;
import cs3500.imageprocessing.filters.BlurFilter;
import cs3500.imageprocessing.filters.GreyscaleFilter;
import cs3500.imageprocessing.filters.SepiaFilter;
import cs3500.imageprocessing.filters.SharpenFilter;
import cs3500.imageprocessing.model.SingleImageModel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the public facing functionality of {@link SingleImageModel}.
 */
public class SingleImageModelTest {

  private ImageModel exModel;
  private SingleImageModel m;
  private ImageModel expected;
  private final List<List<Double>> blurKernel = new ArrayList<>(
      Arrays.asList(Arrays.asList(0.0625, 0.125, 0.0625),
          Arrays.asList(0.125, 0.25, 0.125),
          Arrays.asList(0.0625, 0.125, 0.0625)));
  private final List<List<Double>> simpleKernel = new ArrayList<>(
      Arrays.asList(Arrays.asList(0.5)));
  private final List<List<Double>> nonSquareKernel = new ArrayList<>(
      Arrays.asList(Arrays.asList(0.0625, 0.125, 0.0625),
          Arrays.asList(0.125, 0.25, 0.125)));
  private final List<List<Double>> nonOddKernel = new ArrayList<>(
      Arrays.asList(Arrays.asList(0.0625, 0.125),
          Arrays.asList(0.125, 0.25)));

  @Before
  public void setup() {
    exModel = new SingleImageModel();
    m = new SingleImageModel();
    expected = new SingleImageModel();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyKernelNullKernel() {
    exModel.createImage(
        new CheckerboardImageCreator(4, 4, Color.BLACK, Color.WHITE));
    exModel.applyKernel(null, Channel.RED);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyKernelNullChannel() {
    exModel.createImage(
        new CheckerboardImageCreator(4, 4, Color.BLACK, Color.WHITE));
    exModel.applyKernel(blurKernel, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyKernelNonSquareKernel() {
    exModel.createImage(
        new CheckerboardImageCreator(4, 4, Color.BLACK, Color.WHITE));
    exModel.applyKernel(nonSquareKernel, Channel.BLUE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyKernelNonOddKernel() {
    exModel.createImage(
        new CheckerboardImageCreator(10, 4, Color.BLACK, Color.WHITE));
    exModel.applyKernel(nonOddKernel, Channel.GREEN);
  }

  @Test
  public void testApplyKernelToRed() {
    exModel.createImage(
        new CheckerboardImageCreator(1, 2, Color.RED, Color.GREEN));
    assertEquals(new PixelImpl(255, 0, 0), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(0, 255, 0), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(0, 255, 0), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(255, 0, 0), exModel.getPixelAt(1, 1));
    exModel.applyKernel(simpleKernel, Channel.RED);
    assertEquals(new PixelImpl(127, 0, 0), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(0, 255, 0), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(0, 255, 0), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(127, 0, 0), exModel.getPixelAt(1, 1));
  }

  @Test
  public void testApplyKernelToGreen() {
    exModel.createImage(
        new CheckerboardImageCreator(1, 2, Color.RED, Color.GREEN));
    assertEquals(new PixelImpl(255, 0, 0), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(0, 255, 0), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(0, 255, 0), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(255, 0, 0), exModel.getPixelAt(1, 1));
    exModel.applyKernel(simpleKernel, Channel.GREEN);
    assertEquals(new PixelImpl(255, 0, 0), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(0, 127, 0), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(0, 127, 0), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(255, 0, 0), exModel.getPixelAt(1, 1));
  }

  @Test
  public void testApplyKernelToBlue() {
    exModel.createImage(
        new CheckerboardImageCreator(1, 2, Color.BLUE, Color.RED));
    assertEquals(new PixelImpl(0, 0, 255), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(255, 0, 0), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(255, 0, 0), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(0, 0, 255), exModel.getPixelAt(1, 1));
    exModel.applyKernel(simpleKernel, Channel.BLUE);
    assertEquals(new PixelImpl(0, 0, 127), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(255, 0, 0), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(255, 0, 0), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(0, 0, 127), exModel.getPixelAt(1, 1));
  }

  @Test
  public void testApplyKernelComplexKernel() {
    exModel.createImage(
        new CheckerboardImageCreator(1, 2, Color.BLACK, Color.WHITE));
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(255, 255, 255), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(255, 255, 255), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(1, 1));
    exModel.applyKernel(blurKernel, Channel.BLUE);
    exModel.applyKernel(blurKernel, Channel.GREEN);
    exModel.applyKernel(blurKernel, Channel.RED);
    assertEquals(new PixelImpl(63, 63, 63), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(79, 79, 79), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(79, 79, 79), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(63, 63, 63), exModel.getPixelAt(1, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyFilterNullFilter() {
    exModel.createImage(
        new CheckerboardImageCreator(1, 2, Color.BLUE, Color.RED));
    exModel.applyFilter(null);
  }

  @Test
  public void testApplyFilterBlur() {
    exModel.createImage(
        new CheckerboardImageCreator(1, 2, Color.BLACK, Color.WHITE));
    exModel.applyFilter(new BlurFilter());
    assertEquals(new PixelImpl(63, 63, 63), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(79, 79, 79), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(79, 79, 79), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(63, 63, 63), exModel.getPixelAt(1, 1));
  }

  @Test
  public void testApplyFilterSharpen() {
    exModel.createImage(
        new CheckerboardImageCreator(1, 3, Color.BLACK, Color.WHITE));
    exModel.applyFilter(new SharpenFilter());
    assertEquals(new PixelImpl(63, 63, 63), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(255, 255, 255), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(63, 63, 63), exModel.getPixelAt(2, 0));
    assertEquals(new PixelImpl(255, 255, 255), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(255, 255, 255), exModel.getPixelAt(1, 1));
    assertEquals(new PixelImpl(255, 255, 255), exModel.getPixelAt(2, 1));
    assertEquals(new PixelImpl(63, 63, 63), exModel.getPixelAt(0, 2));
    assertEquals(new PixelImpl(255, 255, 255), exModel.getPixelAt(1, 2));
    assertEquals(new PixelImpl(63, 63, 63), exModel.getPixelAt(2, 2));
  }

  @Test
  public void testApplyFilterGreyscale() {
    exModel.createImage(
        new CheckerboardImageCreator(1, 2, Color.GREEN, Color.BLUE));
    exModel.applyFilter(new GreyscaleFilter());
    assertEquals(new PixelImpl(182, 182, 182), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(18, 18, 18), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(18, 18, 18), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(182, 182, 182), exModel.getPixelAt(1, 1));
  }

  @Test
  public void testApplyFilterSepia() {
    exModel.createImage(
        new CheckerboardImageCreator(1, 2, Color.PINK, Color.RED));
    exModel.applyFilter(new SepiaFilter());
    assertEquals(new PixelImpl(255, 238, 185), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(100, 88, 69), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(100, 88, 69), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(255, 238, 185), exModel.getPixelAt(1, 1));
  }

  @Test
  public void testGetHeight() {
    assertEquals(0, exModel.getHeight());
    exModel.createImage(
        new CheckerboardImageCreator(2, 2, Color.YELLOW, Color.ORANGE));
    assertEquals(4, exModel.getHeight());
    exModel.createImage(
        new CheckerboardImageCreator(4, 3, Color.YELLOW, Color.ORANGE));
    assertEquals(12, exModel.getHeight());
  }

  @Test
  public void testGetPixelAtCheckerBoard() {
    m.createImage(new CheckerboardImageCreator(10, 3, Color.BLUE, Color.GREEN));

    assertEquals(m.getPixelAt(0, 0), new PixelImpl(Color.BLUE));

    // testing all edge cases just before color change
    assertEquals(m.getPixelAt(9, 0), new PixelImpl(Color.BLUE));
    assertEquals(m.getPixelAt(0, 9), new PixelImpl(Color.BLUE));
    assertEquals(m.getPixelAt(9, 0), new PixelImpl(Color.BLUE));

    assertEquals(m.getPixelAt(10, 0), new PixelImpl(Color.GREEN));
    assertEquals(m.getPixelAt(0, 10), new PixelImpl(Color.GREEN));
    assertEquals(m.getPixelAt(11, 0), new PixelImpl(Color.GREEN));
    assertEquals(m.getPixelAt(0, 11), new PixelImpl(Color.GREEN));

    // testing getPixelAt at ends of image
    assertEquals(m.getPixelAt(m.getHeight() - 1, m.getWidth() - 1), new PixelImpl(Color.BLUE));
    assertEquals(m.getPixelAt(0, m.getWidth() - 1), new PixelImpl(Color.BLUE));
    assertEquals(m.getPixelAt(m.getHeight() - 1, 0), new PixelImpl(Color.BLUE));
  }

  @Test
  public void getPixelAtImportedImage() {
    m.storeImage(ImageUtil.readPPM("res/TestImage2.ppm"));

    assertEquals(m.getPixelAt(2, 2), new PixelImpl(30, 43, 54));
    assertEquals(m.getPixelAt(0, m.getWidth() - 1), new PixelImpl(10, 34, 23));
    assertEquals(m.getPixelAt(m.getHeight() - 1, 0), new PixelImpl(111, 54, 12));
    assertEquals(m.getPixelAt(m.getHeight() - 1, m.getWidth() - 1), new PixelImpl(42, 10, 42));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getPixelAtInvalidX() {
    m.createImage(new CheckerboardImageCreator(2, 1, Color.BLUE, Color.GREEN));

    m.getPixelAt(3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getPixelAtInvalidY() {
    m.createImage(new CheckerboardImageCreator(2, 1, Color.BLUE, Color.GREEN));

    m.getPixelAt(0, -2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getPixelAtInvalidXY() {
    m.createImage(new CheckerboardImageCreator(2, 1, Color.BLUE, Color.GREEN));

    m.getPixelAt(6, -23);
  }

  @Test
  public void testGetWidth() {
    // testing with an even checkerboard
    int tileSize = 100;
    int numTiles = 5;
    int imageSize = tileSize * numTiles;

    m.createImage(new CheckerboardImageCreator(tileSize, numTiles, Color.BLACK, Color.WHITE));
    assertEquals(m.getWidth(), imageSize);
  }

  @Test
  public void testGetWidthUnevenDimensionCheckerboard() {
    int tileSize = 7;
    int numTiles = 3;
    int imageSize = tileSize * numTiles;

    m.createImage(new CheckerboardImageCreator(tileSize, numTiles, Color.RED, Color.ORANGE));
    assertEquals(m.getWidth(), imageSize);
  }

  @Test
  public void testGetWidthImportedImage() {
    m.storeImage(ImageUtil.readPPM("res/TestImage.ppm"));

    assertEquals(m.getWidth(), 2);

    setup();
    m.storeImage(ImageUtil.readPPM("res/TestImage2.ppm"));

    assertEquals(m.getWidth(), 4);
  }

  @Test
  public void testApplyLinearTransformationIdentityMatrix() {
    m.createImage(new CheckerboardImageCreator(10, 5, Color.BLUE, Color.YELLOW));
    expected.createImage(new CheckerboardImageCreator(10, 5, Color.BLUE, Color.YELLOW));

    // identity matrix will keep the image the exact same, no effect
    List<List<Double>> identityMatrix = new ArrayList<>(Arrays.asList(
        Arrays.asList(1.0, 0.0, 0.0),
        Arrays.asList(0.0, 1.0, 0.0),
        Arrays.asList(0.0, 0.0, 1.0)
    ));

    m.applyLinearTransformation(identityMatrix);

    for (int i = 0; i < m.getHeight(); i++) {
      for (int j = 0; j < m.getWidth(); j++) {
        assertEquals(m.getPixelAt(i, j), expected.getPixelAt(i, j));
      }
    }
  }

  @Test
  public void testApplyLinearTransformationScaleBy2() {
    double scale = 2.0;
    Color originalColor1 = new Color(25, 100, 125);
    Color originalColor2 = new Color(10, 20, 30);
    Color scaledColor1 = new Color((int) (25 * scale), (int) (100 * scale), (int) (125 * scale));
    Color scaledColor2 = new Color((int) (10 * scale), (int) (20 * scale), (int) (30 * scale));

    m.createImage(
        new CheckerboardImageCreator(
            10,
            5,
            originalColor1,
            originalColor2));
    expected.createImage(
        new CheckerboardImageCreator(
            10,
            5,
            scaledColor1,
            scaledColor2
        )
    );

    List<List<Double>> scalingMatrix = new ArrayList<>(Arrays.asList(
        Arrays.asList(scale, 0.0, 0.0),
        Arrays.asList(0.0, scale, 0.0),
        Arrays.asList(0.0, 0.0, scale)
    ));

    // apply scaling linear transformation
    m.applyLinearTransformation(scalingMatrix);

    for (int i = 0; i < m.getHeight(); i++) {
      for (int j = 0; j < m.getWidth(); j++) {
        assertEquals(m.getPixelAt(i, j), expected.getPixelAt(i, j));
      }
    }
  }

  @Test
  public void testDistinctLinearTransformation() {
    m.createImage(
        new CheckerboardImageCreator(
            1,
            5,
            new Color(17, 32, 9),
            new Color(23, 0, 11)));
    expected.createImage(
        new CheckerboardImageCreator(
            1,
            5,
            new Color(108, 127, 146),
            new Color(56, 87, 98)
        )
    );

    List<List<Double>> matrix =
        new ArrayList<>(Arrays.asList(
            Arrays.asList(1.0, 2.0, 3.0),
            Arrays.asList(2.33, 1.86, 3.1),
            Arrays.asList(4.3, 2.3, 0.0)
        ));

    m.applyLinearTransformation(matrix);

    for (int i = 0; i < m.getHeight(); i++) {
      for (int j = 0; j < m.getWidth(); j++) {
        assertEquals(m.getPixelAt(i, j), expected.getPixelAt(i, j));
      }
    }
  }

  @Test
  public void testScalingLinearTransformRectangularImage() {
    m.storeImage(ImageUtil.readPPM("res/RectangleTestImage.ppm"));

    double scale = 2.0;

    List<List<Double>> scalingMatrix = new ArrayList<>(Arrays.asList(
        Arrays.asList(scale, 0.0, 0.0),
        Arrays.asList(0.0, scale, 0.0),
        Arrays.asList(0.0, 0.0, scale)
    ));

    // apply scaling linear transformation
    m.applyLinearTransformation(scalingMatrix);

    assertEquals(m.getPixelAt(0, 0), new PixelImpl(200, 46, 108));
    assertEquals(m.getPixelAt(1, 0), new PixelImpl(20, 108, 12));
    assertEquals(m.getPixelAt(0, 1), new PixelImpl(68, 52, 112));
    assertEquals(m.getPixelAt(1, 1), new PixelImpl(60, 124, 202));
    assertEquals(m.getPixelAt(m.getHeight() / 2, m.getWidth() / 2), new PixelImpl(86, 60, 222));
    assertEquals(m.getPixelAt(m.getHeight() - 1, m.getWidth() - 1), new PixelImpl(124, 202, 176));
  }

  @Test
  public void testLinearTransformationRectangularImage() {
    m.storeImage(ImageUtil.readPPM("res/RectangleTestImage.ppm"));

    List<List<Double>> matrix =
        new ArrayList<>(Arrays.asList(
            Arrays.asList(1.40, 3.2, 0.36),
            Arrays.asList(1.3, 2.86, 0.01),
            Arrays.asList(4.3, 1.8, 2.1)
        ));

    assertEquals(m.getPixelAt(0, 0), new PixelImpl(100, 23, 54));
    assertEquals(m.getPixelAt(1, 0), new PixelImpl(10, 54, 6));
    assertEquals(m.getPixelAt(0, 1), new PixelImpl(34, 26, 56));
    assertEquals(m.getPixelAt(1, 1), new PixelImpl(30, 62, 101));

    m.applyLinearTransformation(matrix);

    assertEquals(m.getPixelAt(0, 0), new PixelImpl(233, 196, 255));
    assertEquals(m.getPixelAt(1, 0), new PixelImpl(188, 167, 152));
    assertEquals(m.getPixelAt(0, 1), new PixelImpl(150, 119, 255));
    assertEquals(m.getPixelAt(1, 1), new PixelImpl(255, 217, 255));
    assertEquals(m.getPixelAt(m.getHeight() / 2, m.getWidth() - 1), new PixelImpl(242, 215, 228));
    assertEquals(m.getPixelAt(m.getHeight() - 1, m.getWidth() - 1), new PixelImpl(255, 255, 255));
  }

  @Test
  public void testApplyLinearTransformationUpperBound() {
    m.createImage(
        new CheckerboardImageCreator(1, 2, new Color(200, 200, 200), new Color(0, 150, 32)));

    List<List<Double>> scalingMatrix = new ArrayList<>(Arrays.asList(
        Arrays.asList(2.0, 0.0, 0.0),
        Arrays.asList(0.0, 2.0, 0.0),
        Arrays.asList(0.0, 0.0, 2.0)
    ));

    // this should bring some RGB values above 255, which should be clamped
    m.applyLinearTransformation(scalingMatrix);

    assertEquals(m.getPixelAt(0, 0), new PixelImpl(255, 255, 255));
    assertEquals(m.getPixelAt(1, 0), new PixelImpl(0, 255, 64));
  }

  @Test
  public void testApplyLinearTransformationLowerBound() {
    m.createImage(new CheckerboardImageCreator(1, 2, new Color(1, 0, 0), new Color(1, 1, 1)));

    List<List<Double>> scalingMatrix = new ArrayList<>(Arrays.asList(
        Arrays.asList(-2.0, 0.0, 0.0),
        Arrays.asList(0.0, -2.0, 0.0),
        Arrays.asList(0.0, 0.0, -2.0)
    ));

    m.applyLinearTransformation(scalingMatrix);

    assertEquals(m.getPixelAt(0, 0), new PixelImpl(0, 0, 0));
    assertEquals(m.getPixelAt(1, 0), new PixelImpl(0, 0, 0));
  }

  @Test
  public void testLinearTransformationLowerBound() {
    m.createImage(new CheckerboardImageCreator(1, 2, new Color(23, 1, 0), new Color(40, 0, 1)));

    List<List<Double>> scalingMatrix = new ArrayList<>(Arrays.asList(
        Arrays.asList(1.0, 0.0, 0.0),
        Arrays.asList(0.0, -2.0, 0.0),
        Arrays.asList(0.0, 0.0, -2.0)
    ));

    m.applyLinearTransformation(scalingMatrix);

    assertEquals(m.getPixelAt(0, 0), new PixelImpl(23, 0, 0));
    assertEquals(m.getPixelAt(1, 0), new PixelImpl(40, 0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidLinearTransformation() {
    m.storeImage(ImageUtil.readPPM("res/TestImage.ppm"));

    m.applyLinearTransformation(null);
  }

  @Test
  public void testCreateImageCheckerboard() {
    int tileSize = 2;
    int numTiles = 5;

    m.createImage(
        new CheckerboardImageCreator(
            tileSize,
            numTiles,
            new Color(23, 10, 54),
            new Color(20, 40, 60)));

    assertEquals(m.getWidth(), tileSize * numTiles);
    assertEquals(m.getHeight(), tileSize * numTiles);

    // testing that the area defined by tileSize is the same color
    assertEquals(m.getPixelAt(0, 0), new PixelImpl(23, 10, 54));
    assertEquals(m.getPixelAt(1, 0), new PixelImpl(23, 10, 54));
    assertEquals(m.getPixelAt(0, 1), new PixelImpl(23, 10, 54));

    // test that the first tileColor is the same every other tile
    for (int i = 0; i < numTiles; i += 2) {
      assertEquals(m.getPixelAt(0, i * tileSize), new PixelImpl(23, 10, 54));
      assertEquals(m.getPixelAt(i * tileSize, 0), new PixelImpl(23, 10, 54));
    }

    // test that the color changes and maintains itself every other tile
    for (int i = 1; i < numTiles; i += 2) {
      assertEquals(m.getPixelAt(0, i * tileSize), new PixelImpl(20, 40, 60));
      assertEquals(m.getPixelAt(i * tileSize, 0), new PixelImpl(20, 40, 60));
    }

    // testing diagonals
    for (int i = 0; i < numTiles; i++) {
      assertEquals(m.getPixelAt(i * tileSize, i * tileSize), new PixelImpl(23, 10, 54));
    }
  }

  @Test
  public void testCreateImageUsingOwnCreator() {

    /**
     * Testing class. ImageCreator class that just creates a solid blue image of height and width
     * 10.
     */
    class SolidBlueImageSize10 implements ImageCreator {

      @Override
      public List<List<Pixel>> create() {
        List<List<Pixel>> image = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
          image.add(new ArrayList<>());
          for (int j = 0; j < 10; j++) {
            image.get(i).add(new PixelImpl(Color.BLUE));
          }
        }
        return image;
      }
    }

    m.createImage(new SolidBlueImageSize10());
    // a solid blue image is the same as a single blue tile in a checkerboard
    expected.createImage(new CheckerboardImageCreator(10, 1, Color.BLUE, Color.BLACK));

    assertEquals(m.getWidth(), 10);
    assertEquals(m.getHeight(), 10);

    for (int i = 0; i < m.getHeight(); i++) {
      for (int j = 0; j < m.getWidth(); j++) {
        assertEquals(m.getPixelAt(i, j), expected.getPixelAt(i, j));
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStoreImageNullImage() {
    exModel.storeImage(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStoreImageNonRectangularImage() {
    List<List<Pixel>> badImage = new ArrayList<>(
        Arrays.asList(
            Arrays.asList(new PixelImpl(Color.BLACK), new PixelImpl(Color.WHITE)),
            Arrays.asList(new PixelImpl(Color.BLUE))));
    exModel.storeImage(badImage);
  }

  @Test
  public void testStoreImage() {
    // initialize with an imported image
    m.storeImage(ImageUtil.readPPM("res/TestImage2.ppm"));

    assertEquals(m.getWidth(), 4);
    assertEquals(m.getHeight(), 4);
    assertEquals(m.getPixelAt(0, 0), new PixelImpl(12, 15, 100));
    assertEquals(m.getPixelAt(m.getHeight() / 2, m.getHeight() / 2), new PixelImpl(30, 43, 54));
    assertEquals(m.getPixelAt(m.getHeight() - 1, m.getWidth() - 1), new PixelImpl(42, 10, 42));

    List<List<Pixel>> newImage = new ArrayList<>();
    for (int i = 0; i < 256; i++) {
      newImage.add(new ArrayList<>());
      for (int j = 0; j < 256; j++) {
        newImage.get(i).add(new PixelImpl(i, j, i / 2));
      }
    }

    // changing the image stored inside ImageModel m
    m.storeImage(newImage);

    assertEquals(m.getWidth(), 256);
    assertEquals(m.getHeight(), 256);
    assertEquals(m.getPixelAt(0, 0), new PixelImpl(0, 0, 0));
    assertEquals(m.getPixelAt(m.getWidth() / 2, m.getHeight() / 2), new PixelImpl(128, 128, 64));
    assertEquals(m.getPixelAt(m.getWidth() - 1, m.getHeight() - 1), new PixelImpl(255, 255, 127));
  }
}

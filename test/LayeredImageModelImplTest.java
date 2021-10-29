import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cs3500.imageprocessing.controller.ImportImage;
import cs3500.imageprocessing.controller.LoadImage;
import cs3500.imageprocessing.controller.MosaicImage;
import cs3500.imageprocessing.controller.ResizeLayer;
import cs3500.imageprocessing.filters.BlurFilter;
import cs3500.imageprocessing.filters.SharpenFilter;
import cs3500.imageprocessing.model.Channel;
import cs3500.imageprocessing.model.CheckerboardImageCreator;
import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.ImageUtil;
import cs3500.imageprocessing.model.LayeredImageModelImpl;
import cs3500.imageprocessing.model.Pixel;
import cs3500.imageprocessing.model.PixelImpl;
import cs3500.imageprocessing.model.SingleImageModel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the public facing functionality of {@code LayeredImageModelImpl}.
 */
public class LayeredImageModelImplTest {

  private LayeredImageModelImpl actualLayer;
  private SingleImageModel expectedLayer;

  /*
   * Compares the current layer's image of two given ImageModels.
   *
   * @param model1 the first model to compare
   * @param model2 the second model to compare images to
   * @return true if the two models have the same image as their current layer. Similarity is
   *         determined through checking every single pixel.
   */
  private boolean compareImageModels(ImageModel model1, ImageModel model2) {
    if (model1.getWidth() != model2.getWidth()
        || model1.getHeight() != model2.getHeight()) {
      return false;
    }

    for (int i = 0; i < model1.getHeight(); i++) {
      for (int j = 0; j < model1.getWidth(); j++) {
        if (!model1.getPixelAt(i, j).equals(model2.getPixelAt(i, j))) {
          return false;
        }
      }
    }
    return true;
  }

  @Before
  public void setup() {
    this.actualLayer = new LayeredImageModelImpl();
    this.expectedLayer = new SingleImageModel();
  }

  @Test
  public void testStoreImage() {
    List<List<Pixel>> blueImage = new ArrayList<>();

    // constructs a 10 by 10 image of all blue pixels
    for (int i = 0; i < 10; i++) {
      blueImage.add(new ArrayList<>());
      for (int j = 0; j < 10; j++) {
        blueImage.get(i).add(new PixelImpl(Color.BLUE));
      }
    }

    actualLayer.storeImage(blueImage);
    expectedLayer.storeImage(blueImage);

    assertTrue(compareImageModels(actualLayer, expectedLayer));
  }

  @Test
  public void testStoreMultipleImages() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    new ImportImage("res/TestImage.ppm").execute(actualLayer);

    assertEquals(actualLayer.numLayers(), 2);
    assertEquals(actualLayer.getHeight(), 2);
    assertEquals(actualLayer.getWidth(), 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStoreImageWrongSize() {
    new ImportImage("src/TestImage.ppm").execute(actualLayer);

    List<List<Pixel>> wrongSizedImage = new ArrayList<>();
    // making an image of size 10 by 10, not correct size of TestImage.ppm
    for (int i = 0; i < 7; i++) {
      wrongSizedImage.add(new ArrayList<>());
      for (int j = 0; j < 10; j++) {
        wrongSizedImage.get(i).add(new PixelImpl(Color.GREEN));
      }
    }

    actualLayer.storeImage(wrongSizedImage);
  }

  @Test
  public void testGetPixelAt() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    expectedLayer.storeImage(ImageUtil.readPPM("res/TestImage.ppm"));

    assertTrue(expectedLayer.getPixelAt(0, 0).equals(actualLayer.getPixelAt(0, 0)));
    assertTrue(expectedLayer.getPixelAt(0, 1).equals(actualLayer.getPixelAt(0, 1)));
    assertTrue(expectedLayer.getPixelAt(1, 0).equals(actualLayer.getPixelAt(1, 0)));
    assertTrue(expectedLayer.getPixelAt(1, 1).equals(actualLayer.getPixelAt(1, 1)));
    assertEquals(
        expectedLayer.getPixelAt(expectedLayer.getHeight() - 1, expectedLayer.getWidth() - 1),
        actualLayer.getPixelAt(actualLayer.getHeight() - 1, actualLayer.getWidth() - 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelOutOfXBound() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    expectedLayer.getPixelAt(-2, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelOutOfXBound2() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    expectedLayer.getPixelAt(10, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelOutOfYBound() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    expectedLayer.getPixelAt(0, -13);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelOutOfYBound2() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    expectedLayer.getPixelAt(0, 12);
  }

  @Test
  public void testGetDimensionsStoredImage() {
    List<List<Pixel>> testImage = new ArrayList<>();

    // constructs an image that is 7 by 10
    for (int i = 0; i < 7; i++) {
      testImage.add(new ArrayList<>());
      for (int j = 0; j < 10; j++) {
        testImage.get(i).add(new PixelImpl(Color.RED));
      }
    }

    actualLayer.storeImage(testImage);

    assertEquals(actualLayer.getHeight(), 7);
    assertEquals(actualLayer.getWidth(), 10);
  }

  @Test
  public void testGetDimensionsImportedImage() {
    new ImportImage("res/TestImage2.ppm").execute(actualLayer);

    assertEquals(actualLayer.getHeight(), 4);
    assertEquals(actualLayer.getWidth(), 4);
  }

  @Test
  public void testApplyKernelBlur() {
    List<List<Double>> blurKernel = new ArrayList<>(
        Arrays.asList(Arrays.asList(0.0625, 0.125, 0.0625),
            Arrays.asList(0.125, 0.25, 0.125),
            Arrays.asList(0.0625, 0.125, 0.0625))
    );

    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    expectedLayer.storeImage(ImageUtil.readPPM("res/TestImage.ppm"));

    actualLayer.applyKernel(blurKernel, Channel.RED);
    expectedLayer.applyKernel(blurKernel, Channel.RED);
    assertTrue(compareImageModels(actualLayer, expectedLayer));

    actualLayer.applyKernel(blurKernel, Channel.GREEN);
    expectedLayer.applyKernel(blurKernel, Channel.GREEN);
    assertTrue(compareImageModels(actualLayer, expectedLayer));

    actualLayer.applyKernel(blurKernel, Channel.BLUE);
    expectedLayer.applyKernel(blurKernel, Channel.BLUE);
    assertTrue(compareImageModels(actualLayer, expectedLayer));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyInvalidKernelChannelRed() {
    new ImportImage("res/TestImage2.ppm").execute(actualLayer);
    actualLayer.applyKernel(null, Channel.RED);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyInvalidKernelChannelBlue() {
    new ImportImage("res/TestImage2.ppm").execute(actualLayer);
    actualLayer.applyKernel(null, Channel.BLUE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyInvalidKernelChannelGreen() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    actualLayer.applyKernel(null, Channel.GREEN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testKernelInvalidSize() {

    new ImportImage("res/TestImage.ppm").execute(actualLayer);

    List<List<Double>> invalidKernel = new ArrayList<>(
        Arrays.asList(
            Arrays.asList(0.25, 1.00),
            Arrays.asList(1.00)
        )
    );

    actualLayer.applyKernel(invalidKernel, Channel.RED);
  }

  @Test
  public void applyLinearTransformationGreyscale() {
    List<List<Double>> greyscale = new ArrayList<>(
        Arrays.asList(
            Arrays.asList(0.2126, 0.7152, 0.0722),
            Arrays.asList(0.2126, 0.7152, 0.0722),
            Arrays.asList(0.2126, 0.7152, 0.0722)
        ));

    actualLayer.applyLinearTransformation(greyscale);
    expectedLayer.applyLinearTransformation(greyscale);

    assertTrue(compareImageModels(expectedLayer, actualLayer));
  }

  @Test
  public void applyLinearTransformationSepia() {
    List<List<Double>> sepia = new ArrayList<>(
        Arrays.asList(
            Arrays.asList(0.393, 0.769, 0.189),
            Arrays.asList(0.349, 0.686, 0.168),
            Arrays.asList(0.272, 0.534, 0.131)
        ));

    actualLayer.applyLinearTransformation(sepia);
    expectedLayer.applyLinearTransformation(sepia);

    assertTrue(compareImageModels(expectedLayer, actualLayer));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyInvalidLinearTransformation() {
    new ImportImage("res/TestImage2.ppm").execute(actualLayer);
    actualLayer.applyLinearTransformation(null);
  }

  @Test
  public void testApplyBlurFilter() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    expectedLayer.storeImage(ImageUtil.readPPM("res/TestImage.ppm"));

    actualLayer.applyFilter(new BlurFilter());
    expectedLayer.applyFilter(new BlurFilter());

    assertTrue(compareImageModels(actualLayer, expectedLayer));

    assertEquals(actualLayer.getPixelAt(0, 0), new PixelImpl(79, 79, 79));
    assertEquals(actualLayer.getPixelAt(0, 1), new PixelImpl(63, 63, 63));
    assertEquals(actualLayer.getPixelAt(1, 0), new PixelImpl(63, 63, 63));
    assertEquals(actualLayer.getPixelAt(1, 1), new PixelImpl(79, 79, 79));
  }

  @Test
  public void testApplySharpenFilter() {
    new ImportImage("res/TestImage2.ppm").execute(actualLayer);
    expectedLayer.storeImage(ImageUtil.readPPM("res/TestImage2.ppm"));

    actualLayer.applyFilter(new SharpenFilter());
    expectedLayer.applyFilter(new SharpenFilter());

    assertTrue(compareImageModels(actualLayer, expectedLayer));

    assertEquals(actualLayer.getPixelAt(0, 0), new PixelImpl(11, 0, 96));
    assertEquals(actualLayer.getPixelAt(0, 1), new PixelImpl(49, 53, 46));
    assertEquals(actualLayer.getPixelAt(1, 0), new PixelImpl(41, 35, 106));
    assertEquals(actualLayer.getPixelAt(1, 1), new PixelImpl(77, 100, 84));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFilter() {
    new ImportImage("res/TestImage2.ppm").execute(actualLayer);
    actualLayer.applyFilter(null);
  }

  @Test
  public void testNumLayers() {
    assertEquals(actualLayer.numLayers(), 0);

    new ImportImage("res/TestImage2.ppm").execute(actualLayer);
    assertEquals(actualLayer.numLayers(), 1);

    List<List<Pixel>> testImage = new ArrayList<>();

    // constructs an image that is 4 by 4
    for (int i = 0; i < 4; i++) {
      testImage.add(new ArrayList<>());
      for (int j = 0; j < 4; j++) {
        testImage.get(i).add(new PixelImpl(Color.GREEN));
      }
    }

    actualLayer.storeImage(testImage);
    assertEquals(actualLayer.numLayers(), 2);

    actualLayer.removeLayer(0);
    assertEquals(actualLayer.numLayers(), 1);
  }

  @Test
  public void testRemoveTopLayer() {
    // this is the first layer that actualLayer should have
    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    expectedLayer.storeImage(ImageUtil.readPPM("res/TestImage.ppm"));

    List<List<Pixel>> secondLayer = new ArrayList<>();
    // adding one more layer on top
    for (int i = 0; i < 2; i++) {
      secondLayer.add(new ArrayList<>());
      for (int j = 0; j < 2; j++) {
        secondLayer.get(i).add(new PixelImpl(Color.GREEN));
      }
    }

    actualLayer.storeImage(secondLayer);

    // the current topmost layer for actualLayer should be different from the first layer
    assertFalse(compareImageModels(expectedLayer, actualLayer));

    actualLayer.removeLayer(1);

    // now, after removing the top, the new topmost layer should be the same as the first image
    assertTrue(compareImageModels(expectedLayer, actualLayer));
  }

  @Test
  public void testRemoveLayerInBetween() {
    List<List<Pixel>> firstLayer = new ArrayList<>();
    // adding one more layer on top
    for (int i = 0; i < 4; i++) {
      firstLayer.add(new ArrayList<>());
      for (int j = 0; j < 4; j++) {
        firstLayer.get(i).add(new PixelImpl(Color.GREEN));
      }
    }

    // this makes the first layer
    actualLayer.storeImage(firstLayer);

    // second layer, which should be the topmost
    new ImportImage("res/TestImage2.ppm").execute(actualLayer);
    expectedLayer.storeImage(ImageUtil.readPPM("res/TestImage2.ppm"));

    assertTrue(compareImageModels(actualLayer, expectedLayer));

    // since the bottom layer is removed, the top layer should remain unchanged
    actualLayer.removeLayer(0);

    assertTrue(compareImageModels(actualLayer, expectedLayer));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveInvalidIndex() {
    actualLayer.removeLayer(-3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveInvalidIndex2() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    actualLayer.removeLayer(1);
  }

  @Test
  public void testCurrentLayer() {
    new ImportImage("res/TestImage2.ppm").execute(actualLayer);
    expectedLayer.storeImage(ImageUtil.readPPM("res/TestImage2.ppm"));

    List<List<Pixel>> secondLayer = new ArrayList<>();
    // adding one more layer on top
    for (int i = 0; i < 4; i++) {
      secondLayer.add(new ArrayList<>());
      for (int j = 0; j < 4; j++) {
        secondLayer.get(i).add(new PixelImpl(Color.GREEN));
      }
    }
    actualLayer.storeImage(secondLayer);

    // since the current layer is the secondLayer, this test should fail
    assertFalse(compareImageModels(actualLayer, expectedLayer));

    // change currentlayer to be the bottom one
    actualLayer.selectCurrentLayer(0);

    assertTrue(compareImageModels(actualLayer, expectedLayer));
  }

  @Test
  public void testIsVisible() {
    new LoadImage("res/save").execute(actualLayer);

    assertTrue(actualLayer.isVisible(0));
    assertFalse(actualLayer.isVisible(1));
  }

  @Test
  public void testToggleImage() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);

    List<List<Pixel>> secondLayer = new ArrayList<>();
    // adding one more layer on top
    for (int i = 0; i < 2; i++) {
      secondLayer.add(new ArrayList<>());
      for (int j = 0; j < 2; j++) {
        secondLayer.get(i).add(new PixelImpl(Color.GREEN));
      }
    }

    actualLayer.storeImage(secondLayer);

    assertEquals(actualLayer.isVisible(0), true);
    assertTrue(actualLayer.isVisible(1));

    // toggling top layer: should leave bottom layer intact
    actualLayer.toggleLayer(0);

    assertFalse(actualLayer.isVisible(0));
    assertTrue(actualLayer.isVisible(1));

    // toggling back
    actualLayer.toggleLayer(0);
    assertTrue(actualLayer.isVisible(0));
    assertTrue(actualLayer.isVisible(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToggleInvalidIndex() {
    actualLayer.toggleLayer(5);
  }

  @Test
  public void testGetTopmostVisible() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);
    expectedLayer.storeImage(ImageUtil.readPPM("res/TestImage.ppm"));

    List<List<Pixel>> topLayer = new ArrayList<>();
    // adding one more layer on top
    for (int i = 0; i < 2; i++) {
      topLayer.add(new ArrayList<>());
      for (int j = 0; j < 2; j++) {
        topLayer.get(i).add(new PixelImpl(Color.GREEN));
      }
    }

    // making a new model to store the top layer image, for comparison purposes later
    ImageModel topLayerModel = new SingleImageModel();

    topLayerModel.storeImage(topLayer);
    actualLayer.storeImage(topLayer);

    assertTrue(compareImageModels(actualLayer.getTopmostVisibleLayer(), topLayerModel));

    // toggling topmost layer to be false
    actualLayer.toggleLayer(1);

    // getTopMostVisibleLayer should now return a different layer
    assertFalse(compareImageModels(actualLayer.getTopmostVisibleLayer(), topLayerModel));
    assertTrue(compareImageModels(actualLayer.getTopmostVisibleLayer(), expectedLayer));
  }

  @Test(expected = IllegalCallerException.class)
  public void testGetTopmostNoLayers() {
    actualLayer.getTopmostVisibleLayer();
  }

  @Test
  public void testCreatorCheckerboard() {
    actualLayer.createImage(new CheckerboardImageCreator(5, 5, Color.RED, Color.BLUE));
    expectedLayer.createImage(new CheckerboardImageCreator(5, 5, Color.RED, Color.BLUE));

    assertTrue(compareImageModels(actualLayer, expectedLayer));

    assertEquals(actualLayer.getPixelAt(0, 0), new PixelImpl(Color.RED));
    assertEquals(actualLayer.getPixelAt(5, 0), new PixelImpl(Color.BLUE));
    assertEquals(actualLayer.getPixelAt(0, 5), new PixelImpl(Color.BLUE));
    assertEquals(actualLayer.getPixelAt(5, 5), new PixelImpl(Color.RED));
    assertEquals(actualLayer.getPixelAt(17, 10), new PixelImpl(Color.BLUE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateImageNullCreator() {
    actualLayer.createImage(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateCheckerboardNotMatchingSize() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);

    // attempting to create an image that doesn't match current layers
    actualLayer.createImage(new CheckerboardImageCreator(10, 10, Color.BLUE, Color.RED));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateCheckerboardNullColors() {
    actualLayer.createImage(new CheckerboardImageCreator(2, 2, null, null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateCheckerboardNegativeTileSize() {
    actualLayer.createImage(new CheckerboardImageCreator(-3, 2, Color.BLUE, Color.RED));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateCheckerboardNegativeNumTiles() {
    actualLayer.createImage(new CheckerboardImageCreator(3, -2, Color.BLUE, Color.RED));
  }

  @Test
  public void testResize() {
    new ImportImage("res/test2.jpg").execute(actualLayer);
    assertEquals(actualLayer.numLayers(), 1);
    assertEquals(actualLayer.getWidth(), 1060);
    assertEquals(actualLayer.getHeight(), 596);

    new ResizeLayer(500, 250).execute(actualLayer);
    assertEquals(actualLayer.numLayers(), 1);
    assertEquals(actualLayer.getWidth(), 500);
    assertEquals(actualLayer.getHeight(), 250);
  }

  @Test
  public void testResizeMultipleLayers() {
    actualLayer.createImage(new CheckerboardImageCreator(10, 10, Color.BLUE, Color.GREEN));

    assertEquals(actualLayer.numLayers(), 1);
    assertEquals(actualLayer.getWidth(), 100);
    assertEquals(actualLayer.getHeight(), 100);

    actualLayer.createImage(new CheckerboardImageCreator(10, 10, Color.RED, Color.ORANGE));
    assertEquals(actualLayer.numLayers(), 2);
    assertEquals(actualLayer.getWidth(), 100);
    assertEquals(actualLayer.getHeight(), 100);

    actualLayer.createImage(new CheckerboardImageCreator(10, 10, Color.RED, Color.ORANGE));
    assertEquals(actualLayer.numLayers(), 3);
    assertEquals(actualLayer.getWidth(), 100);
    assertEquals(actualLayer.getHeight(), 100);

    new ResizeLayer(50, 50).execute(actualLayer);

    assertEquals(actualLayer.numLayers(), 3);
    assertEquals(actualLayer.getWidth(), 50);
    assertEquals(actualLayer.getHeight(), 50);

    // checking that all layers also have been resized
    actualLayer.selectCurrentLayer(1);
    assertEquals(actualLayer.getWidth(), 50);
    assertEquals(actualLayer.getHeight(), 50);

    actualLayer.selectCurrentLayer(0);
    assertEquals(actualLayer.getWidth(), 50);
    assertEquals(actualLayer.getHeight(), 50);
  }

  @Test
  public void testResizeSquare() {
    actualLayer.createImage(new CheckerboardImageCreator(10, 10, Color.GREEN, Color.PINK));

    assertEquals(actualLayer.getHeight(), 100);
    assertEquals(actualLayer.getWidth(), 100);

    new ResizeLayer(50, 50).execute(actualLayer);

    assertEquals(actualLayer.getHeight(), 50);
    assertEquals(actualLayer.getWidth(), 50);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testResizeFailsOnAttemptedUpscale() {
    actualLayer.createImage(new CheckerboardImageCreator(10, 10, Color.GREEN, Color.PINK));

    new ResizeLayer(200, 200).execute(actualLayer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMosaicNegativeSeeds() {
    actualLayer.createImage(new CheckerboardImageCreator(10, 10, Color.GREEN, Color.PINK));
    new MosaicImage(-1).execute(actualLayer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMosaicTooManySeeds() {
    actualLayer.createImage(new CheckerboardImageCreator(2, 10, Color.RED, Color.PINK));
    new MosaicImage(401).execute(actualLayer);
  }

  @Test
  public void testMosaicValidSeeds() {
    actualLayer.createImage(new CheckerboardImageCreator(1, 1, Color.RED, Color.PINK));
    new MosaicImage(1).execute(actualLayer);
    assertEquals(new PixelImpl(Color.RED), actualLayer.getPixelAt(0, 0));
  }

  @Test
  public void testMosaicValidSeedsBigger() {
    actualLayer.createImage(new CheckerboardImageCreator(1, 2, Color.RED, Color.GREEN));
    new MosaicImage(4).execute(actualLayer);
    assertEquals(new PixelImpl(Color.RED), actualLayer.getPixelAt(0, 0));
    assertEquals(new PixelImpl(Color.GREEN), actualLayer.getPixelAt(1, 0));
    assertEquals(new PixelImpl(Color.GREEN), actualLayer.getPixelAt(0, 1));
    assertEquals(new PixelImpl(Color.RED), actualLayer.getPixelAt(1, 1));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCurrentLayerIndexNoLayers() {
    actualLayer.getCurrentLayerIndex();
  }

  @Test
  public void testGetCurrentLayerIndex() {
    actualLayer.createImage(new CheckerboardImageCreator(10, 10, Color.GREEN, Color.PINK));
    assertEquals(0, actualLayer.getCurrentLayerIndex());
    actualLayer.createImage(new CheckerboardImageCreator(20, 5, Color.BLUE, Color.ORANGE));
    assertEquals(1, actualLayer.getCurrentLayerIndex());
    actualLayer.selectCurrentLayer(0);
    assertEquals(0, actualLayer.getCurrentLayerIndex());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpsizeImageError() {
    new ImportImage("res/test1.jpg");

    new ResizeLayer(1000, 1000).execute(actualLayer);
  }
}

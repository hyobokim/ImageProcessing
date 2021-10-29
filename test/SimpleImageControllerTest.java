import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import cs3500.imageprocessing.controller.ImageController;
import cs3500.imageprocessing.controller.ImportImage;
import cs3500.imageprocessing.controller.SimpleImageController;
import cs3500.imageprocessing.filters.BlurFilter;
import cs3500.imageprocessing.filters.GreyscaleFilter;
import cs3500.imageprocessing.filters.SepiaFilter;
import cs3500.imageprocessing.filters.SharpenFilter;
import cs3500.imageprocessing.model.CheckerboardImageCreator;
import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.ImageUtil;
import cs3500.imageprocessing.model.LayeredImageModel;
import cs3500.imageprocessing.model.LayeredImageModelImpl;
import cs3500.imageprocessing.model.SingleImageModel;
import cs3500.imageprocessing.view.TextImageView;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the public facing functionality of {@link SimpleImageController}.
 */
public class SimpleImageControllerTest {

  private SingleImageModel expectedLayer;
  private LayeredImageModel actualLayer;
  private LayeredImageModel exLayer;

  /**
   * Produces an interaction that represents the given lines being printed.
   *
   * @param lines the lines to be printed
   * @return an interaction representing the printing of the given lines
   */
  private static Interaction prints(String... lines) {
    return (input, output) -> {
      for (String line : lines) {
        output.append(line);
      }
    };
  }

  /**
   * Produces an interaction that represents a given String being inputted.
   *
   * @param in the input
   * @return an interaction representing the inputting of the given String
   */
  private static Interaction inputs(String in) {
    return (input, output) -> {
      input.append(in);
    };
  }

  /*
   * Test harness that tests the playGame method through a series of given interactions.
   */
  private boolean testStart(LayeredImageModel model, Interaction... interactions) {
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    for (Interaction i : interactions) {
      i.apply(fakeUserInput, expectedOutput);
    }

    Readable input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    ImageController controller = new SimpleImageController(
        input, new TextImageView(actualOutput), model);
    controller.start();

    return expectedOutput.toString().equals(actualOutput.toString());
  }

  /**
   * Compares two {@code ImageModel} for equality of pixels.
   *
   * @param model1 the first model
   * @param model2 the second model
   * @return true if they have the same pixels, false if not
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
    expectedLayer = new SingleImageModel();
    actualLayer = new LayeredImageModelImpl();
    exLayer = new LayeredImageModelImpl();
  }

  @Test
  public void testExtraArgumentsErrors() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard 5 5 (255 255 255) (0 0 0) 1\n"),
        prints("Error: command failed. Additional arguments found.\n"),
        inputs("blur 1\n"),
        prints("Error: additional arguments found.\n"),
        inputs("sepia first\n"),
        prints("Error: additional arguments found.\n"),
        inputs("grayscale layer\n"),
        prints("Error: additional arguments found.\n"),
        inputs("sharpen image\n"),
        prints("Error: additional arguments found.\n"),
        inputs("save exFilename png here #even more\n"),
        prints("Error: additional arguments found.\n"),
        inputs("load exFilename png\n"),
        inputs("blur\n"),
        prints("Error: additional arguments found.\n"),
        inputs("export res/filename png 1\n"),
        prints("Error: additional arguments found.\n"),
        inputs("import res/filename 0 #extra\n"),
        prints("Error: additional arguments found.\n")
    );

    assertTrue(result);
  }

  @Test(expected = IllegalStateException.class)
  public void testControllerException() {
    String input = "invalidcommandtotriggerviewoutput\n";
    ImageController controller = new SimpleImageController(new StringReader(input),
        new TextImageView(new MockAppendable()), exLayer);
    controller.start();
  }

  @Test
  public void testInvalidCommands() {
    boolean result = testStart(
        actualLayer,
        inputs("blurimage\n"),
        prints("Error: invalid command.\n"),
        inputs("sharpenimage\n"),
        prints("Error: invalid command.\n"),
        inputs("setCurrent 1\n"),
        prints("Error: invalid command.\n"),
        inputs("importImage\n"),
        prints("Error: invalid command.\n")
    );

    assertTrue(result);
  }

  @Test
  public void testValidCommandsMissingArguments() {
    boolean result = testStart(
        actualLayer,
        inputs("load\n"),
        prints("Error: command not followed by proper arguments.\n"),
        inputs("save filename\n"),
        prints("Error: command not followed by proper arguments.\n"),
        inputs("import\n"),
        prints("Error: command not followed by proper arguments.\n"),
        inputs("export res/file\n"),
        prints("Error: command not followed by proper arguments.\n"),
        inputs("select first #oops! shoulda been an int!\n"),
        prints("Error: command not followed by proper arguments.\n"),
        inputs("toggle\n"),
        prints("Error: command not followed by proper arguments.\n"),
        inputs("remove duckling\n"),
        prints("Error: command not followed by proper arguments.\n"),
        inputs("mosaic \n"),
        prints("Error: command not followed by proper arguments.\n"),
        inputs("resize 0 qw \n"),
        prints("Error: command not followed by proper arguments.\n")
    );

    assertTrue(result);
  }

  @Test
  public void testValidCommandsInvalidArguments() {
    boolean result = testStart(
        actualLayer,
        inputs("load fakefile\n"),
        prints("Error: command failed. Invalid file.\n"),
        inputs("import nonsense\n"),
        prints("Error: command failed. Invalid file.\n"),
        inputs("select 2\n"),
        prints("Error: command failed. Invalid index.\n"),
        inputs("mosaic -1\n"),
        prints("Error: command failed. The number of seeds must be positive.\n"),
        inputs("toggle -1\n"),
        prints("Error: command failed. Invalid index.\n"),
        inputs("remove 100\n"),
        prints("Error: command failed. Invalid index.\n")
    );

    assertTrue(result);
  }

  @Test
  public void testBlur() {
    testStart(actualLayer, inputs("create checkerboard 2 1 (0 0 0) (255 255 255)\nblur\n"));
    expectedLayer.createImage(new CheckerboardImageCreator(2, 1, Color.BLACK, Color.WHITE));
    new BlurFilter().apply(expectedLayer);

    assertTrue(compareImageModels(actualLayer, expectedLayer));
  }

  @Test
  public void testToggleVisible() {
    testStart(actualLayer, inputs("create checkerboard 2 1 (0 0 0) (255 255 255)\ntoggle 1\n"));
    testStart(exLayer, inputs("create checkerboard 2 1 (0 0 0) (255 255 255)\ntoggle 1\ntoggle 1"));

    assertFalse(actualLayer.isVisible(0));
    assertTrue(exLayer.isVisible(0));
  }

  @Test
  public void testRemoveLayer() {
    testStart(actualLayer, inputs("create checkerboard 2 1 (0 0 0) (255 255 255)\nremove 1\n"));
    testStart(exLayer, inputs("create checkerboard 2 1 (0 0 0) (255 255 255)\n"),
        inputs("create checkerboard 2 1 (0 0 0) (255 255 255)\n"),
        inputs("create checkerboard 2 1 (0 0 0) (255 255 255)\n"),
        inputs("remove 1\n"));

    assertEquals(0, actualLayer.numLayers());
    assertEquals(2, exLayer.numLayers());
  }

  @Test
  public void testSelectLayer() {
    testStart(exLayer, inputs("create checkerboard 4 8 (255 0 0) (0 255 0)\n"),
        inputs("create checkerboard 4 8 (0 0 0) (255 255 255)\n"),
        inputs("create checkerboard 4 8 (0 0 0) (255 255 255)\n"),
        inputs("select 1\n"));
    testStart(actualLayer, inputs("create checkerboard 4 8 (255 0 0) (0 255 0)\n"),
        inputs("create checkerboard 4 8 (0 0 0) (255 255 255)\n"),
        inputs("create checkerboard 4 8 (0 0 0) (255 255 255)\n"),
        inputs("select 2\n"));

    expectedLayer.createImage(new CheckerboardImageCreator(4, 8, Color.RED, Color.GREEN));

    assertTrue(compareImageModels(exLayer, expectedLayer));
    assertFalse(compareImageModels(actualLayer, expectedLayer));
  }

  @Test
  public void testCreateValidCheckerboard() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard 5 5 (255 0 0) (0 255 0)")
    );

    assertTrue(result);
    expectedLayer.createImage(new CheckerboardImageCreator(5, 5, Color.RED, Color.GREEN));

    assertTrue(compareImageModels(actualLayer, expectedLayer));
  }

  @Test
  public void testSharpenCreatedCheckerboard() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard 10 10 (255 0 0) (0 255 0)\n"),
        inputs("sharpen")
    );

    assertTrue(result);

    expectedLayer.createImage(
        new CheckerboardImageCreator(10, 10, Color.RED, Color.GREEN));
    expectedLayer.applyFilter(new SharpenFilter());

    assertTrue(compareImageModels(actualLayer, expectedLayer));
  }

  @Test
  public void testCreateValidCheckerboardWithComments() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard 7 7 (0 255 0) (0 0 255) #comment after command\n")
    );

    assertTrue(result);

    expectedLayer.createImage(
        new CheckerboardImageCreator(7, 7, Color.GREEN, Color.BLUE));

    assertTrue(compareImageModels(actualLayer, expectedLayer));
  }

  @Test
  public void testCreateCheckerboardInvalidArgs() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard\n"),
        prints("Error: command failed. Missing arguments.\n")
    );

    assertTrue(result);
  }

  @Test
  public void testCreateCheckerboardAdditionalArgs() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard 5 2 (255 0 0) (0 255 5) excess\n"),
        prints("Error: command failed. Additional arguments found.\n")
    );

    assertTrue(result);
  }

  @Test
  public void testCreateInvalidImage() {
    boolean result = testStart(
        actualLayer,
        inputs("create invalidimage\n"),
        prints("Error: command failed. Invalid type of image.\n")
    );

    assertTrue(result);
  }

  @Test
  public void testCreateCheckerboardNonIntegerArgs() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard not integer (255 0 0) (0 255 5)\n"),
        prints("Error: command failed. Non-integer argument found where integer was expected.\n"),
        inputs("create checkerboard word 1 (255 0 0) (0 255 5)\n"),
        prints("Error: command failed. Non-integer argument found where integer was expected.\n"),
        inputs("create checkerboard 2 word (255 0 0) (0 255 5)\n"),
        prints("Error: command failed. Non-integer argument found where integer was expected.\n")
    );

    // checks error messages loaded as expected, and command didn't go through
    assertTrue(result);
    assertEquals(actualLayer.numLayers(), 0);
  }

  @Test
  public void testInvalidCheckerboardNonIntegerRGBArgs() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard 5 5 (blue red green) (0 255 255)"),
        prints("Error: command failed. Non-integer RGB value found.\n")
    );

    assertTrue(result);
    assertEquals(actualLayer.numLayers(), 0);
  }

  @Test
  public void testCheckerboardInvalidRGBSet() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard 5 5 no set\n"),
        prints(
            "Error: command failed. Color RGB value "
                + "set was not found where it should have been.\n"),
        inputs("create checkerboard 5 5 set 1\n"),
        prints(
            "Error: command failed. Color RGB value "
                + "set was not found where it should have been.\n")
    );

    assertTrue(result);
    assertEquals(actualLayer.numLayers(), 0);
  }

  @Test
  public void testCreateCheckerboardRGBNotClosed() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard 5 5 (255 0 0) (0 255 255\n"),
        prints("Error: command failed. Color RGB value set was not properly completed.\n"),
        inputs("create checkerboard 5 5 (255 0 0 (25 0 0)\n"),
        prints("Error: command failed. Color RGB value set was not properly completed.\n")
    );

    assertTrue(result);
    assertEquals(actualLayer.numLayers(), 0);
  }

  @Test
  public void testCreateCheckerboardInvalidRGB() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard 5 5 (500 255 0) (255 255 255)\n"),
        prints("Error: command failed. Invalid RGB value.\n"),
        inputs("create checkerboard 5 5 (0 0 0) (255 0 500)\n"),
        prints("Error: command failed. Invalid RGB value.\n"),
        inputs("create checkerboard 5 5 (0 -2 0) (255 0 500)\n"),
        prints("Error: command failed. Invalid RGB value.\n")

    );

    assertTrue(result);
    assertEquals(actualLayer.numLayers(), 0);
  }

  @Test
  public void testCreateCheckerboardAfterErrors() {
    boolean result = testStart(
        actualLayer,
        inputs("create checkerboard 5 5 (500 255 0) (255 255 255)\n"),
        prints("Error: command failed. Invalid RGB value.\n"),
        inputs("create checkerboard 5 5 (255 0 0) (0 255 255\n"),
        prints("Error: command failed. Color RGB value set was not properly completed.\n"),
        inputs("create checkerboard 5 5 no set\n"),
        prints(
            "Error: command failed. Color RGB value "
                + "set was not found where it should have been.\n"),
        inputs("create checkerboard 5 5 (blue red green) (0 255 255)\n"),
        prints("Error: command failed. Non-integer RGB value found.\n"),
        inputs("create invalidimage\n"),
        prints("Error: command failed. Invalid type of image.\n"),
        inputs("create checkerboard 5 5 (255 0 0) (0 255 0)\n")
    );

    assertTrue(result);

    expectedLayer.createImage(new CheckerboardImageCreator(5, 5, Color.RED, Color.GREEN));

    assertTrue(compareImageModels(actualLayer, expectedLayer));
  }

  @Test
  public void testCreateMultipleCheckerboards() {
    boolean result = testStart(
        actualLayer,
        inputs("create invalidimage\n"),
        prints("Error: command failed. Invalid type of image.\n"),
        inputs("create checkerboard 5 5 (255 0 0) (0 255 0)\n"),
        inputs("create checkerboard 5 5 (0 255 0) (0 0 255)\n")
    );

    assertTrue(result);

    expectedLayer.createImage(new CheckerboardImageCreator(5, 5, Color.RED, Color.GREEN));
    expectedLayer.createImage(new CheckerboardImageCreator(5, 5, Color.GREEN, Color.BLUE));

    assertTrue(compareImageModels(expectedLayer, actualLayer));
  }

  @Test
  public void testSepiaOnImported() {
    new ImportImage("res/TestImage.ppm").execute(actualLayer);

    boolean result = testStart(
        actualLayer,
        inputs("sepia")
    );

    assertTrue(result);

    expectedLayer.storeImage(ImageUtil.readPPM("res/TestImage.ppm"));
    expectedLayer.applyFilter(new SepiaFilter());

    assertTrue(compareImageModels(expectedLayer, actualLayer));
  }

  @Test
  public void testGreyscaleAfterError() {
    new ImportImage("res/TestImage2.ppm").execute(actualLayer);

    boolean result = testStart(
        actualLayer,
        inputs("invalid command\n"),
        prints("Error: invalid command.\n"),
        inputs("sep@al3\n"),
        prints("Error: invalid command.\n"),
        inputs("grayscale\n")
    );

    assertTrue(result);

    expectedLayer.storeImage(ImageUtil.readPPM("res/TestImage2.ppm"));
    expectedLayer.applyFilter(new GreyscaleFilter());

    assertTrue(compareImageModels(actualLayer, expectedLayer));
  }

  @Test
  public void testSave() {
    // creates three layers, applies sepia to the third and toggles the second
    boolean result = testStart(
        actualLayer,
        inputs("import res/TestImage.ppm\n"),
        inputs("import res/TestImage.ppm\n"),
        inputs("import res/TestImage.ppm\n"),
        inputs("toggle 2\n"),
        inputs("select 3\n"),
        inputs("sepia\n"),
        inputs("save res/testSaved ppm")
    );

    assertTrue(result);

    // testing that all the layers are the same image
    LayeredImageModel actualSavedLayers = new LayeredImageModelImpl();
    LayeredImageModel expectedSavedLayers = new LayeredImageModelImpl();

    new ImportImage("res/testSaved/layer0").execute(actualSavedLayers);
    new ImportImage("res/TestImage.ppm").execute(expectedSavedLayers);
    assertTrue(compareImageModels(expectedSavedLayers, actualSavedLayers));

    // image should still be the same
    new ImportImage("res/testSaved/layer1").execute(actualSavedLayers);
    assertTrue(compareImageModels(expectedSavedLayers, actualSavedLayers));

    new ImportImage("res/testSaved/layer2").execute(actualSavedLayers);
    expectedSavedLayers.applyFilter(new SepiaFilter());
    assertTrue(compareImageModels(expectedSavedLayers, actualSavedLayers));

    // testing the properties text file
    try {
      Scanner scan = new Scanner(new File("res/testSaved/properties"));
      assertEquals(scan.nextLine(), "layer0 true");
      assertEquals(scan.nextLine(), "layer1 false");
      assertEquals(scan.nextLine(), "layer2 true");
    } catch (FileNotFoundException fnf) {
      fail();
    }
  }

  @Test
  public void testLoad() {
    // creates three layers, applies sepia to the third and toggles the second, and saves
    boolean result = testStart(
        actualLayer,
        inputs("import res/TestImage.ppm\n"),
        inputs("import res/TestImage.ppm\n"),
        inputs("import res/TestImage.ppm\n"),
        inputs("toggle 2\n"),
        inputs("select 3\n"),
        inputs("sepia\n"),
        inputs("save res/testSaved ppm")
    );

    assertTrue(result);

    // loads the aforementioned save file
    result = testStart(
        actualLayer,
        inputs("load res/testSaved")
    );

    assertTrue(result);

    assertTrue(actualLayer.isVisible(0));
    assertFalse(actualLayer.isVisible(1));
    assertTrue(actualLayer.isVisible(2));

    expectedLayer.storeImage(ImageUtil.readPPM("res/testImage.ppm"));

    actualLayer.selectCurrentLayer(0);
    assertTrue(compareImageModels(actualLayer, expectedLayer));

    actualLayer.selectCurrentLayer(1);
    assertTrue(compareImageModels(actualLayer, expectedLayer));

    actualLayer.selectCurrentLayer(2);
    expectedLayer.applyFilter(new SepiaFilter());
    assertTrue(compareImageModels(actualLayer, expectedLayer));
  }
}

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import cs3500.imageprocessing.controller.BlurImage;
import cs3500.imageprocessing.controller.CheckerboardImage;
import cs3500.imageprocessing.controller.CreateImage;
import cs3500.imageprocessing.controller.ExportImage;
import cs3500.imageprocessing.controller.GrayscaleImage;
import cs3500.imageprocessing.controller.ImportImage;
import cs3500.imageprocessing.controller.LoadImage;
import cs3500.imageprocessing.controller.RemoveLayer;
import cs3500.imageprocessing.controller.SaveImage;
import cs3500.imageprocessing.controller.SelectLayer;
import cs3500.imageprocessing.controller.SepiaImage;
import cs3500.imageprocessing.controller.SharpenImage;
import cs3500.imageprocessing.controller.ToggleLayer;
import cs3500.imageprocessing.model.CheckerboardImageCreator;
import cs3500.imageprocessing.model.LayeredImageModel;
import cs3500.imageprocessing.model.LayeredImageModelImpl;
import cs3500.imageprocessing.model.PixelImpl;
import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the public facing functionality of the classes that implement the {@code
 * ImageProcessingCommand} interface.
 */
public class ImageProcessingCommandTest {

  private StringBuilder log;
  private LayeredImageModel mock;
  private LayeredImageModel exModel;

  @Before
  public void setup() {
    log = new StringBuilder();
    mock = new MockLayeredImageModel(log);

    exModel = new LayeredImageModelImpl();
    File f = new File("res/ExportExample.ppm");
    f.delete();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImportImageNullFilename() {
    new ImportImage(null).execute(exModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImportImageInvalidFilename() {
    new ImportImage("res/FakeFile.ppm").execute(exModel);
  }

  @Test
  public void testImportImagePPM() {
    new ImportImage("res/TestImage.ppm").execute(exModel);
    assertEquals(new PixelImpl(255, 255, 255), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(255, 255, 255), exModel.getPixelAt(1, 1));
  }

  @Test
  public void testImportImagePNG() {
    new ImportImage("res/testPNG.png").execute(exModel);
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(1, 1));
  }

  @Test
  public void testImportImageJPG() {
    new ImportImage("res/testJPG.jpg").execute(exModel);
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(0, 0, 0), exModel.getPixelAt(1, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExportImageNullFilename() {
    exModel.createImage(new CheckerboardImageCreator(1, 2, Color.GREEN, Color.RED));

    new ExportImage(null, null).execute(exModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExportImageIllegalArgumentException() {
    exModel.createImage(new CheckerboardImageCreator(1, 2, Color.GREEN, Color.RED));
    new ExportImage("BadFile*", "png").execute(exModel);
  }

  @Test
  public void testExportImageValid() {
    exModel.createImage(new CheckerboardImageCreator(1, 2, Color.GREEN, Color.RED));
    LayeredImageModel newModel = new LayeredImageModelImpl();

    new ExportImage("res/ExportExample.jpg", "jpg").execute(exModel);
    new ImportImage("res/ExportExample.jpg").execute(newModel);
    assertEquals(new PixelImpl(156, 157, 30), newModel.getPixelAt(0, 0));
    assertEquals(new PixelImpl(105, 106, 0), newModel.getPixelAt(1, 0));
    assertEquals(new PixelImpl(106, 107, 0), newModel.getPixelAt(0, 1));
    assertEquals(new PixelImpl(150, 151, 24), newModel.getPixelAt(1, 1));
  }

  @Test
  public void testBlurImage() {
    new BlurImage().execute(mock);
    assertEquals("Filter: class cs3500.imageprocessing.filters.BlurFilter",
        log.toString());
  }

  @Test
  public void testSharpenImage() {
    new SharpenImage().execute(mock);
    assertEquals("Filter: class cs3500.imageprocessing.filters.SharpenFilter",
        log.toString());
  }

  @Test
  public void testGrayscaleImage() {
    new GrayscaleImage().execute(mock);
    assertEquals("Filter: class cs3500.imageprocessing.filters.GreyscaleFilter",
        log.toString());
  }

  @Test
  public void testSepiaImage() {
    new SepiaImage().execute(mock);
    assertEquals("Filter: class cs3500.imageprocessing.filters.SepiaFilter",
        log.toString());
  }

  @Test
  public void testSelectLayer() {
    new SelectLayer(2).execute(mock);
    assertEquals("select layerIndex: 1", log.toString());

    new SelectLayer(4).execute(mock);
    assertEquals("select layerIndex: 1select layerIndex: 3", log.toString());
  }

  @Test
  public void testToggleLayer() {
    new ToggleLayer(3).execute(mock);
    assertEquals("toggle layerIndex: 2", log.toString());

    new ToggleLayer(0).execute(mock);
    assertEquals("toggle layerIndex: 2toggle layerIndex: -1", log.toString());
  }

  @Test
  public void testRemoveLayer() {
    new RemoveLayer(5).execute(mock);
    assertEquals("remove layerIndex: 4", log.toString());

    new RemoveLayer(4).execute(mock);
    assertEquals("remove layerIndex: 4remove layerIndex: 3", log.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateImageNullParam() {
    new CreateImage(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateImageNoAddtionalInput() {
    new CreateImage("").execute(mock);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateImageInvalidInput() {
    new CreateImage("notCheckerboard").execute(mock);
  }

  @Test
  public void testCreateImageValidInput() {
    new CreateImage("checkerboard 2 1 (255 255 255) (0 0 0)").execute(mock);
    assertEquals("Creator: class cs3500.imageprocessing.model.CheckerboardImageCreator",
        log.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveImageNullFilename() {
    new SaveImage(null, "png");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveImageNullFormat() {
    new SaveImage("saveHere", null);
  }

  @Test
  public void testSaveImagePNG() {
    new CheckerboardImage("2 1 (255 0 0) (0 0 255)").execute(exModel);
    new CheckerboardImage("2 1 (0 255 0) (255 0 0)").execute(exModel);
    new ToggleLayer(1).execute(exModel);
    new SaveImage("res/saveHere", "png").execute(exModel);
    FileReader fr;
    try {
      fr = new FileReader("res/saveHere/properties");
      Scanner scan = new Scanner(fr);
      assertEquals("layer0falselayer1true", scan.next()
          + scan.next() + scan.next() + scan.next());
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void testSaveImageJPG() {
    new CheckerboardImage("2 1 (255 0 0) (0 0 255)").execute(exModel);
    new CheckerboardImage("2 1 (0 255 0) (255 0 0)").execute(exModel);
    new ToggleLayer(1).execute(exModel);
    new SaveImage("res/saveHere", "jpg").execute(exModel);
    FileReader fr;
    try {
      fr = new FileReader("res/saveHere/properties");
      Scanner scan = new Scanner(fr);
      assertEquals("layer0falselayer1true", scan.next()
          + scan.next() + scan.next() + scan.next());
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void testSaveImagePPM() {
    new CheckerboardImage("2 1 (255 0 0) (0 0 255)").execute(exModel);
    new CheckerboardImage("2 1 (0 255 0) (255 0 0)").execute(exModel);
    new ToggleLayer(1).execute(exModel);
    new SaveImage("res/saveHere", "ppm").execute(exModel);
    FileReader fr;
    try {
      fr = new FileReader("res/saveHere/properties");
      Scanner scan = new Scanner(fr);
      assertEquals("layer0falselayer1true", scan.next()
          + scan.next() + scan.next() + scan.next());
    } catch (IOException e) {
      fail();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImageNullFilename() {
    new LoadImage(null);
  }

  @Test
  public void testLoadImageValid() {
    new LoadImage("res/saveHere").execute(exModel);
    assertTrue(exModel.isVisible(1));
    assertFalse(exModel.isVisible(0));
  }
}

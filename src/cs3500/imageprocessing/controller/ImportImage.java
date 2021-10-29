package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.ImageUtil;
import cs3500.imageprocessing.model.LayeredImageModel;
import cs3500.imageprocessing.model.Pixel;
import cs3500.imageprocessing.model.PixelImpl;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Represents a command that imports an image to a given {@link LayeredImageModel}.
 */
public class ImportImage implements ImageProcessingCommand {

  private final String filename;

  /**
   * Constructs a new {@code ImportImage} object.
   *
   * @param filename the name of the file to import from
   * @throws IllegalArgumentException if the given filename is null
   */
  public ImportImage(String filename) throws IllegalArgumentException {
    if (filename == null) {
      throw new IllegalArgumentException("Filename cannot be null.");
    }
    this.filename = filename;
  }

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    List<List<Pixel>> finalImage = new ArrayList<>();

    try {
      finalImage = ImageUtil.readPPM(filename);
    } catch (IllegalArgumentException e) {
      try {
        InputStream input = new FileInputStream(filename);
        BufferedImage img = ImageIO.read(input);
        if (img == null) {
          throw new IllegalArgumentException("File is not a valid image.");
        }
        for (int i = 0; i < img.getHeight(); i++) {
          finalImage.add(new ArrayList<>());
          for (int j = 0; j < img.getWidth(); j++) {
            finalImage.get(i).add(new PixelImpl(new Color(img.getRGB(j, i))));
          }
        }
      } catch (FileNotFoundException fnf) {
        throw new IllegalArgumentException("Invalid file.");
      } catch (IOException io) {
        throw new IllegalArgumentException("There was an error reading from the file.");
      }
    }

    m.storeImage(finalImage);
  }
}

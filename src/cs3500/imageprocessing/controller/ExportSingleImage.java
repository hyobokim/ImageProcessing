package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.Channel;
import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.ImageUtil;
import cs3500.imageprocessing.model.Pixel;
import cs3500.imageprocessing.model.SingleImageModel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Represents a function to export the image of a {@link SingleImageModel} to a file.
 */
public class ExportSingleImage {

  /**
   * Exports an image stored as part of an {@link ImageModel} to a file. The name of the
   * file, the format to export to, and the model are given as arguments.
   *
   * @param m        the model whose image is being exported
   * @param filename the name of the file to export to
   * @param format   the format to export in
   * @throws IllegalArgumentException if any parameter is null, if the given image model contains a
   *                                  blank image, if an error occurs while writing to the file, or
   *                                  if the filetype is invalid
   */
  public void execute(ImageModel m, String filename, String format)
      throws IllegalArgumentException {
    if (m == null || filename == null || format == null) {
      throw new IllegalArgumentException("Model, filename, and format cannot be null.");
    }
    if (m.getWidth() == 0 || m.getHeight() == 0) {
      throw new IllegalArgumentException("Cannot export blank image.");
    }

    if (format.equals("ppm")) {
      ImageUtil.writePPM(m, filename);
      return;
    }

    BufferedImage img = new BufferedImage(
        m.getWidth(), m.getHeight(), BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < m.getHeight(); i++) {
      for (int j = 0; j < m.getWidth(); j++) {
        Pixel curr = m.getPixelAt(i, j);
        int intPixel = new Color(curr.getChannelValue(Channel.RED),
            curr.getChannelValue(Channel.GREEN),
            curr.getChannelValue(Channel.BLUE)).getRGB();
        img.setRGB(j, i, intPixel);
      }
    }

    try {
      if (!ImageIO.write(img, format, new File(filename))) {
        throw new IllegalArgumentException("Invalid filetype.");
      }
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid file.");
    } catch (IOException io) {
      throw new IllegalArgumentException("Error while writing to file.");
    }
  }
}

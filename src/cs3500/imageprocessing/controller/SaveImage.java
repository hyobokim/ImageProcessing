package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.LayeredImageModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents a command that saves a multi-layered-image to a file from a given {@link
 * LayeredImageModel}. Saves data including images, their visibility, and order.
 */
public class SaveImage implements ImageProcessingCommand {

  private final String filename;
  private final String format;

  /**
   * Constructs a new {@code SaveImage} object.
   *
   * @param filename the filename to save to
   * @param format the format the images are to be stored in
   * @throws IllegalArgumentException if either parameter is null
   */
  public SaveImage(String filename, String format) throws IllegalArgumentException {
    if (filename == null || format == null) {
      throw new IllegalArgumentException("Filename and format cannot be null.");
    }
    this.filename = filename;
    this.format = format;
  }

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    File folder = new File(filename);
    folder.mkdirs();

    StringBuilder contents = new StringBuilder();
    int i = 0;
    for (ImageModel layer : m) {
      contents.append("layer").append(i).append(" ").append(m.isVisible(i)).append("\n");
      new ExportSingleImage().execute(layer, filename + "/layer" + i, format);
      i++;
    }

    new File(filename + "/properties");
    try {
      FileWriter fr = new FileWriter(filename + "/properties");
      fr.write(contents.toString());
      fr.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Error writing to file.");
    }
  }
}

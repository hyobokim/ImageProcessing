package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.LayeredImageModel;

/**
 * Represents a command that exports the image of a given {@link LayeredImageModel} to a file.
 */
public class ExportImage implements ImageProcessingCommand {

  private final String filename;
  private final String format;

  /**
   * Constructs a new {@code ExportImage} object.
   *
   * @param filename the name of the file to export to
   * @param format   the format to export in
   * @throws IllegalArgumentException if either parameter is null
   */
  public ExportImage(String filename, String format) throws IllegalArgumentException {
    if (filename == null
        || format == null) {
      throw new IllegalArgumentException("Filename and Format type must not be null");
    }

    this.filename = filename;
    this.format = format;
  }

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    ImageModel topLayer;
    try {
      topLayer = m.getTopmostVisibleLayer();
    } catch (IllegalCallerException e) {
      throw new IllegalArgumentException("Cannot export with no visible layers.");
    }
    new ExportSingleImage().execute(topLayer, filename, format);
  }
}

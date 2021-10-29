package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.LayeredImageModel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a command to load a saved multi-layer-image into a given {@link LayeredImageModel}.
 */
public class LoadImage implements ImageProcessingCommand {

  private final String filename;

  /**
   * Constructs a new {@code LoadImage} object.
   *
   * @param filename the name of the file to load from
   * @throws IllegalArgumentException if the given filename is null
   */
  public LoadImage(String filename) throws IllegalArgumentException {
    if (filename == null) {
      throw new IllegalArgumentException("Filename cannot be null.");
    }

    this.filename = filename;
  }

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    Scanner scan;
    try {
      scan = new Scanner(new FileInputStream(filename + "/properties"));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid file.");
    }

    int numLayers = m.numLayers();
    for (int i = 0; i < numLayers; i++) {
      m.removeLayer(0);
    }

    while (scan.hasNext()) {
      new ImportImage(filename + "/" + scan.next()).execute(m);
      if (!scan.nextBoolean()) {
        m.toggleLayer(m.numLayers() - 1);
      }
    }
  }
}

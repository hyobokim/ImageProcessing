package cs3500.imageprocessing.controller;

/**
 * Represents a controller for an image processing application. Provides functionality to start the
 * application which can then be used to load, manipulate, save, export, etc. images.
 */
public interface ImageController {

  /**
   * Starts the image processing application. Reads input to execute commands to manipulate images.
   *
   * @throws IllegalStateException if an error occurs while writing to output
   */
  public void start() throws IllegalStateException;
}

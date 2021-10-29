package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.LayeredImageModel;

/**
 * Represents a command for image processing. Includes the functionality to execute the command on a
 * given model.
 */
public interface ImageProcessingCommand {

  /**
   * Executes a command on a given model.
   *
   * @param m the model on which to execute the command
   * @throws IllegalArgumentException if the given model is null, or if any problems arise while
   *                                  executing the command
   */
  public void execute(LayeredImageModel m) throws IllegalArgumentException;
}

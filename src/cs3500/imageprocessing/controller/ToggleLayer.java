package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.LayeredImageModel;

/**
 * Represents a command that toggles the visibility of a given layer in a given {@link
 * LayeredImageModel}.
 */
public class ToggleLayer implements ImageProcessingCommand {

  private final int layerIndex;

  /**
   * Constructs a new {@code ToggleLayer} object.
   *
   * @param layerIndex the index of the layer whose visibility is to be toggled
   */
  public ToggleLayer(int layerIndex) {
    this.layerIndex = layerIndex - 1;
  }

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    m.toggleLayer(layerIndex);
  }
}

package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.LayeredImageModel;

/**
 * Represents a command that selects a layer of a given {@link LayeredImageModel} and sets it
 * as the current layer.
 */
public class SelectLayer implements ImageProcessingCommand {

  private final int layerIndex;

  /**
   * Constructs a new {@code SelectLayer} object.
   *
   * @param layerIndex the index of the layer to be selected.
   */
  public SelectLayer(int layerIndex) {
    this.layerIndex = layerIndex - 1;
  }

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    m.selectCurrentLayer(layerIndex);
  }
}

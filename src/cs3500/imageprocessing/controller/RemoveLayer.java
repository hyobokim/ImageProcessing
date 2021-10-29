package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.LayeredImageModel;

/**
 * Represents a command to remove a layer from a given {@link LayeredImageModel}.
 */
public class RemoveLayer implements ImageProcessingCommand {

  private final int layerIndex;

  /**
   * Constructs a new {@code RemoveLayer} object.
   *
   * @param layerIndex the index of the layer to be removed
   */
  public RemoveLayer(int layerIndex) {
    this.layerIndex = layerIndex - 1;
  }

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    m.removeLayer(layerIndex);
  }
}

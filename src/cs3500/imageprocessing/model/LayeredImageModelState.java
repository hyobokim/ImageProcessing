package cs3500.imageprocessing.model;

/**
 * Represents a view-only LayeredImageModel interface. Handles methods that allow for querying
 * the state of the LayeredImageModel, such as the number of layers or the visibility of specific
 * layers.
 */
public interface LayeredImageModelState extends ImageModelState {

  /**
   * Produces the topmost visible layer in the model.
   *
   * @return the topmost visible layer
   * @throws IllegalCallerException if there are no visible layers
   */
  public ImageModel getTopmostVisibleLayer() throws IllegalCallerException;

  /**
   * Determines whether the layer at the given index is visible or not.
   *
   * @param layerIndex the index of the layer to check the visibility of
   * @return true if the layer is visible, false if not
   * @throws IllegalArgumentException if no layer exists at the given index
   */
  public boolean isVisible(int layerIndex) throws IllegalArgumentException;

  /**
   * Produces the number of layers in the model.
   *
   * @return the number of layers in the model
   */
  public int numLayers();

  /**
   * Produces the index of the current layer in the model.
   *
   * @return the index of the current layer
   */
  public int getCurrentLayerIndex();
}

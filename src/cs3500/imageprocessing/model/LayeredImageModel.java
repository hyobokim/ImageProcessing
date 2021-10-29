package cs3500.imageprocessing.model;

/**
 * Represents the model for an image processing application that supports layered images. Implements
 * all of the functionality of an {@link ImageModel}, allowing regular image modification an
 * querying operations to be performed while also providing the necessary operations to use select,
 * remove, and modify the visibility of layers. Iterable over layers which are represented as
 * ImageModels.
 */
public interface LayeredImageModel extends ImageModel, LayeredImageModelState,
    Iterable<ImageModel> {

  /**
   * Selects the layer at the given index as current.
   *
   * @param layerIndex the index of the layer to select
   * @throws IllegalArgumentException if no layer exists at the given index
   */
  public void selectCurrentLayer(int layerIndex) throws IllegalArgumentException;

  /**
   * Removes the layer at the given index.
   *
   * @param layerIndex the index of the layer to remove
   * @throws IllegalArgumentException if no layer exists at the given index
   */
  public void removeLayer(int layerIndex) throws IllegalArgumentException;

  /**
   * Toggles the visibility of the layer at the given index.
   *
   * @param layerIndex the index of the layer to toggle visibility
   * @throws IllegalArgumentException if no layer exists at the given index
   */
  public void toggleLayer(int layerIndex) throws IllegalArgumentException;
}

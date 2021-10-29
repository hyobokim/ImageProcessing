package cs3500.imageprocessing.model;

import cs3500.imageprocessing.filters.ImageFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Represents a model for an image processing application that supports layered images. Provides all
 * of the functionality of the {@link LayeredImageModel} interface to support operations to load
 * images as layers and query and modify them.
 */
public class LayeredImageModelImpl implements LayeredImageModel {

  /*
   * Class invariant: each layer will always have the same height and width.
   */
  private final List<ImageModel> layers;
  private ImageModel currentLayer;
  private final Map<ImageModel, Boolean> isVisible;

  /**
   * Constructs a new {@code LayeredImageModelImpl} object with no images.
   */
  public LayeredImageModelImpl() {
    this.layers = new ArrayList<>();
    this.currentLayer = null;
    this.isVisible = new HashMap<>();
  }

  @Override
  public void applyKernel(List<List<Double>> kernel, Channel channel)
      throws IllegalArgumentException {
    if (currentLayer != null) {
      currentLayer.applyKernel(kernel, channel);
    }
  }

  @Override
  public void applyLinearTransformation(List<List<Double>> matrix) throws IllegalArgumentException {
    if (currentLayer != null) {
      currentLayer.applyLinearTransformation(matrix);
    }
  }

  @Override
  public void createImage(ImageCreator creator) throws IllegalArgumentException {
    ImageModel newLayer = new SingleImageModel();
    newLayer.createImage(creator);
    validSize(newLayer);

    currentLayer = newLayer;
    layers.add(newLayer);
    isVisible.put(newLayer, true);
  }

  @Override
  public void applyFilter(ImageFilter filter) throws IllegalArgumentException {
    if (currentLayer != null) {
      currentLayer.applyFilter(filter);
    }
  }

  @Override
  public void storeImage(List<List<Pixel>> image) throws IllegalArgumentException {
    ImageModel newLayer = new SingleImageModel();
    newLayer.storeImage(image);
    validSize(newLayer);

    currentLayer = newLayer;
    layers.add(newLayer);
    isVisible.put(newLayer, true);
  }

  @Override
  public Pixel getPixelAt(int x, int y) throws IllegalArgumentException {
    if (currentLayer != null) {
      return currentLayer.getPixelAt(x, y);
    } else {
      throw new IllegalArgumentException("No layer to get pixels from.");
    }
  }

  @Override
  public int getHeight() {
    if (currentLayer != null) {
      return currentLayer.getHeight();
    }

    return 0;
  }

  @Override
  public int getWidth() {
    if (currentLayer != null) {
      return currentLayer.getWidth();
    }

    return 0;
  }

  @Override
  public void selectCurrentLayer(int layerIndex) throws IllegalArgumentException {
    validLayerIndex(layerIndex);

    currentLayer = layers.get(layerIndex);
  }

  @Override
  public void removeLayer(int layerIndex) throws IllegalArgumentException {
    validLayerIndex(layerIndex);

    // checks if the user removed the current layer
    boolean changeCurrent = (layers.get(layerIndex) == currentLayer);

    isVisible.remove(layers.remove(layerIndex));
    if (layers.size() == 0) {
      currentLayer = null;
    } else if (changeCurrent && layerIndex == layers.size()) {
      currentLayer = layers.get(layerIndex - 1);
    } else if (changeCurrent) {
      currentLayer = layers.get(layerIndex);
    }
  }

  @Override
  public void toggleLayer(int layerIndex) throws IllegalArgumentException {
    validLayerIndex(layerIndex);

    ImageModel layer = layers.get(layerIndex);
    isVisible.put(layer, !isVisible.get(layer));
  }

  @Override
  public ImageModel getTopmostVisibleLayer() throws IllegalCallerException {
    for (int i = layers.size() - 1; i >= 0; i--) {
      if (isVisible.get(layers.get(i))) {
        return layers.get(i);
      }
    }
    throw new IllegalCallerException("There are no visible layers.");
  }

  @Override
  public boolean isVisible(int layerIndex) throws IllegalArgumentException {
    validLayerIndex(layerIndex);

    return isVisible.get(layers.get(layerIndex));
  }

  @Override
  public int numLayers() {
    return layers.size();
  }

  @Override
  public int getCurrentLayerIndex() {
    for (int i = 0; i < layers.size(); i++) {
      if (layers.get(i) == currentLayer) {
        return i;
      }
    }
    throw new IllegalStateException("No current layer.");
  }

  /**
   * Checks if a given index is a valid layer index in the context of this layered image model.
   *
   * @param index the index to checker
   * @throws IllegalArgumentException if the index is not valid
   */
  private void validLayerIndex(int index) throws IllegalArgumentException {
    if (index >= layers.size() || index < 0) {
      throw new IllegalArgumentException("Invalid index.");
    }
  }

  /**
   * Checks if a given {@code ImageModel} has a valid size as the layers model. If
   * there are no layers in this model, the size is always valid. If there is at least one
   * layer, the size of the given {@code ImageModel} must match the layer's size.
   *
   * @param model the image model to check size validity
   * @throws IllegalArgumentException if the size is not valid
   */
  private void validSize(ImageModel model) throws IllegalArgumentException {
    if (currentLayer == null) {
      return;
    }
    if (getHeight() != model.getHeight() || getWidth() != model.getWidth()) {
      throw new IllegalArgumentException("Size of image does not match other layers.");
    }
  }

  @Override
  public Iterator<ImageModel> iterator() {
    return new LayeredImageIterator();
  }

  /**
   * Represents an {@link Iterator} over {@link ImageModel} that iterates over the layers in a
   * {@code LayeredImageModelImpl}.
   */
  public class LayeredImageIterator implements Iterator<ImageModel> {

    private int index = 0;

    @Override
    public boolean hasNext() {
      return index < layers.size();
    }

    @Override
    public ImageModel next() {
      return layers.get(index++);
    }
  }
}

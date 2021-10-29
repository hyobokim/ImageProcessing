import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.LayeredImageModel;
import java.util.Iterator;

/**
 * Mock class to log the arguments passed to a LayeredImageModel's methods.
 */
public class MockLayeredImageModel extends MockImageModel implements LayeredImageModel {

  /**
   * Constructs a new {@code MockLayeredImageModel} object.
   *
   * @param log the object to log method calls to.
   */
  public MockLayeredImageModel(StringBuilder log) {
    super(log);
  }

  @Override
  public void selectCurrentLayer(int layerIndex) throws IllegalArgumentException {
    log.append("select layerIndex: ").append(layerIndex);
  }

  @Override
  public void removeLayer(int layerIndex) throws IllegalArgumentException {
    log.append("remove layerIndex: ").append(layerIndex);
  }

  @Override
  public void toggleLayer(int layerIndex) throws IllegalArgumentException {
    log.append("toggle layerIndex: ").append(layerIndex);
  }

  @Override
  public ImageModel getTopmostVisibleLayer() throws IllegalCallerException {
    throw new IllegalCallerException("Should not be called.");
  }

  @Override
  public boolean isVisible(int layerIndex) throws IllegalArgumentException {
    log.append("isVisible layerIndex: ").append(layerIndex);
    return true;
  }

  @Override
  public int numLayers() {
    throw new IllegalCallerException("Should not be called.");
  }

  @Override
  public int getCurrentLayerIndex() {
    throw new IllegalCallerException("Should not be called");
  }

  @Override
  public Iterator<ImageModel> iterator() {
    throw new IllegalCallerException("Should not be called.");
  }
}

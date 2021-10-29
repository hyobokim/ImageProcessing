package cs3500.imageprocessing.view;

import java.awt.Component;

/**
 * Represents the view for an image processing application to be displayed
 * with Swing. Provides functionality of an {@link ImageView} with added support
 * for refreshing the currently displayed image by querying the state of the model.
 */
public interface SwingImageView extends ImageView {

  /**
   * Refreshes the image that is being displayed to be the current state of the model.
   */
  public void refresh();

  /**
   * Produces the component associated with this view.
   *
   * @return the component of this view
   */
  public Component getComponent();
}

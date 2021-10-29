package cs3500.imageprocessing.view;

import java.io.IOException;

/**
 * Represents a view for an image processing application. Provides functionality
 * to render messages to some location for output.
 */
public interface ImageView {

  /**
   * Renders a given message to the output destination of the view.
   *
   * @param message the message to be rendered
   * @throws IllegalArgumentException if the given message is null
   * @throws IOException if the output fails
   */
  public void renderMessage(String message) throws IllegalArgumentException, IOException;
}

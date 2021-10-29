package cs3500.imageprocessing.view;

import java.io.IOException;

/**
 * Represents a textual view for an image processing application. Provides
 * an implementation of the {@link ImageView} interface.
 */
public class TextImageView implements ImageView {

  private final Appendable output;

  /**
   * Constructs a new {@code TextImageView} object.
   *
   * @param output the output destination of this view
   * @throws IllegalArgumentException if the given output destination is null
   */
  public TextImageView(Appendable output) throws IllegalArgumentException {
    if (output == null) {
      throw new IllegalArgumentException("Output destination cannot be null.");
    }

    this.output = output;
  }

  @Override
  public void renderMessage(String message) throws IllegalArgumentException, IOException {
    if (message == null) {
      throw new IllegalArgumentException("Message cannot be null.");
    }

    output.append(message);
  }
}

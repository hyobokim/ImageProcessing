package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.LayeredImageModel;
import cs3500.imageprocessing.view.ImageView;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Represents a text-based controller for an image processing application. Provides the
 * functionality specified by {@link ImageController} to manipulate layered images.
 */
public class SimpleImageController implements ImageController {

  private final ImageView view;
  private final Scanner scanLine;
  private final LayeredImageModel model;

  /**
   * Constructs a new {@code SimpleImageController} object.
   *
   * @param input the source of input for the controller
   * @param view  the view that the controller will use
   * @param model the model to be used by the controller
   * @throws IllegalArgumentException if any parameter is null
   */
  public SimpleImageController(Readable input, ImageView view, LayeredImageModel model)
      throws IllegalArgumentException {
    if (input == null || view == null || model == null) {
      throw new IllegalArgumentException("Input, view, and model cannot be null.");
    }

    this.view = view;
    this.scanLine = new Scanner(input);
    this.model = model;
  }

  @Override
  public void start() throws IllegalStateException {
    Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands;

    knownCommands = new HashMap<>();
    knownCommands.put("import", s -> new ImportImage(s.next()));
    knownCommands.put("export", s -> new ExportImage(s.next(), s.next()));
    knownCommands.put("select", s -> new SelectLayer(s.nextInt()));
    knownCommands.put("remove", s -> new RemoveLayer(s.nextInt()));
    knownCommands.put("toggle", s -> new ToggleLayer(s.nextInt()));
    knownCommands.put("blur", s -> new BlurImage());
    knownCommands.put("sharpen", s -> new SharpenImage());
    knownCommands.put("sepia", s -> new SepiaImage());
    knownCommands.put("grayscale", s -> new GrayscaleImage());
    knownCommands.put("load", s -> new LoadImage(s.next()));
    knownCommands.put("save", s -> new SaveImage(s.next(), s.next()));
    knownCommands.put("create", s -> new CreateImage(s.nextLine()));
    knownCommands.put("resize", s -> new ResizeLayer(s.nextInt(), s.nextInt()));
    knownCommands.put("mosaic", s -> new MosaicImage(s.nextInt()));

    while (scanLine.hasNext()) {
      ImageProcessingCommand c;
      Scanner scan = new Scanner(scanLine.nextLine());
      if (!scan.hasNext()) {
        continue;
      }
      String command = scan.next();
      if (command.equals("quit")) {
        break;
      }
      Function<Scanner, ImageProcessingCommand> function =
          knownCommands.getOrDefault(command, null);
      if (function != null) {
        try {
          c = function.apply(scan);
        } catch (NoSuchElementException e) {
          renderMessage("Error: command not followed by proper arguments.\n");
          continue;
        }
        if (scan.hasNext()) {
          if (scan.next().charAt(0) != '#') {
            renderMessage("Error: additional arguments found.\n");
            continue;
          }
        }
        try {
          c.execute(model);
        } catch (IllegalArgumentException iae) {
          renderMessage("Error: command failed. " + iae.getMessage() + "\n");
        }
      } else {
        renderMessage("Error: invalid command.\n");
      }
    }
  }

  /**
   * Renders a message through the view, handling potential IOExceptions.
   *
   * @param message the message to be rendered
   * @throws IllegalStateException if something goes wrong during the output
   */
  private void renderMessage(String message) throws IllegalStateException {
    try {
      view.renderMessage(message);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to render message.");
    }
  }
}

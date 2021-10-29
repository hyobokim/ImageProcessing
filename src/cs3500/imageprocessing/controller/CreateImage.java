package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.LayeredImageModel;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Represents a command that creates some image for a given {@link LayeredImageModel}.
 */
public class CreateImage implements ImageProcessingCommand {

  private final String line;

  /**
   * Constructs a new {@code CreateImage} object.
   *
   * @param line the arguments for this command as a line
   * @throws IllegalArgumentException if the given line is null
   */
  public CreateImage(String line) throws IllegalArgumentException {
    if (line == null) {
      throw new IllegalArgumentException("Line cannot be null.");
    }
    this.line = line;
  }

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands;

    knownCommands = new HashMap<>();
    knownCommands.put("checkerboard", s -> new CheckerboardImage(s.nextLine()));

    Scanner scan = new Scanner(line);
    ImageProcessingCommand c;
    if (!scan.hasNext()) {
      throw new IllegalArgumentException("Missing arguments.");
    }
    Function<Scanner, ImageProcessingCommand> function =
        knownCommands.getOrDefault(scan.next(), null);

    if (function != null) {
      try {
        c = function.apply(scan);
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException("Missing arguments.");
      }
    } else {
      throw new IllegalArgumentException("Invalid type of image.");
    }
    c.execute(m);
  }
}

package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.CheckerboardImageCreator;
import cs3500.imageprocessing.model.LayeredImageModel;
import java.awt.Color;
import java.util.Scanner;

/**
 * Represents a command that constructs a checkerboard image for a given {@link LayeredImageModel}.
 */
public class CheckerboardImage implements ImageProcessingCommand {

  private final String line;

  /**
   * Constructs a new {@code CheckerboardImage} object.
   *
   * @param line the arguments for the creation of this checkerboard as a line
   * @throws IllegalArgumentException if the given line is null
   */
  public CheckerboardImage(String line) throws IllegalArgumentException {
    if (line == null) {
      throw new IllegalArgumentException("Line cannot be null.");
    }

    this.line = line;
  }

  @Override
  public void execute(LayeredImageModel m) throws IllegalArgumentException {
    Scanner scan = new Scanner(line);
    int location = 0;
    int[] params = new int[8];

    while (scan.hasNext()) {
      String input = scan.next();
      if (location == 2 || location == 5) {
        if (input.charAt(0) != '(') {
          throw new IllegalArgumentException(
              "Color RGB value set was not found where it should have been.");
        }
        try {
          params[location] = Integer.parseInt(input.substring(1));
        } catch (NumberFormatException nfe) {
          throw new IllegalArgumentException(
              "Non-integer RGB value found.");
        }
      } else if (location == 4 || location == 7) {
        if (input.charAt(input.length() - 1) != ')') {
          throw new IllegalArgumentException(
              "Color RGB value set was not properly completed.");
        }
        try {
          params[location] = Integer.parseInt(input.substring(0, input.length() - 1));
        } catch (NumberFormatException nfe) {
          throw new IllegalArgumentException(
              "Non-integer RGB value found.");
        }
      } else if (location <= 7) {
        try {
          params[location] = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
          throw new IllegalArgumentException(
              "Non-integer argument found where integer was expected.");
        }
      } else {
        if (input.charAt(0) != '#') {
          throw new IllegalArgumentException("Additional arguments found.");
        }
        break;
      }
      location++;
    }

    if (location != 8) {
      throw new IllegalArgumentException("Missing arguments.");
    }

    Color color1;
    Color color2;

    try {
      color1 = new Color(params[2], params[3], params[4]);
      color2 = new Color(params[5], params[6], params[7]);
    } catch (IllegalArgumentException iae) {
      throw new IllegalArgumentException("Invalid RGB value.");
    }

    m.createImage(new CheckerboardImageCreator(params[0], params[1], color1, color2));
  }
}

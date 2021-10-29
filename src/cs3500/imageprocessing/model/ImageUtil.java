package cs3500.imageprocessing.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;

/**
 * This class contains utility methods to read a PPM image from file and generate a list of list of
 * pixels to represent the image.
 */
public class ImageUtil {

  /**
   * Read an image file in the PPM format and store it as a list of list of pixels.
   *
   * @param filename the path of the file
   * @throws IllegalArgumentException if the file is not found or is null
   */
  public static List<List<Pixel>> readPPM(String filename) throws IllegalArgumentException {
    Scanner sc;

    if (filename == null) {
      throw new IllegalArgumentException("Filename cannot be null.");
    }

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found!");
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.length() != 0 && s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException(
          "Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    sc.nextInt();

    List<List<Pixel>> image = new ArrayList<>();

    for (int i = 0; i < height; i++) {
      image.add(new ArrayList<>());
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        image.get(i).add(new PixelImpl(r, g, b));
      }
    }

    return image;
  }

  /**
   * Writes an image to a ppm file.
   *
   * @param m        the image model containing the image to write
   * @param filename the name of the file to write to
   * @throws IllegalArgumentException if the filename or model are null, or if an error occurs while
   *                                  writing to the file
   */
  public static void writePPM(ImageModel m, String filename) throws IllegalArgumentException {
    if (m == null || filename == null) {
      throw new IllegalArgumentException("Filename cannot be null.");
    }

    StringBuilder builder = new StringBuilder();
    builder.append("P3\n");
    builder.append(m.getWidth()).append(" ").append(m.getHeight()).append("\n255\n");

    for (int i = 0; i < m.getHeight(); i++) {
      for (int j = 0; j < m.getWidth(); j++) {
        builder.append(m.getPixelAt(i, j)).append("\n");
      }
    }
    File f = new File(filename);
    try {
      FileWriter fr = new FileWriter(filename);
      fr.write(builder.toString());
      fr.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Error occured while writing to file.");
    }
  }
}


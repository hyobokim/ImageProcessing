import cs3500.imageprocessing.controller.SimpleImageController;
import cs3500.imageprocessing.controller.SwingController;
import cs3500.imageprocessing.model.LayeredImageModel;
import cs3500.imageprocessing.model.LayeredImageModelImpl;
import cs3500.imageprocessing.view.TextImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/**
 * Main class to drive the program. Provides the main method that runs the image processing
 * controller to allow the user to interact with the application.
 */
public class Main {

  /**
   * Main method to drive the program. Sets up and starts a controller based on the arguments given
   * at runtime. Options are -script followed by a filename to run a script, -text to interactively
   * run commands through the console, and -interactive to open the Swing GUI.
   *
   * @param args the arguments to be read
   * @throws IllegalArgumentException if the arguments do not match what is expected
   */
  public static void main(String[] args) throws IllegalArgumentException {

    if (args == null || args.length == 0) {
      throw new IllegalArgumentException("Missing arguments.");
    }

    LayeredImageModel model = new LayeredImageModelImpl();
    Readable userInput;
    int numUsedArgs;

    switch (args[0]) {
      case "-script":
        if (args.length == 1) {
          throw new IllegalArgumentException("Missing filename for script.");
        }
        try {
          userInput = new InputStreamReader(new FileInputStream(args[1]));
        } catch (FileNotFoundException fnf) {
          throw new IllegalArgumentException("Invalid file.");
        }
        numUsedArgs = 2;
        break;
      case "-text":
        userInput = new InputStreamReader(System.in);
        numUsedArgs = 1;
        break;
      case "-interactive":
        new SwingController(model).start();
        numUsedArgs = 1;
        if (numUsedArgs != args.length) {
          throw new IllegalArgumentException("Extra arguments found.");
        }
        return;
      default:
        throw new IllegalArgumentException("Invalid arguments.");
    }
    if (numUsedArgs != args.length) {
      throw new IllegalArgumentException("Extra arguments found.");
    }

    new SimpleImageController(userInput, new TextImageView(System.out), model).start();
  }
}

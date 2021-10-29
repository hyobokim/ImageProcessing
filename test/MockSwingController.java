import cs3500.imageprocessing.controller.SwingImageController;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.JCheckBox;

/**
 * Mock swing controller to log inputs to the actionPerformed and itemStateChanged methods.
 */
public class MockSwingController implements SwingImageController {

  private final StringBuilder log;

  /**
   * Constructs a new {@code MockSwingController} object with the given log.
   *
   * @param log the object to log method calls to
   * @throws IllegalArgumentException if the given log is null
   */
  public MockSwingController(StringBuilder log) throws IllegalArgumentException {
    if (log == null) {
      throw new IllegalArgumentException("Log cannot be null.");
    }
    this.log = log;
  }

  @Override
  public void start() throws IllegalStateException {
    throw new IllegalCallerException("This method should not be called.");
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    log.append(e.getActionCommand());
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    log.append(((JCheckBox) e.getItemSelectable()).getActionCommand());
  }
}

package cs3500.imageprocessing.controller;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

/**
 * Represents a controller for an image processing application. Provides functionality to control an
 * application that uses a Swing GUI.
 */
public interface SwingImageController extends ImageController, ActionListener,
    ItemListener {
  // no additional methods needed
}

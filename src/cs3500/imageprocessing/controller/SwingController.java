package cs3500.imageprocessing.controller;

import cs3500.imageprocessing.model.LayeredImageModel;
import cs3500.imageprocessing.view.SwingImageView;
import cs3500.imageprocessing.view.SwingImageViewImpl;
import cs3500.imageprocessing.view.TextImageView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Represents a GUI-based controller designed specifically for interaction with,
 * which uses Java's Swing library to generate the view and display output.
 */
public class SwingController implements SwingImageController {

  SwingImageView view;
  LayeredImageModel model;

  /**
   * Constructs a new {@Code SwingController} object.
   *
   * @param model the model to be used by the controller
   */
  public SwingController(LayeredImageModel model) {
    this.model = model;
    this.view = new SwingImageViewImpl(model, this);
  }

  @Override
  public void start() throws IllegalStateException {
    view.getComponent().setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command;
    String layerIndex;
    String directory;
    String filetype;
    switch (e.getActionCommand()) {
      case "resize":
        String width = JOptionPane.showInputDialog("Enter new width");
        String height = JOptionPane.showInputDialog("Enter new height");

        command = "resize " + width + " " + height;
        break;
      case "checkerboard":
        String numTiles = JOptionPane.showInputDialog("Enter number of tiles");
        if (numTiles == null) {
          return;
        }
        String tileSize = JOptionPane.showInputDialog("Enter size of each tile");
        if (tileSize == null) {
          return;
        }
        Color col = JColorChooser
            .showDialog(view.getComponent(), "Choose a color for the first tile", Color.WHITE);
        if (col == null) {
          return;
        }
        Color col2 = JColorChooser
            .showDialog(view.getComponent(), "Choose a color for the second tile", Color.WHITE);
        command =
            "create checkerboard " + tileSize + " " + numTiles
                + " " + "(" + col.getRed() + " " + col.getGreen() + " " + col.getBlue() + ")"
                + " " + "(" + col2.getRed() + " " + col2.getGreen() + " " + col2.getBlue() + ")";
        break;
      case "import":
        JFileChooser fchooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JPG, PPM, PNG Images", "jpg", "png", "ppm");
        fchooser.setFileFilter(filter);
        int retvalue = fchooser.showOpenDialog(view.getComponent());
        if (retvalue == JFileChooser.APPROVE_OPTION) {
          File f = fchooser.getSelectedFile();
          command = "import " + f.getAbsolutePath();
        } else {
          return;
        }
        break;
      case "mosaic":
        layerIndex = JOptionPane.showInputDialog("Enter number of seeds for mosaic");
        command = "mosaic " + layerIndex;
        break;
      case "remove":
        layerIndex = JOptionPane.showInputDialog("Enter number of layer to remove");
        command = "remove " + layerIndex;
        break;
      case "toggle":
        layerIndex = JOptionPane.showInputDialog("Enter number of layer to toggle");
        command = "toggle " + layerIndex;
        break;
      case "select":
        layerIndex = JOptionPane.showInputDialog("Enter number of layer to select");
        command = "select " + layerIndex;
        break;
      case "save":
        directory = saveFileDirectory();
        if (directory == null) {
          return;
        }
        filetype = JOptionPane.showInputDialog("Enter the file format to use for the save");
        command = "save " + directory + " " + filetype;
        break;
      case "export":
        directory = saveFileDirectory();
        if (directory == null) {
          return;
        }
        filetype = JOptionPane.showInputDialog("Enter the file format to export to");
        command = "export " + directory + " " + filetype;
        break;
      case "load":
        fchooser = new JFileChooser(".");
        fchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        retvalue = fchooser.showOpenDialog(view.getComponent());
        if (retvalue == JFileChooser.APPROVE_OPTION) {
          File f = fchooser.getSelectedFile();
          command = "load " + f.getAbsolutePath();
        } else {
          return;
        }
        break;
      case "script":
        fchooser = new JFileChooser(".");
        filter = new FileNameExtensionFilter(
            "TXT file", "txt");
        fchooser.setFileFilter(filter);
        retvalue = fchooser.showOpenDialog(view.getComponent());
        if (retvalue == JFileChooser.APPROVE_OPTION) {
          File f = fchooser.getSelectedFile();
          command = f.getAbsolutePath();
        } else {
          return;
        }
        ImageController scriptController;
        StringBuilder output;
        try {
          output = new StringBuilder();
          scriptController = new SimpleImageController(
              new InputStreamReader(new FileInputStream(command)),
              new TextImageView(output),
              model);
        } catch (FileNotFoundException ex) {
          try {
            view.renderMessage("File not found.");
            return;
          } catch (IOException io) {
            throw new IllegalStateException("Error occurred while trying to render message.");
          }
        }
        scriptController.start();
        String errors = output.toString();
        if (!errors.equals("")) {
          try {
            view.renderMessage(errors);
          } catch (IOException ioException) {
            throw new IllegalStateException("Error occurred while trying to render message.");
          }
        }
        view.refresh();
        return;
      default:
        command = e.getActionCommand();
    }

    ImageController oneTimeControl = new SimpleImageController(new StringReader(command), view,
        model);
    oneTimeControl.start();
    view.refresh();
  }

  private String saveFileDirectory() {
    final JFileChooser fchooser = new JFileChooser(".");
    int retvalue = fchooser.showSaveDialog(view.getComponent());
    if (retvalue == JFileChooser.APPROVE_OPTION) {
      File f = fchooser.getSelectedFile();
      return f.getAbsolutePath();
    }
    return null;
  }

  @Override
  public void itemStateChanged(ItemEvent ev) {
    String command = ((JCheckBox) ev.getItemSelectable()).getActionCommand();

    ImageController oneTimeControl = new SimpleImageController(new StringReader(command), view,
        model);
    oneTimeControl.start();
    view.refresh();
  }
}

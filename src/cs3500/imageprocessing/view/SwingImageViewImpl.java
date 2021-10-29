package cs3500.imageprocessing.view;

import cs3500.imageprocessing.model.Channel;
import cs3500.imageprocessing.model.ImageModel;
import cs3500.imageprocessing.model.LayeredImageModelState;
import cs3500.imageprocessing.model.Pixel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * Implementation of the SwingImageView interface. Uses the java.Swift package to generate a GUI
 * that acts as the output for
 */
public class SwingImageViewImpl implements SwingImageView {

  private final LayeredImageModelState model;
  private final ActionListener listener;
  private final JLabel layer;
  private final JPanel togglePanel;
  private final JPanel selectPanel;
  private final JPanel topPanel;
  private final JFrame component;

  /**
   * Constructs a {@Code SwingImageViewImpl} object.
   *
   * @param model    the ImageModelState for the view to get information from.
   * @param listener the listener to handle button presses and ActionEvents.
   */
  public SwingImageViewImpl(LayeredImageModelState model, ActionListener listener) {
    super();
    this.model = model;
    this.listener = listener;
    this.layer = new JLabel(new ImageIcon());
    this.component = new JFrame();

    component.setTitle("Image Processor");
    component.setSize(1000, 1000);

    // panel that shows current layer's image, and radio selection buttons
    this.topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));

    //checkboxes to toggle layers on and off
    JScrollPane togglePanelPane = new JScrollPane();
    togglePanel = new JPanel();
    togglePanel.setBorder(BorderFactory.createTitledBorder("Toggle layers"));
    togglePanel.setLayout(new BoxLayout(togglePanel, BoxLayout.PAGE_AXIS));
    togglePanel.setMaximumSize(new Dimension(150, 150));
    togglePanelPane = new JScrollPane(togglePanel);
    togglePanelPane.setMaximumSize(new Dimension(150, 150));
    togglePanelPane.setPreferredSize(new Dimension(150, 150));

    topPanel.add(togglePanelPane);

    // panel to show topmost visible layer
    JScrollPane imagePane = new JScrollPane(layer);
    imagePane.setPreferredSize(new Dimension(500, 500));
    imagePane.setMaximumSize(imagePane.getPreferredSize());
    topPanel.add(imagePane, JPanel.CENTER_ALIGNMENT);

    //radio buttons to select current layer
    JScrollPane selectPanelPane = new JScrollPane();
    selectPanel = new JPanel();
    selectPanel.setBorder(BorderFactory.createTitledBorder("Select layer"));

    selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.PAGE_AXIS));
    selectPanel.setMaximumSize(new Dimension(150, 150));
    selectPanelPane = new JScrollPane(selectPanel);
    selectPanelPane.setMaximumSize(new Dimension(150, 150));
    selectPanelPane.setPreferredSize(new Dimension(150, 150));

    topPanel.add(selectPanelPane);

    menuSetup();

    // button pane
    JPanel buttonPane = new JPanel();
    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
    buttonPane.add(Box.createHorizontalGlue());

    // checkerboard button
    JButton checkerboard = new JButton("checkerboard");
    checkerboard.setActionCommand("checkerboard");
    checkerboard.addActionListener(listener);
    buttonPane.add(checkerboard);

    // sepia button
    JButton sepia = new JButton("sepia");
    sepia.setActionCommand("sepia");
    sepia.addActionListener(listener);
    buttonPane.add(sepia);

    // blur button
    JButton blur = new JButton("blur");
    blur.setActionCommand("blur");
    blur.addActionListener(listener);
    buttonPane.add(blur);

    // sharpen button
    JButton sharpen = new JButton("sharpen");
    sharpen.setActionCommand("sharpen");
    sharpen.addActionListener(listener);
    buttonPane.add(sharpen);

    // grayscale button
    JButton grayscale = new JButton("grayscale");
    grayscale.setActionCommand("grayscale");
    grayscale.addActionListener(listener);
    buttonPane.add(grayscale);

    // resize button
    JButton resize = new JButton("resize");
    resize.setActionCommand("resize");
    resize.addActionListener(listener);
    buttonPane.add(resize);

    // mosaic button
    JButton mosaic = new JButton("mosaic");
    mosaic.setActionCommand("mosaic");
    mosaic.addActionListener(listener);
    buttonPane.add(mosaic);

    buttonPane.add(Box.createHorizontalGlue());
    buttonPane.setMaximumSize(buttonPane.getPreferredSize());

    component.setLayout(new BoxLayout(component.getContentPane(), BoxLayout.PAGE_AXIS));
    component.add(topPanel);
    component.add(buttonPane);
    component.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  @Override
  public void renderMessage(String message) throws IllegalArgumentException {
    JOptionPane.showMessageDialog(component, message, "Error", JOptionPane.WARNING_MESSAGE);
  }

  @Override
  public void refresh() {

    ButtonGroup rGroup1 = new ButtonGroup();
    togglePanel.removeAll();
    selectPanel.removeAll();

    for (int i = 0; i < this.model.numLayers(); i++) {
      int currIndex = model.getCurrentLayerIndex();
      JCheckBox box = new JCheckBox("Layer " + (i + 1));
      box.setSelected(this.model.isVisible(i));
      box.setActionCommand("toggle " + (i + 1));
      box.addActionListener(listener);

      togglePanel.add(box);

      JRadioButton button = new JRadioButton("Layer " + (i + 1));
      button.setActionCommand("select " + (i + 1));
      button.addActionListener(listener);
      if (i == currIndex) {
        button.setSelected(true);
      }
      rGroup1.add(button);
      selectPanel.add(button);
    }

    ImageModel model;
    try {
      model = this.model.getTopmostVisibleLayer();
    } catch (IllegalCallerException e) {
      layer.setIcon(new ImageIcon());
      component.revalidate();
      component.repaint();
      return;
    }

    BufferedImage newImage = new BufferedImage(model.getWidth(), model.getHeight(),
        BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < model.getHeight(); i++) {
      for (int j = 0; j < model.getWidth(); j++) {
        Pixel p = model.getPixelAt(i, j);
        newImage.setRGB(j, i,
            new Color(p.getChannelValue(Channel.RED), p.getChannelValue(Channel.GREEN),
                p.getChannelValue(Channel.BLUE)).getRGB());
      }
    }

    layer.setIcon(new ImageIcon(newImage));
    topPanel.setMaximumSize(topPanel.getPreferredSize());

    component.revalidate();
    component.repaint();
  }

  @Override
  public Component getComponent() {
    return this.component;
  }

  /**
   * Helper to set up the menu bar for the view.
   */
  private void menuSetup() {

    JMenuBar menuBar = new JMenuBar();

    // handles file IO commands such as export, save, and load
    JMenu fileMenu = new JMenu("File");

    JMenuItem m1 = new JMenuItem("Export");
    JMenuItem m2 = new JMenuItem("Save");
    JMenuItem m3 = new JMenuItem("Load");
    JMenuItem m4 = new JMenuItem("Script");

    m1.setActionCommand("export");
    m2.setActionCommand("save");
    m3.setActionCommand("load");
    m4.setActionCommand("script");

    m1.addActionListener(listener);
    m2.addActionListener(listener);
    m3.addActionListener(listener);
    m4.addActionListener(listener);

    fileMenu.add(m1);
    fileMenu.add(m2);
    fileMenu.add(m3);
    fileMenu.add(m4);

    // add menu. handles adding layers either programatically or through imports
    JMenu addMenu = new JMenu("Add");
    JMenuItem addOption1 = new JMenuItem("Import");

    addMenu.add(addOption1);

    addOption1.setActionCommand("import");

    addOption1.addActionListener(listener);

    JMenu createMenu = new JMenu("Create");
    JMenuItem createOption1 = new JMenuItem("Checkerboard");

    createMenu.add(createOption1);
    createOption1.setActionCommand("checkerboard");
    createOption1.addActionListener(listener);

    addMenu.add(createMenu);

    // layer menu, handles selecting and removing layers
    JMenu layerMenu = new JMenu("Layer");
    JMenuItem layerOption1 = new JMenuItem("Select");
    JMenuItem layerOption2 = new JMenuItem("Remove");
    JMenuItem layerOption3 = new JMenuItem("Toggle");
    JMenuItem layerOption4 = new JMenuItem("Resize");

    layerMenu.add(layerOption1);
    layerMenu.add(layerOption2);
    layerMenu.add(layerOption3);
    layerMenu.add(layerOption4);

    layerOption1.setActionCommand("select");
    layerOption2.setActionCommand("remove");
    layerOption3.setActionCommand("toggle");
    layerOption4.setActionCommand("resize");

    layerOption1.addActionListener(listener);
    layerOption2.addActionListener(listener);
    layerOption3.addActionListener(listener);
    layerOption4.addActionListener(listener);

    // filter menu, handles applying filters to the current layer
    JMenu filterMenu = new JMenu("Filter");
    JMenuItem filterOption1 = new JMenuItem("Sepia");
    JMenuItem filterOption2 = new JMenuItem("Grayscale");
    JMenuItem filterOption3 = new JMenuItem("Blur");
    JMenuItem filterOption4 = new JMenuItem("Sharpen");
    JMenuItem filterOption5 = new JMenuItem("Mosaic");

    filterMenu.add(filterOption1);
    filterMenu.add(filterOption2);
    filterMenu.add(filterOption3);
    filterMenu.add(filterOption4);
    filterMenu.add(filterOption5);

    filterOption1.setActionCommand("sepia");
    filterOption2.setActionCommand("grayscale");
    filterOption3.setActionCommand("blur");
    filterOption4.setActionCommand("sharpen");
    filterOption5.setActionCommand("mosaic");

    filterOption1.addActionListener(listener);
    filterOption2.addActionListener(listener);
    filterOption3.addActionListener(listener);
    filterOption4.addActionListener(listener);
    filterOption5.addActionListener(listener);

    menuBar.add(fileMenu);
    menuBar.add(addMenu);
    menuBar.add(layerMenu);
    menuBar.add(filterMenu);
    component.setJMenuBar(menuBar);
  }
}

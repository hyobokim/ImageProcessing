import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cs3500.imageprocessing.model.Channel;
import cs3500.imageprocessing.model.Pixel;
import cs3500.imageprocessing.model.PixelImpl;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the public facing functionality of {@code PixelImpl}.
 */
public class PixelImplTest {

  private final List<List<Double>> otherInvalidMatrix = new ArrayList<>(
      Arrays.asList(Arrays.asList(0.0625, 0.125, 0.0625),
          Arrays.asList(0.125, 0.25, 0.125)));
  private final List<List<Double>> invalidMatrix = new ArrayList<>(
      Arrays.asList(Arrays.asList(0.0625, 0.125, 0.0625),
          Arrays.asList(0.125, 0.25, 0.125),
          Arrays.asList(0.0625, 0.125)));
  private final List<List<Double>> simpleMatrix = new ArrayList<>(
      Arrays.asList(Arrays.asList(0.5, 0.5, 0.5),
          Arrays.asList(0.25, 0.25, 0.25),
          Arrays.asList(0.1, 0.1, 0.1)));
  private Pixel simplePixel;
  private Pixel redPixel;
  private Pixel blackPixel;

  @Before
  public void setup() {
    simplePixel = new PixelImpl(100, 100, 100);
    redPixel = new PixelImpl(Color.RED);
    blackPixel = new PixelImpl(Color.BLACK);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPixelImplConstructorInvalidRed() {
    new PixelImpl(-1, 10, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPixelImplConstructorInvalidGreen() {
    new PixelImpl(50, 256, 50);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPixelImplConstructorInvalidBlue() {
    new PixelImpl(100, 100, 500);
  }

  @Test
  public void testPixelImplConstructorValidChannelValues() {
    Pixel p = new PixelImpl(0, 0, 255);
    assertEquals(0, p.getChannelValue(Channel.RED));
    assertEquals(0, p.getChannelValue(Channel.GREEN));
    assertEquals(255, p.getChannelValue(Channel.BLUE));
  }

  @Test
  public void testPixelImplCopyConstructor() {
    Pixel p = new PixelImpl(new PixelImpl(24, 25, 26));
    assertEquals(24, p.getChannelValue(Channel.RED));
    assertEquals(25, p.getChannelValue(Channel.GREEN));
    assertEquals(26, p.getChannelValue(Channel.BLUE));
  }

  @Test
  public void testPixelImplColorConstructor() {
    Pixel p = new PixelImpl(Color.RED);
    assertEquals(255, p.getChannelValue(Channel.RED));
    assertEquals(0, p.getChannelValue(Channel.GREEN));
    assertEquals(0, p.getChannelValue(Channel.BLUE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyLinearTransformationNullMatrix() {
    Pixel p = new PixelImpl(Color.RED);
    p.applyLinearTransformation(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyLinearTransformationInvalidMatrix() {
    redPixel.applyLinearTransformation(invalidMatrix);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyLinearTransformationTooShortMatrix() {
    redPixel.applyLinearTransformation(otherInvalidMatrix);
  }

  @Test
  public void testApplyLinearTransformationValidMatrix() {
    simplePixel.applyLinearTransformation(simpleMatrix);
    assertEquals(150, simplePixel.getChannelValue(Channel.RED));
    assertEquals(75, simplePixel.getChannelValue(Channel.GREEN));
    assertEquals(30, simplePixel.getChannelValue(Channel.BLUE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetChannelValueNullChannel() {
    blackPixel.getChannelValue(null);
  }

  @Test
  public void testGetChannelValueValidChannels() {
    assertEquals(0, blackPixel.getChannelValue(Channel.RED));
    assertEquals(0, blackPixel.getChannelValue(Channel.GREEN));
    assertEquals(0, blackPixel.getChannelValue(Channel.BLUE));
    assertEquals(255, redPixel.getChannelValue(Channel.RED));
    assertEquals(0, redPixel.getChannelValue(Channel.GREEN));
    assertEquals(0, redPixel.getChannelValue(Channel.BLUE));
    assertEquals(100, simplePixel.getChannelValue(Channel.RED));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetChannelNullChannel() {
    blackPixel.setChannel(null, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetChannelNegativeValue() {
    blackPixel.setChannel(Channel.RED, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetChannelTooBigValue() {
    blackPixel.setChannel(Channel.BLUE, 256);
  }

  @Test
  public void testSetChannelValidValuesAndChannels() {
    assertEquals(0, blackPixel.getChannelValue(Channel.RED));
    blackPixel.setChannel(Channel.RED, 255);
    assertEquals(255, blackPixel.getChannelValue(Channel.RED));
    assertEquals(0, blackPixel.getChannelValue(Channel.GREEN));
    blackPixel.setChannel(Channel.GREEN, 111);
    assertEquals(111, blackPixel.getChannelValue(Channel.GREEN));
    assertEquals(0, blackPixel.getChannelValue(Channel.BLUE));
    blackPixel.setChannel(Channel.BLUE, 43);
    assertEquals(43, blackPixel.getChannelValue(Channel.BLUE));
    assertEquals(255, redPixel.getChannelValue(Channel.RED));
    redPixel.setChannel(Channel.RED, 0);
    assertEquals(0, redPixel.getChannelValue(Channel.RED));
  }

  @Test
  public void testToString() {
    assertEquals("255\n0\n0", redPixel.toString());
    assertEquals("0\n0\n0", blackPixel.toString());
    assertEquals("100\n100\n100", simplePixel.toString());
  }

  @Test
  public void testEquals() {
    assertFalse(redPixel.equals(blackPixel));
    assertTrue(redPixel.equals(redPixel));
    assertFalse(redPixel.equals(new PixelImpl(255, 0, 100)));
    assertTrue(redPixel.equals(new PixelImpl(Color.RED)));
    assertTrue(blackPixel.equals(new PixelImpl(0, 0, 0)));
    assertFalse(simplePixel == null);
    assertFalse(simplePixel.equals(new ArrayList<Double>()));
  }

  @Test
  public void testHashCode() {
    assertEquals(255000000, redPixel.hashCode());
    assertEquals(0, blackPixel.hashCode());
    assertEquals(100100100, simplePixel.hashCode());
  }
}

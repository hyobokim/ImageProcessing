import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import cs3500.imageprocessing.view.ImageView;
import cs3500.imageprocessing.view.TextImageView;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the public facing functionality of {@link TextImageView}.
 */
public class TextImageViewTest {

  private ImageView view;
  private Appendable ap;

  @Before
  public void setup() {
    ap = new StringBuilder();
    view = new TextImageView(ap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTextImageViewConstructorNullParam() {
    new TextImageView(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTextImageViewRenderMessageNull() {
    try {
      view.renderMessage(null);
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void testTextImageViewRenderMessageValid() {
    try {
      view.renderMessage("test!");
      assertEquals("test!", ap.toString());
      view.renderMessage(" more stuff!");
      assertEquals("test! more stuff!", ap.toString());
      view.renderMessage(" a bit more.\n");
      assertEquals("test! more stuff! a bit more.\n", ap.toString());
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void testTextImageViewRenderMessageIOException() {
    try {
      new TextImageView(new MockAppendable()).renderMessage("good message.");
      fail();
    } catch (IOException e) {
      assertTrue(e instanceof IOException);
    }
  }
}

import java.io.IOException;

/**
 * Mock appendable object that throws an IOException for every version of append.
 * Used to test that methods that use Appendables correctly handle this exception.
 */
public class MockAppendable implements Appendable {

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException("mock");
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException("mock");
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException("mock");
  }
}

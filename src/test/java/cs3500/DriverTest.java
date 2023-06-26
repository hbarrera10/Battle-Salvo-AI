package cs3500;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * test the driver class
 */
class DriverTest {

  /**
   * tests the invalid arguments branch of Driver
   */
  @Test
  public void testMainBranch3() {
    String[] invalidArgs = new String[]{"Invalid"};
    assertThrows(IllegalArgumentException.class, () -> Driver.main(invalidArgs));
  }

}
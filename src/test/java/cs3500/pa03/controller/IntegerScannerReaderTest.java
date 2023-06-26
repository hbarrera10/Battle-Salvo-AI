package cs3500.pa03.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

/**
 * test the IntegerScannerReader
 */
class IntegerScannerReaderTest {

  @Test
  void constructorTest() {
    assertThrows(IllegalArgumentException.class, () -> new IntegerScannerReader(0,
        1, "blah"));
    assertThrows(IllegalArgumentException.class, () -> new IntegerScannerReader(1,
        0, "blah"));
  }

  @Test
  void testRead() {
    String input = "1 2 3\n4 5 6\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    IntegerScannerReader reader = new IntegerScannerReader(2, 3, "Enter integers: ");
    int[][] result = reader.read();

    String expectedOutput = "Enter integers: ";
    assertEquals(expectedOutput, outputStream.toString());

    int[][] expectedArray = {
        {1, 2, 3},
        {4, 5, 6}
    };
    assertArrayEquals(expectedArray, result);
  }
}
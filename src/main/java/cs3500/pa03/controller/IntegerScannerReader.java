package cs3500.pa03.controller;

import java.util.Scanner;

/**
 * represents a reader that scans integers by rows
 */
public class IntegerScannerReader implements Reader<int[][]> {
  private final Scanner scanner = new Scanner(System.in);
  private final int numOfLines;
  private final int numOfIntsPerLine;
  private final String directions;

  /**
   * constructor
   *
   * @param numOfLines the number of lines
   * @param numOfIntsPerLine the number of integers per line
   * @param directions the directions displayed as a prompt
   */
  public IntegerScannerReader(int numOfLines, int numOfIntsPerLine, String directions) {
    if (numOfLines < 1) {
      throw new IllegalArgumentException("Given number of lines argument is less than 1.");
    }
    this.numOfLines = numOfLines;
    if (numOfIntsPerLine < 1) {
      throw new IllegalArgumentException(
          "Given number of integers per line argument is less than 1.");
    }
    this.numOfIntsPerLine = numOfIntsPerLine;
    this.directions = directions;
  }

  /**
   * Reads the contents of the inputted info by the user to an array of integers.
   *
   * @return the message contents
   */
  @Override
  public int[][] read() {
    System.out.print(directions);
    int[][] output = new int[numOfLines][numOfIntsPerLine];

    for (int i = 0; i < numOfLines; i++) {
      for (int j = 0; j < numOfIntsPerLine; j++) {
        if (scanner.hasNextInt()) {
          output[i][j] = scanner.nextInt();
        }
      }
    }
    return output;
  }
}

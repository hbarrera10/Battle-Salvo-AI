package cs3500.pa03.controller;

/**
 * represents a reader
 *
 * @param <T> output info of type T
 */
public interface Reader<T> {
  /**
   * reads and returns T
   *
   * @return the read info of type T
   */
  T read();
}

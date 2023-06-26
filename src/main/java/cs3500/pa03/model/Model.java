package cs3500.pa03.model;

/**
 * represents the Model of the MVC architecture
 *
 * @param <T> represents the type of data that is being processed
 */
public interface Model<T> {
  /**
   * updates the model
   *
   * @param data being used to update
   */
  void update(T data);
}

package cs3500.pa04.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonUtilsTest {

  @Test
  void serializeRecord_InvalidRecord_ThrowsIllegalArgumentException() {
    // Arrange
    InvalidRecord record = new InvalidRecord();

    // Act & Assert
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        JsonUtils.serializeRecord(record));
  }

  private record InvalidRecord() {
    // this getName method is used implicitly, and it throws an error
    public String getName() {
      throw new UnsupportedOperationException("InvalidRecord does not support getName()");
    }
  }
}
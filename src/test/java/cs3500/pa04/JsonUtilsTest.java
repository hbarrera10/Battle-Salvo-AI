package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.controller.CoordJson;
import cs3500.pa04.controller.JsonUtils;
import org.junit.jupiter.api.Test;

/**
 * represents a test for the JsonUtils class
 */
public class JsonUtilsTest {

  @Test
  public void testSerializeRecord() {
    // Create a sample record object
    CoordJson coordJson = new CoordJson(2, 3);

    // Serialize the record using JsonUtils
    JsonNode jsonNode = JsonUtils.serializeRecord(coordJson);

    // Deserialize the JSON back into a CoordJson object
    ObjectMapper mapper = new ObjectMapper();
    CoordJson deserializedCoordJson = mapper.convertValue(jsonNode, CoordJson.class);

    // Ensure that the deserialized object matches the original object
    assertEquals(coordJson, deserializedCoordJson);
  }
}

package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cs3500.pa04.controller.CoordJson;
import org.junit.jupiter.api.Test;

/**
 * represents a test for the CoordJson class
 */
public class CoordJsonTest {

  @Test
  public void testCoordJsonConstructorAndGetters() {
    CoordJson coordJson = new CoordJson(2, 3);

    assertEquals(2, coordJson.x());
    assertEquals(3, coordJson.y());
  }

  @Test
  public void testCoordJsonSerialization() {
    CoordJson coordJson = new CoordJson(2, 3);

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode expectedJson = mapper.createObjectNode();
    expectedJson.put("x", 2);
    expectedJson.put("y", 3);

    assertEquals(expectedJson.toString(), mapper.valueToTree(coordJson).toString());
  }

  @Test
  public void testCoordJsonDeserialization() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode json = mapper.createObjectNode();
    json.put("x", 2);
    json.put("y", 3);

    CoordJson coordJson = mapper.convertValue(json, CoordJson.class);

    assertEquals(2, coordJson.x());
    assertEquals(3, coordJson.y());
  }
}

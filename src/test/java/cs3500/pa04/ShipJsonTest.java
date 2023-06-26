package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cs3500.pa04.controller.CoordJson;
import cs3500.pa04.controller.ShipJson;
import org.junit.jupiter.api.Test;

/**
 * test the ShipJson Record
 */
public class ShipJsonTest {

  @Test
  public void testShipJsonConstructorAndGetters() {
    CoordJson coordJson = new CoordJson(2, 3);
    ShipJson shipJson = new ShipJson(coordJson, 4, "horizontal");

    assertEquals(coordJson, shipJson.coord());
    assertEquals(4, shipJson.length());
    assertEquals("horizontal", shipJson.direction());
  }

  @Test
  public void testShipJsonSerialization() {
    CoordJson coordJson = new CoordJson(2, 3);

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode expectedJson = mapper.createObjectNode();
    expectedJson.set("coord", mapper.valueToTree(coordJson));
    expectedJson.put("length", 4);
    expectedJson.put("direction", "horizontal");

    ShipJson shipJson = new ShipJson(coordJson, 4, "horizontal");

    assertEquals(expectedJson.toString(), mapper.valueToTree(shipJson).toString());
  }

  @Test
  public void testShipJsonDeserialization() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode json = mapper.createObjectNode();
    CoordJson coordJson = new CoordJson(2, 3);
    json.set("coord", mapper.valueToTree(coordJson));
    json.put("length", 4);
    json.put("direction", "horizontal");

    ShipJson shipJson = mapper.convertValue(json, ShipJson.class);

    assertEquals(coordJson, shipJson.coord());
    assertEquals(4, shipJson.length());
    assertEquals("horizontal", shipJson.direction());
  }
}

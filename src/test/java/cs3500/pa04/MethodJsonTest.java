package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cs3500.pa04.controller.MethodJson;
import org.junit.jupiter.api.Test;

/**
 * represents a test for the MethodJson class
 */
public class MethodJsonTest {

  @Test
  public void testMethodJsonConstructorAndGetters() {
    JsonNode arguments = new ObjectMapper().createObjectNode();
    MethodJson methodJson = new MethodJson("join", arguments);

    assertEquals("join", methodJson.methodName());
    assertEquals(arguments, methodJson.arguments());
  }

  @Test
  public void testMethodJsonSerialization() {
    JsonNode arguments = new ObjectMapper().createObjectNode();
    MethodJson methodJson = new MethodJson("join", arguments);

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode expectedJson = mapper.createObjectNode();
    expectedJson.put("method-name", "join");
    expectedJson.set("arguments", arguments);

    assertEquals(expectedJson.toString(), mapper.valueToTree(methodJson).toString());
  }

  @Test
  public void testMethodJsonDeserialization() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode json = mapper.createObjectNode();
    json.put("method-name", "join");
    json.set("arguments", mapper.createObjectNode());

    MethodJson methodJson = mapper.convertValue(json, MethodJson.class);

    assertEquals("join", methodJson.methodName());
    assertEquals(mapper.createObjectNode(), methodJson.arguments());
  }
}

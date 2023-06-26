package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.ShipType;
import cs3500.pa04.controller.JsonUtils;
import cs3500.pa04.controller.MethodJson;
import cs3500.pa04.controller.ProxyController;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test correct responses for different requests from the mocket
 */
public class ProxyControllerTest {

  private ByteArrayOutputStream testLog;
  private ProxyController controller;
  private final ObjectMapper mapper = new ObjectMapper();


  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setup() {
    this.testLog = new ByteArrayOutputStream(2048);
    assertEquals("", logToString());
  }

  @Test
  public void testJoinSingle() {
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("game-type", "SINGLE");
    argumentsObject.put("name", "Henry and Sasha");
    MethodJson join = new MethodJson("join", argumentsObject);
    JsonNode sampleMessage = JsonUtils.serializeRecord(join);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      this.controller = new ProxyController(socket, new AiPlayer());
    } catch (Exception e) {
      fail();
    }
    this.controller.run();

    assertEquals("{\"method-name\":\"join\",\"arguments\":{\"game-type\":\"MULTI\",\"name"
        + "\":\"Henry and Sasha\"}}\n", socket.getOutputStream().toString());
  }

  @Test
  public void testSetup() {
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("width", 6);
    argumentsObject.put("height", 6);

    // create the fleet specifications
    Map<ShipType, Integer> fleetSpecifications = new HashMap<>();
    fleetSpecifications.put(ShipType.CARRIER, 1);
    fleetSpecifications.put(ShipType.BATTLESHIP, 1);
    fleetSpecifications.put(ShipType.DESTROYER, 1);
    fleetSpecifications.put(ShipType.SUBMARINE, 1);
    JsonNode fleetSpecsNode = mapper.valueToTree(fleetSpecifications);

    // add the fleet specifications to the arguments object
    argumentsObject.set("fleet-spec", fleetSpecsNode);

    // create the MethodJson object with the arguments object
    MethodJson setup = new MethodJson("setup", argumentsObject);

    // create the sample message using the "setup" method and the MethodJson object
    JsonNode sampleMessage = createSampleMessage("setup", setup);

    // create a mocket instance with the test log and the sample message
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      controller.delegateMessage(setup);
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
    assertTrue(socket.getOutputStream().toString().contains("{\"method-name\":\"setup\",\"arguments"
        + "\":{\"fleet\":[{\"coord\":{"));
  }

  @Test
  public void testTakeShots() {
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("width", 6);
    argumentsObject.put("height", 6);

    // create the fleet specifications
    Map<ShipType, Integer> fleetSpecifications = new HashMap<>();
    fleetSpecifications.put(ShipType.CARRIER, 1);
    fleetSpecifications.put(ShipType.BATTLESHIP, 1);
    fleetSpecifications.put(ShipType.DESTROYER, 1);
    fleetSpecifications.put(ShipType.SUBMARINE, 1);
    JsonNode fleetSpecsNode = mapper.valueToTree(fleetSpecifications);

    // add the fleet specifications to the arguments object
    argumentsObject.set("fleet-spec", fleetSpecsNode);

    // create the setup MethodJson object
    MethodJson setup = new MethodJson("setup", argumentsObject);

    // create the takeShots MethodJson object
    MethodJson takeShots = new MethodJson("take-shots",
        JsonNodeFactory.instance.objectNode());

    // create the sample messages using the MethodJson objects
    JsonNode setupMessage = createSampleMessage("setup", setup);
    JsonNode takeShotsMessage = createSampleMessage("take-shots", takeShots);

    // create a Mocket instance with the test log and the sample messages
    Mocket socket = new Mocket(this.testLog, List.of(setupMessage.toString(),
        takeShotsMessage.toString()));

    try {
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      controller.delegateMessage(setup);
      controller.delegateMessage(takeShots);
      assertTrue(socket.getOutputStream().toString().contains("{\"method-name\":\"take-shots\",\""
          + "arguments\":{\"coordinates\":[{"));
    } catch (IOException e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testReportDamage() {
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("width", 6);
    argumentsObject.put("height", 6);

    // create the fleet specifications
    Map<ShipType, Integer> fleetSpecifications = new HashMap<>();
    fleetSpecifications.put(ShipType.CARRIER, 1);
    fleetSpecifications.put(ShipType.BATTLESHIP, 1);
    fleetSpecifications.put(ShipType.DESTROYER, 1);
    fleetSpecifications.put(ShipType.SUBMARINE, 1);
    JsonNode fleetSpecsNode = mapper.valueToTree(fleetSpecifications);

    // add the fleet specifications to the arguments object
    argumentsObject.set("fleet-spec", fleetSpecsNode);

    // create the setup MethodJson object
    MethodJson setup = new MethodJson("setup", argumentsObject);

    // create the arguments object with the desired properties
    ObjectNode argumentsObjectReportDamage = JsonNodeFactory.instance.objectNode();
    ArrayNode coordinatesArray = argumentsObjectReportDamage.putArray("coordinates");
    ObjectNode coord1 = coordinatesArray.addObject();
    coord1.put("x", 3);
    coord1.put("y", 4);
    ObjectNode coord2 = coordinatesArray.addObject();
    coord2.put("x", 2);
    coord2.put("y", 5);

    MethodJson reportDamage = new MethodJson("report-damage", argumentsObjectReportDamage);
    JsonNode sampleMessage = JsonUtils.serializeRecord(reportDamage);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      controller.delegateMessage(setup);
      controller.delegateMessage(reportDamage);
      assertTrue(socket.getOutputStream().toString().contains("{\"method-name\":\"report-damage\","
          + "\"arguments\":{\"coordinates\":["));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testSuccessfulHits() {
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("width", 6);
    argumentsObject.put("height", 6);

    // create the fleet specifications
    Map<ShipType, Integer> fleetSpecifications = new HashMap<>();
    fleetSpecifications.put(ShipType.CARRIER, 1);
    fleetSpecifications.put(ShipType.BATTLESHIP, 1);
    fleetSpecifications.put(ShipType.DESTROYER, 1);
    fleetSpecifications.put(ShipType.SUBMARINE, 1);
    JsonNode fleetSpecsNode = mapper.valueToTree(fleetSpecifications);

    // add the fleet specifications to the arguments object
    argumentsObject.set("fleet-spec", fleetSpecsNode);

    // create the setup MethodJson object
    MethodJson setup = new MethodJson("setup", argumentsObject);


    ObjectNode argumentsObjectSuccessfulHits = JsonNodeFactory.instance.objectNode();
    ArrayNode coordinatesArray = argumentsObjectSuccessfulHits.putArray("coordinates");
    ObjectNode coord1 = coordinatesArray.addObject();
    coord1.put("x", 3);
    coord1.put("y", 4);
    ObjectNode coord2 = coordinatesArray.addObject();
    coord2.put("x", 2);
    coord2.put("y", 5);

    MethodJson successfulHits = new MethodJson("successful-hits",
        argumentsObjectSuccessfulHits);

    JsonNode sampleMessage = JsonUtils.serializeRecord(successfulHits);
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      // create the ProxyController instance with the Mocket and player
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      controller.delegateMessage(setup);
      controller.delegateMessage(successfulHits);
      assertTrue(socket.getOutputStream().toString().contains("{\"method-name\":\"successful-hits\""
          + ",\"arguments\":{}}"));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testEndGameWin() {
    // create the arguments object with the desired properties
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("result", "WIN");
    argumentsObject.put("reason", "Game over");

    // create the MethodJson object with the arguments object
    MethodJson endGame = new MethodJson("end-game", argumentsObject);

    // create the sample message using the "end-game" method and the MethodJson object
    JsonNode sampleMessage = createSampleMessage("end-game", endGame);

    // create a mocket instance with the test log and the sample message
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      // create the ProxyController instance with the mocket and player
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      controller.delegateMessage(endGame);
      assertTrue(socket.getOutputStream().toString().contains("{\"method-name\":\"end-game\",\""
          + "arguments\":{}}"));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testEndGameLoss() {
    // create the arguments object with the desired properties
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("result", "LOSE");
    argumentsObject.put("reason", "You lost!");

    // create the MethodJson object with the arguments object
    MethodJson endGame = new MethodJson("end-game", argumentsObject);

    // create the sample message using the "end-game" method and the MethodJson object
    JsonNode sampleMessage = createSampleMessage("end-game", endGame);

    // create a mocket instance with the test log and the sample message
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      // create the ProxyController instance with the mocket and player
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      controller.delegateMessage(endGame);
      assertTrue(socket.getOutputStream().toString().contains("{\"method-name\":\"end-game\",\""
          + "arguments\":{}}"));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testEndGameInvalidResult() {
    // create the arguments object with the desired properties
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("reason", "Game over");

    // create the MethodJson object with the arguments object
    MethodJson endGame = new MethodJson("end-game", argumentsObject);

    // create the sample message using the "end-game" method and the MethodJson object
    JsonNode sampleMessage = createSampleMessage("end-game", endGame);

    // create a mocket instance with the test log and the sample message
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      // create the ProxyController instance with the mocket and player
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      assertThrows(IllegalStateException.class, () -> controller.delegateMessage(endGame));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testEndGameInvalidReason() {
    // create the arguments object with the desired properties
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("result", "WIN");

    // create the MethodJson object with the arguments object
    MethodJson endGame = new MethodJson("end-game", argumentsObject);

    // create the sample message using the "end-game" method and the MethodJson object
    JsonNode sampleMessage = createSampleMessage("end-game", endGame);

    // create a mocket instance with the test log and the sample message
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      // create the ProxyController instance with the mocket and player
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      assertThrows(IllegalStateException.class, () -> controller.delegateMessage(endGame));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testEndGameInvalidResultString() {
    // create the arguments object with the desired properties
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("result", "INVALID-END");
    argumentsObject.put("reason", "Game over");

    // create the MethodJson object with the arguments object
    MethodJson endGame = new MethodJson("end-game", argumentsObject);

    // create the sample message using the "end-game" method and the MethodJson object
    JsonNode sampleMessage = createSampleMessage("end-game", endGame);

    // create a mocket instance with the test log and the sample message
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      // create the ProxyController instance with the mocket and player
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      assertThrows(IllegalStateException.class, () -> controller.delegateMessage(endGame));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testEndGameDraw() {
    // create the arguments object with the desired properties
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("result", "DRAW");
    argumentsObject.put("reason", "It was a draw");

    // create the MethodJson object with the arguments object
    MethodJson endGame = new MethodJson("end-game", argumentsObject);

    // create the sample message using the "end-game" method and the MethodJson object
    JsonNode sampleMessage = createSampleMessage("end-game", endGame);

    // create a mocket instance with the test log and the sample message
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      // create the ProxyController instance with the mocket and player
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      controller.delegateMessage(endGame);
      assertTrue(socket.getOutputStream().toString().contains("{\"method-name\":\"end-game\",\""
          + "arguments\":{}}"));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testDelegateMessageException() {
    // create the arguments object with the desired properties
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();

    // create the MethodJson object with the arguments object
    MethodJson invalidMethod = new MethodJson("invalid-method", argumentsObject);

    // create a mocket instance with the test log and the sample message
    Mocket socket = new Mocket(this.testLog, List.of(invalidMethod.toString()));

    try {
      // create the ProxyController instance with the mocket and player
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      assertThrows(IllegalStateException.class, () -> controller.delegateMessage(invalidMethod));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }


  @Test
  public void testSetupNoWidth() {
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("height", 6);

    // create the fleet specifications
    Map<ShipType, Integer> fleetSpecifications = new HashMap<>();
    fleetSpecifications.put(ShipType.CARRIER, 1);
    fleetSpecifications.put(ShipType.BATTLESHIP, 1);
    fleetSpecifications.put(ShipType.DESTROYER, 1);
    fleetSpecifications.put(ShipType.SUBMARINE, 1);
    JsonNode fleetSpecsNode = mapper.valueToTree(fleetSpecifications);

    // add the fleet specifications to the arguments object
    argumentsObject.set("fleet-spec", fleetSpecsNode);

    // create the MethodJson object with the arguments object
    MethodJson setup = new MethodJson("setup", argumentsObject);

    // create the sample message using the "setup" method and the MethodJson object
    JsonNode sampleMessage = createSampleMessage("setup", setup);

    // create a mocket instance with the test log and the sample message
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      assertThrows(IllegalStateException.class, () -> controller.delegateMessage(setup));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testSetupNoHeight() {
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("width", 6);

    // create the fleet specifications
    Map<ShipType, Integer> fleetSpecifications = new HashMap<>();
    fleetSpecifications.put(ShipType.CARRIER, 1);
    fleetSpecifications.put(ShipType.BATTLESHIP, 1);
    fleetSpecifications.put(ShipType.DESTROYER, 1);
    fleetSpecifications.put(ShipType.SUBMARINE, 1);
    JsonNode fleetSpecsNode = mapper.valueToTree(fleetSpecifications);

    // add the fleet specifications to the arguments object
    argumentsObject.set("fleet-spec", fleetSpecsNode);

    // create the MethodJson object with the arguments object
    MethodJson setup = new MethodJson("setup", argumentsObject);

    // create the sample message using the "setup" method and the MethodJson object
    JsonNode sampleMessage = createSampleMessage("setup", setup);

    // create a mocket instance with the test log and the sample message
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      assertThrows(IllegalStateException.class, () -> controller.delegateMessage(setup));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testSetupNoFleetSpec() {
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("height", 6);
    argumentsObject.put("width", 6);

    // create the MethodJson object with the arguments object
    MethodJson setup = new MethodJson("setup", argumentsObject);

    // create the sample message using the "setup" method and the MethodJson object
    JsonNode sampleMessage = createSampleMessage("setup", setup);

    // create a mocket instance with the test log and the sample message
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      assertThrows(IllegalStateException.class, () -> controller.delegateMessage(setup));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testSuccessfulHitsNoCoords() {
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("width", 6);
    argumentsObject.put("height", 6);

    // create the fleet specifications
    Map<ShipType, Integer> fleetSpecifications = new HashMap<>();
    fleetSpecifications.put(ShipType.CARRIER, 1);
    fleetSpecifications.put(ShipType.BATTLESHIP, 1);
    fleetSpecifications.put(ShipType.DESTROYER, 1);
    fleetSpecifications.put(ShipType.SUBMARINE, 1);
    JsonNode fleetSpecsNode = mapper.valueToTree(fleetSpecifications);

    // add the fleet specifications to the arguments object
    argumentsObject.set("fleet-spec", fleetSpecsNode);

    // create the setup MethodJson object
    MethodJson setup = new MethodJson("setup", argumentsObject);

    MethodJson successfulHits = new MethodJson("successful-hits",
        JsonNodeFactory.instance.objectNode());

    JsonNode sampleMessage = JsonUtils.serializeRecord(successfulHits);
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      // create the ProxyController instance with the Mocket and player
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      controller.delegateMessage(setup);
      assertThrows(IllegalStateException.class, () -> controller.delegateMessage(successfulHits));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }

  @Test
  public void testSuccessfulHitsCoordNoArray() {
    ObjectNode argumentsObject = JsonNodeFactory.instance.objectNode();
    argumentsObject.put("width", 6);
    argumentsObject.put("height", 6);

    // create the fleet specifications
    Map<ShipType, Integer> fleetSpecifications = new HashMap<>();
    fleetSpecifications.put(ShipType.CARRIER, 1);
    fleetSpecifications.put(ShipType.BATTLESHIP, 1);
    fleetSpecifications.put(ShipType.DESTROYER, 1);
    fleetSpecifications.put(ShipType.SUBMARINE, 1);
    JsonNode fleetSpecsNode = mapper.valueToTree(fleetSpecifications);

    // add the fleet specifications to the arguments object
    argumentsObject.set("fleet-spec", fleetSpecsNode);

    // create the setup MethodJson object
    MethodJson setup = new MethodJson("setup", argumentsObject);


    ObjectNode argumentsObjectSuccessfulHits = JsonNodeFactory.instance.objectNode();
    argumentsObjectSuccessfulHits.put("coordinates", "invalid");

    MethodJson successfulHits = new MethodJson("successful-hits",
        argumentsObjectSuccessfulHits);

    JsonNode sampleMessage = JsonUtils.serializeRecord(successfulHits);
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    try {
      // create the ProxyController instance with the Mocket and player
      ProxyController controller = new ProxyController(socket, new AiPlayer());
      controller.delegateMessage(setup);
      assertThrows(IllegalStateException.class, () -> controller.delegateMessage(successfulHits));
    } catch (Exception e) {
      fail("Failed to initialize the ProxyController: " + e.getMessage());
    }
  }


  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   */
  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }

  /**
   * Create a MessageJson for some name and arguments.
   *
   * @param messageName   name of the type of message; "hint" or "win"
   * @param messageObject object to embed in a message json
   * @return a MessageJson for the object
   */
  private JsonNode createSampleMessage(String messageName, Record messageObject) {
    MethodJson messageJson =
        new MethodJson(messageName, JsonUtils.serializeRecord(messageObject));
    return JsonUtils.serializeRecord(messageJson);
  }
}
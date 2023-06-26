package cs3500.pa04.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cs3500.pa03.controller.Controller;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.Player;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * represents the proxy controller for dealing with server requests
 */
public class ProxyController implements Controller {
  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final Player player;
  private final ObjectMapper mapper = new ObjectMapper();


  /**
   * base constructor
   *
   * @param server represents the server that the proxy controller is interacting with
   * @param player representing the player that is interacting with the server
   * @throws IOException when there is a miscommunication
   */
  public ProxyController(Socket server, Player player) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.player = player;
  }


  /**
   * runs the proxy controller
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MethodJson message = parser.readValueAs(MethodJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      // Disconnected from server or parsing exception
    }
  }

  /**
   * Determines the type of request the server has sent ("guess" or "win") and delegates to the
   * corresponding helper method with the message arguments.
   *
   * @param message the MessageJSON used to determine what the server has sent
   */
  public void delegateMessage(MethodJson message) {
    String name = message.methodName();
    JsonNode arguments = message.arguments();

    if ("join".equals(name)) {
      handleJoin(arguments);
    } else if ("setup".equals(name)) {
      handleSetup(arguments);
    } else if ("take-shots".equals(name)) {
      handleTakeShots();
    } else if ("report-damage".equals(name)) {
      handleReportDamage(arguments);
    } else if ("successful-hits".equals(name)) {
      handleSuccessfulHits(arguments);
    } else if ("end-game".equals(name)) {
      handleEndGame(arguments);
    } else {
      throw new IllegalStateException("Invalid message name");
    }
  }

  /**
   * send a join message back to the server
   *
   * @param arguments for the join method
   */
  private void handleJoin(JsonNode arguments) {
    ObjectNode argumentsObject = ((ObjectNode) arguments).put("game-type", "MULTI");
    argumentsObject.put("name", player.name());
    MethodJson handleJson = new MethodJson("join", argumentsObject);
    JsonNode jsonResponse = JsonUtils.serializeRecord(handleJson);
    out.println(jsonResponse);
  }

  /**
   * sets up the player and sends the orientation of the player's ships
   *
   * @param arguments for the setup method
   */
  private void handleSetup(JsonNode arguments) {
    // check if the arguments are valid
    if (arguments.get("width") == null) {
      throw new IllegalStateException("No width argument within the setup server request.");
    } else if (arguments.get("height") == null) {
      throw new IllegalStateException("No height argument within the setup server request.");
    } else if (arguments.get("fleet-spec") == null) {
      throw new IllegalStateException("No fleet specifications within the setup server request.");
    }

    // initialize the arguments
    int width = Integer.parseInt(String.valueOf(arguments.get("width")));
    int height = Integer.parseInt(String.valueOf(arguments.get("height")));
    Map<ShipType, Integer> fleetSpecifications = mapper.convertValue(arguments.get("fleet-spec"),
        new TypeReference<>(){});

    // get the ship info generated by the player
    List<Ship> shipInfo = player.setup(height, width, fleetSpecifications);
    List<ShipJson> fleet = new ArrayList<>();

    // go through each ship in the generated list and create ShipJsons
    for (Ship s : shipInfo) {
      Coord startingCoord = s.getStartingCoord();
      CoordJson startingCoordJson = new CoordJson(startingCoord.getCol(), startingCoord.getRow());

      ShipJson shipJson = new ShipJson(startingCoordJson, s.getType().getSize(), s.getDirection());
      fleet.add(shipJson);
    }

    // format the response for returning
    ObjectNode argumentsResponse = mapper.createObjectNode();
    argumentsResponse.set("fleet", mapper.valueToTree(fleet));
    MethodJson handleSetup = new MethodJson("setup", argumentsResponse);
    JsonNode setupResponse = JsonUtils.serializeRecord(handleSetup);

    this.out.println(setupResponse);
  }

  /**
   * sends shots taken by the player in this controller
   */
  private void handleTakeShots() {
    // generate the player shots
    List<CoordJson> playerJsonShots = coordsToJsonCoordList(player.takeShots());

    // format the response for returning
    ObjectNode argumentsResponse = mapper.createObjectNode();
    argumentsResponse.set("coordinates", mapper.valueToTree(playerJsonShots));
    MethodJson handleSetup = new MethodJson("take-shots", argumentsResponse);
    JsonNode setupResponse = JsonUtils.serializeRecord(handleSetup);
    this.out.println(setupResponse);
  }

  /**
   * reports the damage dealt by the server shots
   *
   * @param arguments for report-damage method
   */
  private void handleReportDamage(JsonNode arguments) {
    // handle the input coordinates
    List<Coord> opponentShots = handleJsonCoords(arguments);
    // shots reporting damage
    List<CoordJson> reportedJsonDamage = coordsToJsonCoordList(player.reportDamage(opponentShots));

    // format the response for returning
    ObjectNode argumentsResponse = mapper.createObjectNode();
    argumentsResponse.set("coordinates", mapper.valueToTree(reportedJsonDamage));
    MethodJson handleSetup = new MethodJson("report-damage", argumentsResponse);
    JsonNode setupResponse = JsonUtils.serializeRecord(handleSetup);
    this.out.println(setupResponse);
  }

  /**
   * converts a List of Coord into a List of CoordJson
   *
   * @param listOfCoord the to be transformed list
   * @return the transformed Json formatted list
   */
  private List<CoordJson> coordsToJsonCoordList(List<Coord> listOfCoord) {
    List<CoordJson> returnJsonCoord = new ArrayList<>();
    // convert the coordinates to Json format
    for (Coord c : listOfCoord) {
      CoordJson jsonShot = new CoordJson(c.getCol(), c.getRow());
      returnJsonCoord.add(jsonShot);
    }
    return returnJsonCoord;
  }

  /**
   * @param arguments turn Json coords into Coord
   * @return List of Coord
   */
  private List<Coord> handleJsonCoords(JsonNode arguments) {
    if (arguments.get("coordinates") == null) {
      throw new IllegalStateException("Expected coordinates argument but not given.");
    }
    JsonNode coordinates = arguments.get("coordinates");
    List<Coord> returnCoordinates = new ArrayList<>();
    if (coordinates.isArray()) {
      for (JsonNode coord : coordinates) {
        int col = Integer.parseInt(coord.get("x").toString());
        int row = Integer.parseInt(coord.get("y").toString());

        returnCoordinates.add(new Coord(row, col));
      }
    } else {
      throw new IllegalStateException("There is no coordinates array.");
    }
    return returnCoordinates;
  }

  /**
   * sends the shots that hit to the player in this controller
   *
   * @param arguments for successful-hits method
   */
  private void handleSuccessfulHits(JsonNode arguments) {
    List<Coord> hitShots = handleJsonCoords(arguments);

    // send the successful hits to the player
    player.successfulHits(hitShots);

    MethodJson handleSuccessfulHits = new MethodJson("successful-hits",
        mapper.createObjectNode());
    JsonNode successfulHitsResponse = JsonUtils.serializeRecord(handleSuccessfulHits);
    this.out.println(successfulHitsResponse);
  }

  /**
   * receives the game result and sends the result to the player
   *
   * @param arguments for the end-game method
   */
  private void handleEndGame(JsonNode arguments) {
    if (arguments.get("result") == null) {
      throw new IllegalStateException("No result 'argument' within the end-game server request.");
    } else if (arguments.get("reason") == null) {
      throw new IllegalStateException("No result 'reason' within the end-game server request.");
    }
    String result = arguments.get("result").toString();
    String reason = arguments.get("reason").toString();

    if (result.contains("WIN")) {
      player.endGame(GameResult.WIN, reason);
    } else if (result.contains("LOSE")) {
      player.endGame(GameResult.LOSS, reason);
    } else if (result.contains("DRAW")) {
      player.endGame(GameResult.DRAW, reason);
    } else {
      throw new IllegalStateException("Game result given is not valid");
    }

    MethodJson handleEndGame = new MethodJson("end-game",
        mapper.createObjectNode());
    JsonNode endGameResponse = JsonUtils.serializeRecord(handleEndGame);
    this.out.println(endGameResponse);
    try {
      this.server.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
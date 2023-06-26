package cs3500.pa04.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * represents a ship in JSON form
 *
 * @param coord representing the starting position of the ship
 * @param length of the ship
 * @param direction of the ship
 */
public record ShipJson(
    @JsonProperty("coord") CoordJson coord,
    @JsonProperty("length") int length,
    @JsonProperty("direction") String direction) {
}


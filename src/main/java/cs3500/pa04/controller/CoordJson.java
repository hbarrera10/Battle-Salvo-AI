package cs3500.pa04.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * represents a coordinate but in JSON form
 * <p>
 *  * <code>
 *  * {
 *  *   "x": 6,
 *  *   "y": 6
 *  * }
 *  * </code>
 *  * </p>
 *
 * @param x representing the x parameter of the coordinate
 * @param y representing the y parameter of the coordinate
 */
public record CoordJson(
    @JsonProperty("x") int x,
    @JsonProperty("y") int y) {
}

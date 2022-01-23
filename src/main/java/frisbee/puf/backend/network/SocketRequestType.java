package frisbee.puf.backend.network;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SocketRequestType {
    /**
     * The request type for initializing the game setup.
     */
    @JsonProperty("INIT")
    INIT,
    /**
     * The request type for notifying the other client that player is ready.
     */
    @JsonProperty("READY")
    READY,
    /**
     * The request type for informing the other client about character movement.
     */
    @JsonProperty("MOVE")
    MOVE,
    /**
     * The request type for informing the other client that the frisbee was
     * thrown.
     */
    @JsonProperty("THROW")
    THROW,
    /**
     * The request type for starting the game.
     */
    @JsonProperty("GAME_RUNNING")
    GAME_RUNNING,
    /**
     * The request type for disconnecting a client.
     */
    @JsonProperty("DISCONNECT")
    DISCONNECT
}

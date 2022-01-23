package frisbee.puf.backend.network;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SocketRequest.class)
public class SocketRequest {
    /**
     * The RequestType instance.
     */
    private SocketRequestType requestType;

    /**
     * The request's value.
     */
    private String value;

    /**
     * Constructs a SocketRequest object with the provided request type and
     * value.
     *
     * @param requestType the request's type
     * @param value       the request's value
     */
    public SocketRequest(SocketRequestType requestType, String value) {
        this.requestType = requestType;
        this.value = value;
    }

    /**
     * Gets the request type.
     *
     * @return the RequestType instance
     */
    public SocketRequestType getRequestType() {
        return requestType;
    }

    /**
     * Sets the request type.
     *
     * @param requestType the RequestType instance
     */
    public void setRequestType(SocketRequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * Gets the request value.
     *
     * @return a String representation of the request's value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the request value.
     *
     * @param value a String representation of the request's value
     */
    public void setValue(String value) {
        this.value = value;
    }
}

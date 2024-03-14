/************************************************
 *
 * Author: Ryan Huntington
 * Assignment: Program 4
 * Class: CSI 4321
 *
 ************************************************/
package jack.serialization;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Represents a New message
 */
public class New extends Message {
    /**
     * operation code for New message
     */
    public static final String OPERATION = "N";
    private final Service service;

    /**
     * Creates a New message from given values
     *
     * @param host the host ID
     * @param port the port
     * @throws IllegalArgumentException if any validation problem with host and/or port, including null, etc.
     */
    public New(String host, int port) throws IllegalArgumentException {
        this.service = new Service(host, port);

    }

    /**
     * Get the host
     *
     * @return the host
     */
    public String getHost() {
        return this.service.getHost();
    }

    /**
     * Set the host
     *
     * @param host the new host value
     * @return this object with new value
     */
    public New setHost(String host) {
        this.service.setHost(host);
        return this;
    }

    /**
     * Get the port
     *
     * @return the port
     */
    public int getPort() {
        return this.service.getPort();
    }

    /**
     * Set the port
     *
     * @param port the new port value
     * @return this object with new value
     */
    public New setPort(int port) {
        this.service.setPort(port);
        return this;
    }

    /**
     * Serialize the message.
     * @return serialized message
     */
    @Override
    public byte[] encode() {
        String string = this.getOperation().charAt(0) + " " + this.service.toString();
        return string.getBytes(StandardCharsets.US_ASCII);
    }

    /**
     * Returns string of the form NEW [name:port]
     *
     * @return the string representation of this object
     */
    @Override
    public String toString() {
        return "NEW " + '[' + this.service + ']';
    }

    /**
     * Get the operation.
     * @return operation
     */
    @Override
    public String getOperation() {
        return OPERATION;
    }

    /**
     * test equivalency
     * @param o other object
     * @return if true
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        New aNew = (New) o;
        return Objects.equals(service, aNew.service);
    }

    /**
     * generate hashcode
     * @return if the same
     */
    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}


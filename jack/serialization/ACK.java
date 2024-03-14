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
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Represents an ACK message
 *
 *  @version 1.0
 */
public class ACK extends Message {
    /**
     * Operation for ACK message
     */
    public static final String OPERATION = "A";
    private final Service service;

    /**
     * Create an ACK message from given values.
     * @param host host ID
     * @param port port
     * @throws IllegalArgumentException if any validation problem with host and/or port, including null, etc.
     */
    public ACK(String host, int port) throws IllegalArgumentException {
        this.service = new Service(host, port);
    }
    /**
     * Get the host.
     * @return the host
     */
    public String getHost() {
        return this.service.getHost();
    }
    /**
     * Set the host.
     * @param host new host value
     * @return this object with new value
     */
    public ACK setHost(String host) throws IllegalArgumentException{
        this.service.setHost(host);
        return this;
    }
    /**
     * Get the port.
     * @return the port
     */
    public int getPort() {
        return this.service.getPort();
    }
    /**
     * Set the port.
     * @param port the port
     * @return this object with new value
     */
    public ACK setPort(int port) throws IllegalArgumentException{
        this.service.setPort(port);
        return this;
    }
    /**
     * Returns string of the form ACK [name:port].
     * For example: ACK [google.com:8080].
     * @return string representation of the object
     */
    @Override
    public String toString() {
        return  "ACK [" + this.service + ']';
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
     * Get the operation.
     * @return operation
     */
    @Override
    public String getOperation() {
        return OPERATION;
    }



    /**
     * determines if object is equal
     * @param o other object
     * @return if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ACK ack = (ACK) o;
        return Objects.equals(service, ack.service);
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
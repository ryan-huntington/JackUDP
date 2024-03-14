/************************************************
 *
 * Author: Ryan Huntington
 * Assignment: Program 4
 * Class: CSI 4321
 *
 ************************************************/
package jack.serialization;

import java.util.Objects;

/**
 * Identifies a service by host and port
 * @version 1.0
 */
public class Service {
    //makes sense, I can do this.
    private static final int MAX_PORT = 65535;
    private String host;
    private int port;

    /**
     * Creates a service ID
     * @param host the service host
     * @param port the service port
     * @throws IllegalArgumentException if invalid host or port
     */
    public Service(String host, int port) throws IllegalArgumentException {
        setHost(host);
        setPort(port);
    }

    /**
     * Set the service host
     * @param host the service host
     * @return this object with new value
     * @throws IllegalArgumentException if invalid host
     */
    public Service setHost(String host) throws IllegalArgumentException {
        if(host == null || host.isBlank() || !this.validateName(host)){
            throw new IllegalArgumentException("invalid host name");
        }
        this.host = host;
        return this;
    }

    /**
     * Set the service port
     * @param port the service port
     * @throws IllegalArgumentException if invalid port
     */
    public void setPort(int port) throws IllegalArgumentException {
        if(!this.validatePort(port)){
            throw new IllegalArgumentException("invalid port");
        }
        this.port = port;
    }

    /**
     * Get the service host
     * @return the service host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Get the service port
     * @return the service port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Returns a string of the form name:port
     * @return a string representation of this object
     */
    public String toString() {
        return this.host + ':' + this.port;
    }

    /**
     * validates name
     * @param name host
     * @return if valid
     */
    private boolean validateName(String name){
        return name.length() >= 1 && name.chars().allMatch(c -> (c >= 'A' && c <= 'Z')
                || (c >= 'a' && c <= 'z')
                || (c >= '0' && c <= '9')
                || (c == '.')
                || (c == '-'));
    }

    /**
     * validate the port
     * @param port port
     * @return if valid
     */
    private boolean validatePort(int port){
        return port >= 1 && port <= MAX_PORT;
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
        Service service = (Service) o;
        return port == service.port && Objects.equals(host, service.host);
    }

    /**
     * generate hashcode
     * @return if the same
     */
    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}


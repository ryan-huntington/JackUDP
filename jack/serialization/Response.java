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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Response message.
 *
 * The list of services from any method (e.g., toString, encode, getServiceList, etc.) must not have
 * duplicates and be sorted by Java's default order using host/port as the primary/secondary sort key.
 *
 * Note that Response deserialization will accept unordered and/or duplicate host+port pairs
 */
public class Response extends Message {
    /**
     * Operation code for Response message
     */
    public static final String OPERATION = "R";
    private final List<Service> services;
    /**
     * Constructs a response with an empty host:port list.
     */
    public Response() {
        this.services = new ArrayList<>();
    }

    /**
     * Adds a service to the list. The list of services must be sorted by Java's default String order
     * for the String representation of a service (e.g., name:port). A duplicate host+port leaves
     * the list unchanged.
     *
     * @param host new service host
     * @param port new service port
     * @return this object with the new value
     * @throws IllegalArgumentException if validation fails, including null host
     */
    public final Response addService(String host, int port) throws IllegalArgumentException {
        Service service = new Service(host, port);

        if(!services.contains(service)){
            services.add(service);
            services.sort(Comparator.comparing(Service::getHost).thenComparingInt(Service::getPort));
        }

        return this;
    }

    /**
     * Returns the service (string representation) list where each service is represented as
     * name:port (e.g., google:8000).
     *
     * @return service list
     */
    public List<String> getServiceList() {
        return this.services.stream()
                .map(Service::toString)
                .collect(Collectors.toList());
    }


    /**
     * Returns a string of the form:
     * RESPONSE [name:port]*
     *
     * For example:
     * RESPONSE [fire:8000][wind:7000]
     *
     * @return string representation of the response
     */
    @Override
    public String toString() {
        return "RESPONSE " + this.getServiceList().stream()
                .map(service -> '[' + service + ']')
                .collect(Collectors.joining());
    }

    /**
     * Serialize the message.
     * @return serialized message
     */
    @Override
    public byte[] encode() {

        String string = this.getOperation().charAt(0) + " ";
        for(Service s : this.services){
            string += s.toString();
            string += " ";
        }

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
        Response response = (Response) o;
        return Objects.equals(services, response.services);
    }

    /**
     * generate hashcode
     * @return if the same
     */
    @Override
    public int hashCode() {
        return Objects.hash(services);
    }
}


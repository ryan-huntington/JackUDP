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
 * Represents a Query message that contains a search string.
 */
public class Query extends Message {
    /**
     * Operation for Query message
     */
    public static final String OPERATION = "Q";
    private String query;
    /**
     * Creates a Query message from the given search string.
     *
     * @param searchString the search string for the query
     * @throws IllegalArgumentException if the search string is null or empty
     */
    public Query(String searchString) throws IllegalArgumentException {
        this.setSearchString(searchString);
    }

    /**
     * Gets the search string of this Query message.
     *
     * @return the search string
     */
    public String getSearchString() {
        return this.query;
    }

    /**
     * Sets the search string of this Query message.
     *
     * @param searchString the new search string to set
     * @return this Query object with the new value
     * @throws IllegalArgumentException if the search string is null or empty
     */
    public final Query setSearchString(String searchString) throws IllegalArgumentException {
        if(!validateQuery(searchString)){
            throw new IllegalArgumentException("invalid query");
        }
        this.query = searchString;
        return this;
    }

    /**
     * Returns a string representation of this Query message.
     *
     * @return a string of the form "QUERY query"
     */
    @Override
    public String toString() {
        return "QUERY " + this.query;
    }

    /**
     * Serialize the message.
     * @return serialized message
     */
    @Override
    public byte[] encode() {
        String string = this.getOperation().charAt(0) + " " + this.query;
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
        Query query1 = (Query) o;
        return Objects.equals(query, query1.query);
    }

    /**
     * generate hashcode
     * @return if the same
     */
    @Override
    public int hashCode() {
        return Objects.hash(query);
    }

    /**
     * tests if the query is correct
     * @param query the string
     * @return if valid or not
     */
    private boolean validateQuery(String query){
        return query != null && !query.isBlank() && ((query.chars().allMatch(c -> (c >= 'A' && c <= 'Z')
                || (c >= 'a' && c <= 'z')
                || (c >= '0' && c <= '9')
                || (c == '.')
                || (c == '-'))) || query.equals("*")) ;
    }
}


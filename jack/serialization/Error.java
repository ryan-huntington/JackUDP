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
 * Represents an Error message
 *
 *  @version 1.0
 */
public class Error extends Message {
    /**
     * Operation for Error message
     */
    public static final String OPERATION = "E";
    private String errorMessage;

    /**
     * Create an Error message from given values.
     * @param errorMessage error message
     * @throws IllegalArgumentException if any validation problem with errorMessage, including null, etc.
     */
    public Error(String errorMessage) throws IllegalArgumentException {
        setErrorMessage(errorMessage);
    }
    /**
     * Get error message.
     * @return error message
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }
    /**
     * Set the error message.
     * @param errorMessage error message
     * @return this object with new value
     * @throws IllegalArgumentException if validation fails, including null
     */
    public final Error setErrorMessage(String errorMessage) throws IllegalArgumentException {
        if(!validateMessage(errorMessage)){
            throw new IllegalArgumentException("Illegal error message");
        }
        this.errorMessage = errorMessage;
        return this;
    }
    /**
     * Returns string of the form ERROR message.
     * For example: ERROR Bad stuff.
     * @return string representation of the object
     */
    @Override
    public String toString() {
        return "ERROR " + this.errorMessage;
    }

    /**
     * Serialize the message.
     * @return serialized message
     */
    @Override
    public byte[] encode() {
        String string = this.getOperation().charAt(0) + " " + this.errorMessage;
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
        Error error = (Error) o;
        return Objects.equals(errorMessage, error.errorMessage);
    }

    /**
     * generate hashcode
     * @return if the same
     */
    @Override
    public int hashCode() {
        return Objects.hash(errorMessage);
    }

    /**
     * ensures the message is valid
     * @param message message
     * @return if valid
     */
    private boolean validateMessage(String message){
        return message != null && message.length() >= 1 && validateBytes(message);
    }
}
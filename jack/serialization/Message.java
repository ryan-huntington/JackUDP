/************************************************
 *
 * Author: Ryan Huntington
 * Assignment: Program 4
 * Class: CSI 4321
 *
 ************************************************/
package jack.serialization;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Represents a Jack message.
 *
 *  @version 1.0
 */
public abstract class Message {
    /**
     * max size of UDP payload
     */
    public static final int MAX_SIZE = 65527;

    /**
     * default constructor (because I cant get rid of the error message on javadoc)
     */
    protected Message() {

    }

    /**
     *  Deserialize message from given bytes.
     *  @param msgBytes message bytes
     *  @return specific Message resulting from deserialization
     *  @throws IllegalArgumentException if validation fails, including null msgBytes
     */
    public static Message decode(byte[] msgBytes) throws IllegalArgumentException {
        if (msgBytes == null || msgBytes.length < 2 || msgBytes.length > MAX_SIZE){
            throw new IllegalArgumentException("Message invalid");
        }

        try {
            String op = new String(new byte[]{msgBytes[0]}, StandardCharsets.UTF_8);

            String payload = new String(msgBytes, 2, msgBytes.length - 2, StandardCharsets.US_ASCII);
            if(!validateBytes(op) || !validateBytes(payload) || msgBytes[1] != ' '){
                throw new IllegalArgumentException("Bytes are not valid");
            }
            Message msg;
            switch (op){
                case Query.OPERATION -> {
                    msg = new Query(payload);
                }
                case Response.OPERATION -> {
                    msg = new Response();
                    String[] services = payload.split(" ");
                    for(String s : services){
                        if(!s.isBlank()){
                            String[] fields = s.split(":");
                            if(fields.length != 2){
                                throw new IllegalArgumentException("Invalid payload on RESPONSE");
                            }

                            ((Response)msg).addService(fields[0], Integer.parseInt(fields[1]));
                        }

                    }
                }
                case New.OPERATION -> {
                    String[] fields = payload.split(":");
                    if(fields.length != 2){
                        throw new IllegalArgumentException("Invalid payload on NEW");
                    }
                    msg = new New(fields[0], Integer.parseInt(fields[1]));
                }
                case ACK.OPERATION -> {
                    String[] fields = payload.split(":");
                    if(fields.length != 2){
                        throw new IllegalArgumentException("Invalid payload on NEW");
                    }
                    msg = new ACK(fields[0], Integer.parseInt(fields[1]));
                }
                case Error.OPERATION -> {
                    msg = new Error(payload);
                }
                default -> throw new IllegalArgumentException("Invalid Op type");
            }
            return msg;
        }  catch(NumberFormatException e){
            throw new IllegalArgumentException("Error parsing port (invalid integer)");
        }
    }
    /**
     * Serialize the message.
     * @return serialized message
     */
    public abstract byte[] encode();
    /**
     * Get the operation.
     * @return operation
     */
    public abstract String getOperation();


    /**
     * validates that string is within valid range
     * @param s string
     * @return if valid
     */
    protected static boolean validateBytes(String s){
        return s.chars().allMatch(c -> c >= 32 && c <= 127);
    }

}

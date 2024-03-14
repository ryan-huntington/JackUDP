/************************************************
 *
 * Author: Ryan Huntington
 * Assignment: Program 5
 * Class: CSI 4321
 *
 ************************************************/
package jack.app.client;

import jack.serialization.*;
import jack.serialization.Error;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * sends requests to server
 */
public class Client {

    private static final int MAX_UDP = 65507;
    private static final int TIMEOUT = 3000; // Resend timeout (milliseconds)
    private static final int MAXTRIES = 3; // Maximum retransmissions

    /**
     * no constructor
     */
    private Client(){

    }

    /**
     * runs the main client
     * @param args command line variables
     */
    public static void main(String[] args)  {

        if (args.length < 3) { // Test for correct # of args
            System.err.println("Bad Parameters: <Server> <Port> <Op> <Payload>");
            return;
        }

        InetAddress serverAddress;
        try{
            serverAddress = InetAddress.getByName(args[0]); // Server address

        } catch (IOException e){
            System.out.println("Bad Parameters: invalid server name");
            return;
        }


        int servPort = 0;
        try{
            servPort = Integer.parseInt(args[1]); // Server port
        }catch(NumberFormatException e){
            System.err.println("Bad parameters: Invalid port. Use numbers. ");
            return;
        }

        // Create a StringBuilder to concatenate the arguments with spaces
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]);
            sb.append(" ");
        }
        // Remove the trailing space
        if(args.length > 3 && sb.charAt(0) != 'R'){
            sb.setLength(sb.length() - 1);
        }
        if(args.length > 4 && sb.charAt(0) != 'R'){
            System.err.println("Bad Parameters: Too many values");
            return;
        }

        // Convert the StringBuilder to a byte array
        byte[] byteArray = sb.toString().getBytes();
        try{
            Message msg = Message.decode(byteArray);
            DatagramSocket socket;
            try{
                socket = new DatagramSocket();
                socket.setSoTimeout(TIMEOUT); // Maximum receive blocking time (milliseconds)
            } catch (IOException e){
                System.err.println("Communication problem: error creating socket");
                return;
            }


            DatagramPacket sendPacket = new DatagramPacket(byteArray, // Sending packet
                    byteArray.length, serverAddress, servPort);

            byte[] buffer = new byte[MAX_UDP];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);


            int tries = 0; // Packets may be lost, so we have to keep trying
            boolean done = false;
            do {
                socket.send(sendPacket);
                try {
                    socket.receive(receivePacket);

                    if (!receivePacket.getAddress().equals(serverAddress) || receivePacket.getPort() != servPort ){// Check source
                        System.err.println("Unexpected message source: packet received from unknown server");
                        tries++;
                    }
                    else{
                        byte[] data = new byte[receivePacket.getLength()];
                        System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());

                        done = handleMessage(msg, data);
                        if(!done){
                            tries++;
                        }
                    }

                } catch (InterruptedIOException e) { // We did not get anything
                    tries += 1;
                    System.err.println("Nothing received. " + (MAXTRIES - tries) + " tries left. ");

                } catch(IOException e){
                    System.err.println("Communication problem: IO error");
                    tries++;
                }
            } while ((!done) && (tries < MAXTRIES));

            if(tries == MAXTRIES){
                System.err.println("Reached maximum retransmit times");
            }

            socket.close();

        } catch (IllegalArgumentException e){
            System.err.println("Bad parameters: " + e.getMessage());
        } catch (IOException e){
            System.err.println("Communication Problem: error sending packet");
        }
        
    }


    /**
     * handles the data received by teh server if valid
     * @param msg message the client sent
     * @param data the data from the server
     * @return if the client is expecting more from server or not
     */
    private static boolean handleMessage(Message msg, byte[] data){
        boolean done = false;

        try{
            Message received = Message.decode(data);
            String op = received.getOperation();
            switch (op){
                case ACK.OPERATION -> {
                    if(msg.getOperation().equals(New.OPERATION)){
                        if(((ACK)received).getHost().equals(((New)msg).getHost()) &&  ((ACK)received).getPort() == ((New)msg).getPort()){
                            System.out.println(received);
                            done = true;
                        }
                        else{
                            System.err.println("Unexpected ACK");
                        }
                    }else{
                        System.err.println("Unexpected ACK");
                    }

                } case Response.OPERATION -> {
                    if(msg.getOperation().equals(Query.OPERATION)){
                        //the response is the answer
                        System.out.println(received);
                        done = true;
                    } else{
                        System.err.println("Unexpected Response");

                    }

                } case Error.OPERATION -> {
                    System.err.println(((Error)received).getErrorMessage());
                    done = true;
                }
                default -> {
                    System.err.println("Unexpected message type: " + received);
                }

            }
        } catch (IllegalArgumentException e){
            System.err.println("Invalid message: " + e.getMessage());
        }
        return done;
    }
}


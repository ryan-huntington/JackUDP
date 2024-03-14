/************************************************
 *
 * Author: Ryan Huntington
 * Assignment: Program 6
 * Class: CSI 4321
 *
 ************************************************/
package jack.app.server;

import jack.serialization.*;
import jack.serialization.Error;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * handles messages received by client
 */
public class Server {
    private static final Logger LOGGER = Logger.getLogger(megex.app.server.Server.class.getName());
    private static final int MAX_UDP = 65507; // Maximum size of udp datagram
    private static final Response services = new Response();
    private static DatagramSocket socket;
    private static DatagramPacket packet;

    /**
     * no constructor
     */
    private Server(){

    }

    /**
     * main class to run server
     * @param args port passed in
     * @throws IOException
     * if error setting up server
     */
    public static void main(String[] args) throws IOException {
        LogManager logManager = LogManager.getLogManager();
        //src/main/java/
        try (FileInputStream configFile = new FileInputStream("log.properties")) {
            logManager.readConfiguration(configFile);
        } catch (IOException ex) {
            LOGGER.severe("Failed to load logging configuration: " + ex.getMessage());
            System.exit(1);
        }

        if (args.length != 1) { // Test for correct # of args
            LOGGER.severe("Invalid args on startup. Expected: <port>");
            System.exit(1);
        }


        int servPort = 0;
        try{
            servPort = Integer.parseInt(args[0]); // Server port
        }catch(NumberFormatException e){
            LOGGER.log(Level.SEVERE, "Error: Invalid port. Use numbers. ");
            System.exit(1);
        }

        socket = new DatagramSocket(servPort);
        packet = new DatagramPacket(new byte[MAX_UDP], MAX_UDP);




        while (true) { // Run forever, receiving and echoing datagrams

            byte[] data = new byte[0];
            try {
                socket.receive(packet); // Receive packet from client
                LOGGER.log(Level.INFO, "Handling client at " + packet.getAddress().getHostAddress()
                        + " on port " + packet.getPort());

                data = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());

                Message msg = Message.decode(data);
                handleMessage(msg);

            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Communication problem: IO error");
            } catch (IllegalArgumentException e) {
                String message = "Invalid message: " + e.getMessage();
                LOGGER.log(Level.WARNING, message);
                Error error = new Error(message);
                sendPacket(error);

            }

            packet.setLength(MAX_UDP); // Reset length to avoid shrinking buffer
        }
        /* NOT REACHED */
    }

    /**
     * used to send packets to client
     * @param msg message to be sent
     * @throws IOException
     * if error sending
     */
    private static void sendPacket(Message msg) throws IOException {
        byte[] byteArray = msg.encode();
        InetAddress clientAddress = InetAddress.getByName(packet.getAddress().getHostAddress());
        DatagramPacket sendPacket = new DatagramPacket(byteArray, // Sending packet
                byteArray.length, clientAddress, packet.getPort());

        socket.send(sendPacket);
    }

    /**
     * handles the message received from the client and send correct message
     * @param msg message received from client
     * @throws IllegalArgumentException if problem creating message
     * @throws IOException if error sending packet
     */
    private static void handleMessage(Message msg) throws IllegalArgumentException, IOException {
        Message sendMsg;
        switch (msg.getOperation()){
            case Query.OPERATION -> {
                LOGGER.log(Level.INFO, msg.toString());
                String query = ((Query)msg).getSearchString();
                if(query.equals("*")){
                    sendMsg = services;
                }
                else{
                    List<String> filter = filterStringList(services.getServiceList(), query);
                    Response newService = new Response();
                    for(String s : filter){
                        String[] fields = s.split(":");
                        newService.addService(fields[0], Integer.parseInt(fields[1]));
                    }
                    sendMsg = newService;
                }
                LOGGER.log(Level.INFO, sendMsg.toString());

            } case New.OPERATION -> {
                LOGGER.log(Level.INFO, msg.toString());
                sendMsg = new ACK(((New)msg).getHost(), ((New)msg).getPort());
                LOGGER.log(Level.INFO, sendMsg.toString());
                services.addService(((New)msg).getHost(), ((New)msg).getPort());

            }default -> {
                String error = "Unexpected message type: " + msg;
                sendMsg = new Error(error);
                LOGGER.log(Level.INFO,  sendMsg.toString());


            }

        }
        sendPacket(sendMsg);
    }

    /**
     * filter the responses of the server when receive Q
     * @param inputList list of services provided
     * @param filterString query
     * @return the filtered list of services
     */
    private static List<String> filterStringList(List<String> inputList, String filterString) {
        List<String> filteredList = new ArrayList<>();

        // Loop through each string in the input list
        for (String inputString : inputList) {
            // Split the string into <name> and <port> using ":" as the delimiter
            String[] parts = inputString.split(":");
            String name = parts[0]; // Extract <name> from the split parts

            // Check if <name> contains the filter string
            if (name.contains(filterString)) {
                filteredList.add(inputString);
            }
        }

        return filteredList;
    }
}

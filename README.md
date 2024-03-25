# JackUDP

This Java project implements a UDP (User Datagram Protocol) server and client, allowing for lightweight and connectionless communication between two endpoints.

## Features:

- **UDP Server:** The server component of the project listens on a specified port for incoming UDP datagrams. It accepts datagrams from clients, processes them, and sends back appropriate responses. The server operates in a connectionless manner, handling each datagram independently.

- **UDP Client:** The client component initiates communication with the server by sending UDP datagrams containing a specified operation and payload. It then waits for a response from the server. Similar to the server, the client operates in a connectionless manner, sending and receiving datagrams without establishing a persistent connection.

## Usage:

### Server:

To run the UDP server, use the following command:
`java Server <port>`

- `<port>`: The port number on which the server will listen for incoming UDP datagrams.

### Client:
To run the UDP client, use the following command:
`java Client <Server> <Port> <Op> <Payload>`

- `<Server>`: The IP address or hostname of the UDP server.
- `<Port>`: The port number on which the UDP server is listening.
- `<Op>`: The operation to be performed by the server.
- `<Payload>`: The payload or data to be sent to the server.

  ## Dependencies:

- **Java:** The program is written in Java and requires a Java Runtime Environment (JRE) to execute.

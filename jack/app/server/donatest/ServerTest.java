package jack.app.server.donatest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import jack.serialization.Error;
import jack.serialization.Message;
import jack.serialization.New;
import jack.serialization.Query;
import jack.serialization.Response;
import jack.serialization.ACK;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerTest {

  private static final int SRVRPORT = 3000;
  private static final int PKTMAX = 65507;
  private static final Charset CHARENC = StandardCharsets.US_ASCII;
  protected static final Duration DECODETIMEOUT = Duration.ofMillis(750);

  private DatagramSocket socket;
  private int port;
  private InetAddress server;

  public ServerTest() throws IOException {
    // Determine server identity and port
    port = System.getProperty("port") == null ? SRVRPORT : Integer.parseInt(System.getProperty("port"));
    server = (InetAddress) InetAddress
        .getByName(System.getProperty("server") == null ? "localhost" : System.getProperty("server"));
    socket = new DatagramSocket();
    socket.connect(server, port);
  }

  @AfterEach
  public void teardown() throws IOException {
    socket.close();
  }

  @Test
  @Order(1)
  // Pre: Empty
  // Post: Empty
  public void testEmptyList() throws IOException {
    testSend(new Query("foo.com"), new Response(), 0);
  }

  protected static Response state = new Response();

  @Test
  @Order(2)
  // Post: [foo.com:50][www.foo.com:5000]
  public void testBasicList() throws IOException {
    // Add foo.com
    testSend(new New("foo.com", 50), new ACK("foo.com", 50), 0);

    // Query for non-existing host
    testSend(new Query("fob.com"), new Response(), 0);

    // Query for non-existing host
    testSend(new Query("5"), new Response(), 0);

    // Query for existing host
    state.addService("foo.com", 50);
    testSend(new Query("oo.com"), state, 0);

    // Add new host
    testSend(new New("www.foo.com", 5000), new ACK("www.foo.com", 5000), 0);
    state.addService("www.foo.com", 5000);
    testSend(new Query("foo.com"), state, 0);
  }

  @Test
  @Order(3)
  // Post: [foo.com:50][www.foo.com:5000]
  public void testDuplicate() throws IOException {
    // Add duplicate foo.com
    testSend(new New("foo.com", 50), new ACK("foo.com", 50), 0);

    // Query for existing host
    testSend(new Query("foo.com"), state, 0);
  }

  @Test
  @Order(4)
  // Post: [foo.com:50][www.foo.com:5000]
  public void testBadMessage() throws IOException {
    Query r = new Query("foo.com");
    byte[] rBuf = r.encode();
    rBuf[0] = 'X';
    sendPacket(rBuf);
    Error msg = (Error) receivePacket();
    assertTrue(msg.getErrorMessage().startsWith("Invalid message"));
  }

  @Test
  @Order(5)
  // Post: [foo.com:50][www.foo.com:5000]
  public void testBadPayloadName() throws IOException {
    New r = new New("X", 4000);
    String s = new String(r.encode(), CHARENC).replace('X', '^');
    sendPacket(s.getBytes(CHARENC));
    Error msg = (Error) receivePacket();
    assertTrue(msg.getErrorMessage().startsWith("Invalid message"));
  }

  @Test
  @Order(6)
  // Post: [foo.com:50][www.foo.com:5000]
  public void testBadPayloadSeparator() throws IOException {
    New r = new New("foo.com", 4000);
    String s = new String(r.encode(), CHARENC).replace(':', '-');
    sendPacket(s.getBytes(CHARENC));
    Error msg = (Error) receivePacket();
    assertTrue(msg.getErrorMessage().startsWith("Invalid message"));
  }

  @Test
  @Order(7)
  // Post: [foo.com:50][www.foo.com:5000]
  public void testUnexpectedType() throws IOException {
    Response r = new Response();
    sendPacket(r.encode());
    Error msg = (Error) receivePacket();
    assertTrue(msg.getErrorMessage().startsWith("Unexpected message type"));
  }

  @Test
  @Order(8)
  // Post: [foo.com:50][www.foo.com:5000][zz...zz:50]
  public void testLong() throws IOException {
    final String LONGNAME = "z".repeat(65495);
    final int PORT = 50;
    // Add foo.com
    testSend(new New(LONGNAME, PORT), new ACK(LONGNAME, PORT), 0);

    // Query for non-existing host
    testSend(new Query("z"), new Response().addService(LONGNAME, PORT), 0);
  }

  private void testSend(Message send, Message expMsg, int extraByteCt) throws IOException {
    sendPacket(Arrays.copyOf(send.encode(), send.encode().length + extraByteCt));
    Message rcvMsg = receivePacket();
    assertEquals(expMsg, rcvMsg);
  }

  /*
   * Utility functions
   */
  protected void sendMessage(Message pkt) {
    sendPacket(pkt.encode());
  }

  protected void sendPacket(byte[] sndBuffer) {
    if (sndBuffer != null) {
      DatagramPacket sndDatagram = new DatagramPacket(sndBuffer, sndBuffer.length);
      try {
        socket.send(sndDatagram);
      } catch (IOException e) {
        System.err.println("Unable to send: " + e.getMessage());
      }
    }
  }

  protected Message receivePacket() throws IOException {
    DatagramPacket rcvDatagram = new DatagramPacket(new byte[PKTMAX], PKTMAX);

    assertTimeoutPreemptively(DECODETIMEOUT, () -> {
      socket.receive(rcvDatagram); // Receive packet from client
    });

    // Copy valid subset of buffer bytes and decode
    byte[] rcvBuffer = Arrays.copyOf(rcvDatagram.getData(), rcvDatagram.getLength());

    return Message.decode(rcvBuffer);
  }
}

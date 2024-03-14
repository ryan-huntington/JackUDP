/************************************************
 *
 * Author: Ryan Huntington
 * Assignment: Program 4
 * Class: CSI 4321
 *
 ************************************************/
package jack.serialization.test;

import jack.serialization.*;
import jack.serialization.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class
 */
public class JackTest {
    /**
     * default
     */
    private JackTest(){

    }

    /**
     * tests service class
     */
    @Nested
    public class ServiceTests {
        /**
         * default
         */
        private ServiceTests(){

        }
        Service service = new Service("name", 4);

        /**
         * tests creation and setters
         */
        @DisplayName("Test creation and setters")
        @Test
        public void testHost(){
            String validName = "validName";
            String invalid = "not valid";
            String validResponse = "*";
            String empty = "";
            String nullString = null;
            int validNum = 55;
            int invalidNum = 65536;

            assertDoesNotThrow(() -> new Service(validName, validNum));
            assertDoesNotThrow(() -> service.setHost(validName));
            assertDoesNotThrow(() -> service.setPort(validNum));
            assertThrows(IllegalArgumentException.class, () -> new Service(invalid, validNum));
            assertThrows(IllegalArgumentException.class, () -> new Service(validName, invalidNum));
            assertThrows(IllegalArgumentException.class, () -> new Service(validResponse, validNum));
            assertThrows(IllegalArgumentException.class, () -> new Service(empty, validNum));
            assertThrows(IllegalArgumentException.class, () -> new Service(nullString, validNum));
            assertThrows(IllegalArgumentException.class, () -> service.setHost(invalid));
            assertThrows(IllegalArgumentException.class, () -> service.setHost(validResponse));
            assertThrows(IllegalArgumentException.class, () -> service.setHost(empty));
            assertThrows(IllegalArgumentException.class, () -> service.setHost(nullString));
            assertThrows(IllegalArgumentException.class, () -> service.setPort(invalidNum));

        }

        /**
         * test setters for service
         */
        @DisplayName("test setters")
        @Test
        public void testGetters(){
            Service s = new Service("name", 1);
            Service s2 = s.setHost("newName");

            assertEquals(s, s2);
            assertEquals(s, s);

            Service s1 = new Service("name", 1);
            s1.setPort(2);

            assertEquals(s1, s1);
        }

        /**
         * tests the toString
         */
        @DisplayName("test ToString")
        @Test
        public void testString(){
            Service s = new Service("host", 1);
            String string = "host:1";

            assertEquals(string, s.toString());
        }
    }

    /**
     * tests the ACK class
     */
    @Nested
    public class ACKTests {
        /**
         * default
         */
        private ACKTests(){

        }
        /**
         * tests ack toString
         */
        @DisplayName("test ToString")
        @Test
        public void testString(){
            ACK s = new ACK("host", 1);
            String string = "ACK [host:1]";

            assertEquals(string, s.toString());
        }

        /**
         * tests encode
         */
        @DisplayName("test Encode")
        @Test
        public void testEncode(){
            ACK a = new ACK("host", 1);
            byte[] data = a.encode();
            byte[] expected = {'A', ' ', 'h', 'o', 's', 't', ':', '1'};

            assertArrayEquals(expected, data);
        }

        /**
         * tests decode
         */
        @DisplayName("test ack decode")
        @Test
        public void testDecode(){
            ACK a = new ACK("host", 1);
            byte[] expected = {'A', ' ', 'h', 'o', 's', 't', ':', '1'};
            Message msg = Message.decode(expected);
            assertEquals(a, msg);

        }
    }

    /**
     * tests error class
     */
    @Nested
    public class ErrorTests {
        /**
         * default
         */
        private ErrorTests(){

        }
        /**
         * test toString
         */
        @DisplayName("test ToString")
        @Test
        public void testString(){
            Message error = new Error("error message");
            String string = "ERROR error message";

            assertEquals(string, error.toString());
        }

        /**
         * tests encode
         */
        @DisplayName("test Encode")
        @Test
        public void testEncode(){
            Message error = new Error("error ");
            byte[] data = error.encode();
            byte[] expected = {'E', ' ', 'e', 'r', 'r', 'o', 'r', ' '};

            assertArrayEquals(expected, data);
        }

        /**
         * tests decode
         */
        @DisplayName("test ack decode")
        @Test
        public void testDecode(){
            Message error = new Error("error ");
            byte[] expected = {'E', ' ', 'e', 'r', 'r', 'o', 'r', ' '};
            Message msg = Message.decode(expected);
            assertEquals(error, msg);

        }

        /**
         * tests specific setters
         */
        @DisplayName("test setters")
        @Test
        public void testSetter(){
            Error q = new Error("string");
            byte[] wrong = {0x10, 0x0a, 0x2, 31};
            String s = new String(wrong);
            assertThrows(IllegalArgumentException.class, () -> q.setErrorMessage(""));
            assertThrows(IllegalArgumentException.class, () -> q.setErrorMessage(s));

        }

        /**
         * verify that there is a space
         */
        @DisplayName("DonTest")
        @Test
        public void donTest(){
            byte[] data = {69, 66, 97, 100, 32, 83, 116, 117, 102, 102};
            byte[] data2 = {69, 82, 82, 79, 82, 32, 102, 111, 111, 46, 99, 111, 109, 58, 49, 48};
            assertThrows(IllegalArgumentException.class, () -> Message.decode(data));
            assertThrows(IllegalArgumentException.class, () -> Message.decode(data2));

        }
    }

    /**
     * tests new class
     */
    @Nested
    public class NewTests {
        /**
         * default
         */
        private NewTests(){

        }
        /**
         * tests toString
         */
        @DisplayName("test ToString")
        @Test
        public void testString(){
            New s = new New("host", 1);
            String string = "NEW [host:1]";

            assertEquals(string, s.toString());
        }

        /**
         * tests encode
         */
        @DisplayName("test Encode")
        @Test
        public void testEncode(){
            New a = new New("host", 1);
            byte[] data = a.encode();
            byte[] expected = {'N', ' ', 'h', 'o', 's', 't', ':', '1'};

            assertArrayEquals(expected, data);
        }

        /**
         * tests decode
         */
        @DisplayName("test ack decode")
        @Test
        public void testDecode(){
            New a = new New("host", 1);
            byte[] expected = {'N', ' ', 'h', 'o', 's', 't', ':', '1'};
            Message msg = Message.decode(expected);
            assertEquals(a, msg);

        }
    }

    /**
     * tests query class
     */
    @Nested
    public class QueryTests {
        /**
         * default
         */
        private QueryTests(){

        }
        /**
         * tests toString
         */
        @DisplayName("test ToString")
        @Test
        public void testString(){
            Query s = new Query("host");
            String string = "QUERY host";

            assertEquals(string, s.toString());
        }

        /**
         * tests encode
         */
        @DisplayName("test Encode")
        @Test
        public void testEncode(){
            Query a = new Query("host");
            byte[] data = a.encode();
            byte[] expected = {'Q', ' ', 'h', 'o', 's', 't'};

            assertArrayEquals(expected, data);

            Query a1 = new Query("*");
            byte[] data1 = a1.encode();
            byte[] expected1 = {'Q', ' ', '*'};
            assertArrayEquals(expected1, data1);
        }

        /**
         * tests decode
         */
        @DisplayName("test ack decode")
        @Test
        public void testDecode(){
            Query a = new Query("host");
            byte[] expected = {'Q', ' ', 'h', 'o', 's', 't'};
            Message msg = Message.decode(expected);
            assertEquals(a, msg);

        }

        /**
         * tests specific setters
         */
        @DisplayName("test setters")
        @Test
        public void testSetter(){
            Query q = new Query("string");
            byte[] wrong = {0x10, 0x0a, 0x2, 31};
            String s = new String(wrong);
            assertThrows(IllegalArgumentException.class, () -> q.setSearchString(""));
            assertThrows(IllegalArgumentException.class, () -> q.setSearchString("host " + 0x1));
            assertThrows(IllegalArgumentException.class, () -> q.setSearchString(s));
            assertDoesNotThrow(() -> q.setSearchString("*"));
            assertDoesNotThrow(() -> q.setSearchString("State"));
            assertDoesNotThrow(() -> q.setSearchString("A093"));

        }
    }

    /**
     * tests response class
     */
    @Nested
    public class ResponseTests {
        /**
         * default
         */
        private ResponseTests(){

        }
        /**
         * test toString
         */
        @DisplayName("test ToString")
        @Test
        public void testString(){
            Response s = new Response();
            s.addService("host", 1);
            s.addService("host", 1);
            String string = "RESPONSE [host:1]";

            assertEquals(string, s.toString());

            String string1 = "RESPONSE [host:1][host:2]";
            s.addService("host", 2);
            assertEquals(string1, s.toString());

            String string2 = "RESPONSE [aaaa:3][host:1][host:2]";
            s.addService("aaaa", 3);
            assertEquals(string2, s.toString());

        }

        /**
         * test encode
         */
        @DisplayName("test Encode")
        @Test
        public void testEncode(){
            Response a = new Response();
            a.addService("host", 1);
            byte[] data = a.encode();
            byte[] expected = {'R', ' ', 'h', 'o', 's', 't', ':', '1', ' '};

            assertArrayEquals(expected, data);
        }

        /**
         * test decode
         */
        @DisplayName("test ack decode")
        @Test
        public void testDecode(){
            Response a = new Response();
            a.addService("host", 1);
            byte[] expected = {'R', ' ', 'h', 'o', 's', 't', ':', '1', ' '};
            Message msg = Message.decode(expected);
            assertEquals(a, msg);

        }

        /**
         * short response (valid)
         */
        @DisplayName("don test")
        @Test
        public void donTest(){
            byte[] data = {82, 32};
            assertDoesNotThrow(() -> Message.decode(data));

        }
    }

}


import model.Consumer;
import model.Event;

import java.time.LocalDateTime;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import model.Booking;
import model.EventTagCollection;
import model.EventType;

@TestInstance(Lifecycle.PER_CLASS)
public class TestConsumer {
        // For testing notify
        private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        private final PrintStream originalOut = System.out;
        private final PrintStream originalErr = System.err;

        @BeforeAll
        public void setUpStreams() {
                System.setOut(new PrintStream(outContent));
                System.setErr(new PrintStream(errContent));
        }

        @AfterAll
        public void restoreStreams() {
                System.setOut(originalOut);
                System.setErr(originalErr);
        }

        // Test the constructor
        @Test
        public void testConstructor() {
                Consumer consumer = new Consumer("Daquille Jeffreys", "Daq@example.com", "123-456-7890",
                                "52 north street",
                                "password");
                assertEquals("Daquille Jeffreys", consumer.getName());
                assertEquals("Daq@example.com", consumer.getEmail());
                assertEquals("123-456-7890", consumer.getPhoneNumber());
                assertEquals("52 north street", consumer.getAddress());
                assertNotNull(consumer.getPreferences());
                assertNotNull(consumer.getBookings());
        }

        @Test
        public void testAddBooking() {
                Consumer consumer = new Consumer("Daquille Jeffreys", "Daq@example.com", "123-456-7890",
                                "52 north street",
                                "password");
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.now(), LocalDateTime.now().plusHours(1), new EventTagCollection());
                Booking booking = new Booking(Long.valueOf(10), consumer, event, 5, LocalDateTime.now());

                consumer.addBooking(booking);
                assertTrue(consumer.getBookings().contains(booking));
        }

        public void testAddMultipleBooking() {
                Consumer consumer = new Consumer("Daquille Jeffreys", "Daq@example.com", "123-456-7890",
                                "52 north street",
                                "password");
                Event event1 = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.now(), LocalDateTime.now().plusHours(1), new EventTagCollection());
                Booking booking1 = new Booking(Long.valueOf(10), consumer, event1, 5, LocalDateTime.now());

                Event event2 = new Event(456, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.now(), LocalDateTime.now().plusHours(1), new EventTagCollection());
                Booking booking2 = new Booking(Long.valueOf(10), consumer, event2, 5, LocalDateTime.now());

                consumer.addBooking(booking1);
                consumer.addBooking(booking2);
                assertEquals(2, consumer.getBookings().size());
                assertTrue(consumer.getBookings().contains(booking1));
                assertTrue(consumer.getBookings().contains(booking2));
        }

        @Test
        public void testGetName() {
                Consumer consumer = new Consumer("Daquille Jeffreys", "Daq@example.com", "123-456-7890",
                                "52 north street",
                                "password");
                assertEquals("Daquille Jeffreys", consumer.getName());
        }

        @Test
        public void testSetName() {
                Consumer consumer = new Consumer("Daquille Jeffreys", "Daq@example.com", "123-456-7890",
                                "52 north street",
                                "password");
                consumer.setName("Billy Boxer");
                assertEquals("Billy Boxer", consumer.getName());
        }

        @Test
        public void testSetAddress() {
                Consumer consumer = new Consumer("Daquille Jeffreys", "Daq@example.com", "123-456-7890",
                                "52 north street",
                                "password");
                consumer.setAddress("456 meadows lane");
                assertEquals("456 meadows lane", consumer.getAddress());
        }

        @Test
        public void testGetBookings() {
                Consumer consumer = new Consumer("Daquille Jeffreys", "Daq@example.com", "123-456-7890",
                                "52 north street",
                                "password");
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.now(), LocalDateTime.now().plusHours(1), new EventTagCollection());
                Booking booking = new Booking(Long.valueOf(10), consumer, event, 5, LocalDateTime.now());
                consumer.addBooking(booking);
                assertNotNull(consumer.getBookings());
        }

        @Test
        public void testGetAddress() {
                Consumer consumer = new Consumer("Daquille Jeffreys", "Daq@example.com", "123-456-7890",
                                "52 north street",
                                "password");
                assertEquals("52 north street", consumer.getAddress());
        }

        @Test
        public void testNotify() {
                Consumer consumer = new Consumer("Daquille Jeffreys", "Daq@example.com", "123-456-7890",
                                "52 north street",
                                "password");
                String message = "You're event has been cancelled.";
                consumer.notify(message);
                String expectedOutput = "Message to " + consumer.getEmail() + " and " + consumer.getPhoneNumber() + ": "
                                + message + "\n";
                assertEquals(expectedOutput, outContent.toString());
        }

        @Test
        public void testSetPhoneNumber() {
                Consumer consumer = new Consumer("Daquille Jeffreys", "Daq@example.com", "123-456-7890",
                                "52 north street",
                                "password");
                consumer.setPhoneNumber("565-781-340");
                assertEquals("565-781-340", consumer.getPhoneNumber());
        }
}
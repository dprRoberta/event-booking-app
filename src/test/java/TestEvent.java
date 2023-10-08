
import model.Event;
import model.EventStatus;
import model.EventTagCollection;
import model.EventType;
import model.Review;
import model.Consumer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestEvent {

        // public void testReviewsFieldIsEmptyAfterConstruction() {
        // Event event = new Event(123, "Mother of God", EventType.MUSIC, 100, 5000,
        // "Appopleton tower", "AJ is out playing at the new arena in AT.",
        // LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
        // new EventTagCollection());

        @Test
        public void testEventNotEmpty() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertNotNull(event);
        }

        @Test
        public void testEventNumber() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertEquals(event.getEventNumber(), 123);
        }

        @Test
        public void testTitle() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertEquals("Mother of God", event.getTitle());
        }

        @Test
        public void testType() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertEquals(EventType.Music, event.getType());
        }

        @Test
        public void testNumTicketsCap() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertEquals(100, event.getNumTicketsCap());
        }

        @Test
        public void testTicketPriceInPence() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertEquals(5000, event.getTicketPriceInPence());
        }

        @Test
        public void testVenueAddress() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertEquals("Appopleton tower", event.getVenueAddress());
        }

        @Test
        public void testDescription() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertEquals("AJ is out playing at the new arena in AT.", event.getDescription());
        }

        @Test
        public void testStartDateTime() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertEquals(LocalDateTime.of(2023, 4, 15, 20, 0), event.getStartDateTime());
        }

        @Test
        public void testEndDateTime() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertEquals(LocalDateTime.of(2023, 4, 15, 22, 0), event.getEndDateTime());
        }

        @Test
        public void testStatus() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertEquals(EventStatus.ACTIVE, event.getStatus());
        }

        @Test
        public void testNumTicketsLeft() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                assertEquals(100, event.getNumTicketsLeft());
        }

        @Test
        public void testReviewsFieldIsEmptyAfterConstruction() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());

                assertNotNull(event.getReviews());

                assertTrue(event.getReviews().isEmpty());
        }

        @Test
        public void testAdd1Review() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());

                Consumer testConsumer = new Consumer(
                                "Amy McDonald",
                                "test@mail.com",
                                "004499",
                                "Stockbridge, Edinburgh",
                                "123456");
                // Consumer author, Event event, LocalDateTime creationDateTime, String content
                Review review1 = new Review(testConsumer, event, LocalDateTime.of(2023, 4, 15, 20, 0),
                                "Great concert!");
                event.addReview(review1);

                List<Review> reviews = event.getReviews();

                assertEquals(1, reviews.size());
                assertEquals(review1, reviews.get(0));

        }

        @Test
        public void testAddMultipleReviews() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());
                Consumer testConsumer = new Consumer(
                                "Amy McDonald",
                                "test@mail.com",
                                "004499",
                                "Stockbridge, Edinburgh",
                                "123456");
                // Consumer author, Event event, LocalDateTime creationDateTime, String content
                Review review1 = new Review(testConsumer, event, LocalDateTime.of(2023, 4, 15, 20, 0),
                                "Great concert!");
                event.addReview(review1);

                List<Review> reviews = event.getReviews();

                assertEquals(1, reviews.size());
                assertEquals(review1, reviews.get(0));

                Review review2 = new Review(testConsumer, event, LocalDateTime.of(2023, 4, 15, 20, 0),
                                "Great concert * 2!");
                event.addReview(review2);

                reviews = event.getReviews();

                assertEquals(2, reviews.size());
                assertEquals(review1, reviews.get(0));
                assertEquals(review2, reviews.get(1));
        }

        @Test
        public void testCancel() {
                Event event = new Event(123, "Mother of God", EventType.Music, 100, 5000, "Appopleton tower",
                                "AJ is out playing at the new arena in AT.",
                                LocalDateTime.of(2023, 4, 15, 20, 0), LocalDateTime.of(2023, 4, 15, 22, 0),
                                new EventTagCollection());

                event.cancel();

                assertEquals(EventStatus.CANCELLED, event.getStatus());
        }
}

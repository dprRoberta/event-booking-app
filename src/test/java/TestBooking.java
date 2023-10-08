
import model.Event;
import model.EventTagCollection;
import model.EventType;
import model.Booking;
import model.BookingStatus;
import model.Consumer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

public class TestBooking {
  public static Booking createBooking() {
    Consumer consumer = new Consumer("John Doe", "johnd@google.com", "1234567890", "123 Fake St", "password");
    Event event = new Event(1, "Event 1", EventType.Sports, 100, 200, "123 Fake St", "This is a test event",
        LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), new EventTagCollection());

    Booking booking = new Booking(10, consumer, event, 1, LocalDateTime.now());
    return booking;
  }

  @Test
  public void testBasicBooking() {
    Booking booking = createBooking();

    assertNotNull(booking);
    assertEquals(10, booking.getBookingNumber());
    assertEquals(BookingStatus.Active, booking.getStatus());
    assertFalse(booking.isCancelled());
  }

  @Test
  public void testCancelBookingByConsumer() {
    Booking booking = createBooking();

    booking.cancelByConsumer();
    assertEquals(BookingStatus.CancelledByConsumer, booking.getStatus());
    assertTrue(booking.isCancelled());
  }

  @Test
  public void testCancelBookingByProvider() {
    Booking booking = createBooking();

    booking.cancelByProvider();
    assertEquals(BookingStatus.CancelledByProvider, booking.getStatus());
    assertTrue(booking.isCancelled());
  }

  @Test
  public void testCancelBookingThatHasAlreadyBeenCancelled() {
    Booking booking = createBooking();

    booking.cancelByProvider();
    booking.cancelByConsumer();
    assertEquals(BookingStatus.CancelledByConsumer, booking.getStatus());
    assertTrue(booking.isCancelled());
  }
}

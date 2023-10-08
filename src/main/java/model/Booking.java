package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * {@link Booking} represents a booking made by a {@link Consumer} for an
 * {@link Event}.
 */
public class Booking implements Serializable {
    private final long bookingNumber;
    private final Consumer booker;
    private final Event event;
    private final int numTickets;
    private final LocalDateTime bookingDateTime;
    private BookingStatus status;

    /**
     * @param bookingNumber   unique identifier for this booking
     * @param booker          the {@link Consumer} who made this booking
     * @param event           the {@link Event} this booking is for
     * @param numTickets      the number of booked tickets
     * @param bookingDateTime the date and time when this booking was made
     */
    public Booking(long bookingNumber,
            Consumer booker,
            Event event,
            int numTickets,
            LocalDateTime bookingDateTime) {
        this.status = BookingStatus.Active;
        this.booker = booker;
        this.event = event;
        this.bookingNumber = bookingNumber;
        this.numTickets = numTickets;
        this.bookingDateTime = bookingDateTime;
    }

    public long getBookingNumber() {
        return bookingNumber;
    }

    public Boolean isCancelled() {
        return status == BookingStatus.CancelledByConsumer || status == BookingStatus.CancelledByProvider;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Consumer getBooker() {
        return booker;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public int getNumTickets() {
        return numTickets;
    }

    /**
     * Sets the {@link #status} to {@link BookingStatus#CancelledByConsumer}.
     */
    public void cancelByConsumer() {
        this.status = BookingStatus.CancelledByConsumer;
    }

    /**
     * Sets the {@link #status} to {@link BookingStatus#CancelledByProvider}.
     */
    public void cancelByProvider() {
        this.status = BookingStatus.CancelledByProvider;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "status=" + status +
                ", bookingNumber=" + bookingNumber +
                ", booker=" + booker.getName() +
                ", event=" + event +
                ", numTickets=" + numTickets +
                ", bookingDateTime=" + bookingDateTime +
                '}';
    }

    // Required method for load command
    @Override
    public boolean equals(Object o) {
        // Object is equal to itself
        if (o == this) {
            return true;
        }

        // Check if object given is also an event
        if (!(o instanceof Booking)) {
            return false;
        }

        // Convert object to event so we can apply our check
        Booking otherBooking = (Booking) o;

        // Check fields are equal
        return this.status.equals(otherBooking.status) &&
                this.booker.equals(otherBooking.booker) &&
                this.event.equals(otherBooking.event) &&
                this.numTickets == otherBooking.numTickets &&
                this.bookingDateTime.equals(otherBooking.bookingDateTime);
    }

}

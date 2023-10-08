package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link Event} represents an event that can be booked by {@link Consumer}s.
 * Tickets can be free, but they are
 * required to attend, and there is a maximum cap on the number of tickets that
 * can be booked.
 */
public class Event implements Serializable {
    private final long eventNumber;
    private final String title;
    private final EventType type;
    private final int numTicketsCap;
    private final int ticketPriceInPence;
    private final String venueAddress;
    private final String description;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final EventTagCollection tags;

    private EventStatus status;
    private int numTicketsLeft;
    private List<Review> reviews;

    /**
     * Create a new Event with status = {@link EventStatus#ACTIVE}
     * 
     * @param eventNumber        unique event identifier
     * @param title              name of the event
     * @param type               type of the event
     * @param numTicketsCap      maximum number of tickets, initially all available
     *                           for booking
     * @param ticketPriceInPence price of each ticket in GBP pence
     * @param venueAddress       address where the performance will be taking place
     * @param description        additional details about the event, e.g., who the
     *                           performers in a concert will be
     *                           or if payment is required on entry in addition to
     *                           ticket booking
     * @param startDateTime      date and time when the performance will begin
     * @param endDateTime        date and time when the performance will end
     */
    public Event(long eventNumber,
            String title,
            EventType type,
            int numTicketsCap,
            int ticketPriceInPence,
            String venueAddress,
            String description,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            EventTagCollection tags) {
        this.eventNumber = eventNumber;
        this.title = title;
        this.type = type;
        this.numTicketsCap = numTicketsCap;
        this.ticketPriceInPence = ticketPriceInPence;
        this.venueAddress = venueAddress;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.tags = tags;
        this.status = EventStatus.ACTIVE;
        this.numTicketsLeft = numTicketsCap;

        this.reviews = new ArrayList<Review>();

    }

    public long getId() {
        return eventNumber;
    }

    /**
     * @return Number of the maximum cap of tickets which were initially available
     */
    public int getNumTicketsCap() {
        return numTicketsCap;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public int getNumTicketsLeft() {
        return numTicketsLeft;
    }

    public void setNumTicketsLeft(int numTicketsLeft) {
        this.numTicketsLeft = numTicketsLeft;
    }

    public int getTicketPriceInPence() {
        return ticketPriceInPence;
    }

    public long getEventNumber() {
        return eventNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public EventType getType() {
        return type;
    }

    public EventStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public EventTagCollection getTags() {
        return tags;
    }

    public Boolean hasEnded() {
        return endDateTime.isBefore(LocalDateTime.now());
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public String getDescription() {
        return description;
    }

    /**
     * Set {@link #status} to {@link EventStatus#CANCELLED}
     */
    public void cancel() {
        status = EventStatus.CANCELLED;
    }


    @Override
    public String toString() {
        return "Event{" +
                "eventNumber=" + eventNumber +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", numTicketsCap=" + numTicketsCap +
                ", ticketPriceInPence=" + ticketPriceInPence +
                ", venueAddress='" + venueAddress + '\'' +
                ", description='" + description + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", status=" + status +
                ", numTicketsLeft=" + numTicketsLeft +
                ", tags=" + tags +
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
        if (!(o instanceof Event)) {
            return false;
        }

        // Convert object to event so we can apply our check
        Event otherEvent = (Event) o;

        // Check fields are equal
        return this.title.equals(otherEvent.title) &&
                this.startDateTime.equals(otherEvent.startDateTime) &&
                this.endDateTime.equals(otherEvent.endDateTime) &&
                this.type.equals(otherEvent.type) &&
                this.numTicketsCap == otherEvent.numTicketsCap &&
                this.ticketPriceInPence == otherEvent.ticketPriceInPence &&
                this.venueAddress.equals(otherEvent.venueAddress) &&
                this.description.equals(otherEvent.description) &&
                this.status.equals(otherEvent.status) &&
                this.numTicketsLeft == otherEvent.numTicketsLeft &&
                this.tags.equals(otherEvent.tags);
    }

}

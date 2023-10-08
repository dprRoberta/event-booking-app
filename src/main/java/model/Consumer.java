package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link Consumer} represents a user of the application, who can browse
 * {@link Event}s and book {@link Event}s.
 */
public class Consumer extends User implements Serializable {
    private final List<Booking> bookings;
    private String name;
    private String phoneNumber;
    private String address;
    private EventTagCollection preferences;

    /**
     * Create a new Consumer with an empty list of bookings and default Covid-19
     * preferences
     *
     * @param name        full name of the Consumer
     * @param email       email address of the Consumer (used to log in to the
     *                    application and for event cancellation
     *                    notifications)
     * @param phoneNumber phone number of the Consumer (used for event cancellation
     *                    notifications)
     * @param address     address of the Consumer (optional)
     * @param password    password used to log in to the application
     */
    public Consumer(String name, String email, String phoneNumber, String address, String password) {
        super(email, password);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.preferences = new EventTagCollection();
        this.bookings = new LinkedList<>();
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public EventTagCollection getPreferences() {
        return preferences;
    }

    public void setPreferences(EventTagCollection preferences) {
        this.preferences = preferences;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Mock method: print out a message to STDOUT. A real implementation would send
     * an email and/or text to the
     * {@link Consumer}'s {@link #phoneNumber}.
     *
     * @param message message from an {@link Staff} regarding an event cancellation
     */
    public void notify(String message) {
        System.out.println("Message to " + getEmail() + " and " + phoneNumber + ": " + message);
    }

    public void setPhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "bookings=" + bookings +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", preferences=" + preferences +
                '}';
    }

    // Required methods for load command
    @Override
    public boolean equals(Object o) {
        // Object is equal to itself
        if (o == this) {
            return true;
        }

        // Check if object given is also a consumer
        if (!(o instanceof Consumer)) {
            return false;
        }

        // Convert object to consumer so we can apply our check
        Consumer otherConsumer = (Consumer) o;

        // Check all easy to check field:
        return this.name.equals(otherConsumer.name) &&
                this.address.equals(otherConsumer.address) &&
                this.phoneNumber.equals(otherConsumer.phoneNumber) &&
                this.preferences.equals(otherConsumer.preferences);
        // I don't check with password hash since this will be different in different
        // session
    }

}
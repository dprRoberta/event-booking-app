import command.*;
import controller.Context;
import controller.Controller;
import model.Booking;
import model.Consumer;

import model.Event;
import model.EventTagCollection;
import model.EventType;
import state.IBookingState;
import state.IEventState;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewEventSystemTests extends ConsoleTest {

    private static Event createEventHelper(Controller controller, LocalDateTime start, LocalDateTime end) {
        LocalDateTime timeNow = LocalDateTime.now();

        IEventState eventState = controller.getContext().getEventState();
        IBookingState bookingState = controller.getContext().getBookingState();

        Event event = eventState.createEvent("Event 1", EventType.Dance, 100, 100,
                "55.95407768858387, -3.187465919902889",
                "Ramen time", start, end, new EventTagCollection());
        createConsumer(controller);

        Consumer consumer = (Consumer) controller.getContext().getUserState().getCurrentUser();

        Booking booking = controller.getContext().getBookingState().createBooking(consumer, event, 1);
        consumer.addBooking(booking);

        return event;
    }

    @Test
    void reviewEventSuccess() {
        Controller controller = createController();
        LocalDateTime timeNow = LocalDateTime.now();

        Event event = createEventHelper(controller, timeNow.minusHours(2), timeNow.minusHours(1) );

        startOutputCapture();
        controller.runCommand(new ReviewEventCommand(event.getEventNumber(), "super fun event tbh"));

        stopOutputCaptureAndCompare(
                "REVIEW_EVENT_SUCCESS");
    }

    @Test
    void eventNotFound() {
        Controller controller = createController();
        startOutputCapture();
        controller.runCommand(new ReviewEventCommand(1, "super fun event tbh"));

        stopOutputCaptureAndCompare(
                "REVIEW_EVENT_EVENT_NOT_FOUND");
    }
    @Test
    void eventNotOver() {
        Controller controller = createController();
        LocalDateTime timeNow = LocalDateTime.now();
        Event event = createEventHelper(controller, timeNow.minusHours(1), timeNow.plusHours(1) );

        startOutputCapture();

        controller.runCommand(new ReviewEventCommand(1, "super fun event tbh"));

        stopOutputCaptureAndCompare(
                "REVIEW_EVENT_EVENT_NOT_OVER");
    }
    @Test
    void UserNotConsumer(){
        Controller controller = createController();
        LocalDateTime timeNow = LocalDateTime.now();

        Event event = createEventHelper(controller, timeNow.minusHours(2), timeNow.minusHours(1) );
        startOutputCapture();
        controller.getContext().getUserState().setCurrentUser(null);

        controller.runCommand(new ReviewEventCommand(event.getEventNumber(), "super fun event tbh"));

        stopOutputCaptureAndCompare(
                "REVIEW_EVENT_USER_NOT_CONSUMER");
    }
    @Test
    void consumerNoValidBooking(){
        Controller controller = createController();
        LocalDateTime timeNow = LocalDateTime.now();

        IEventState eventState = controller.getContext().getEventState();
        IBookingState bookingState = controller.getContext().getBookingState();

        Event event = eventState.createEvent("Event 1", EventType.Dance, 100, 100,
                "55.95407768858387, -3.187465919902889",
                "Ramen time", timeNow.minusHours(2), timeNow.minusHours(1), new EventTagCollection());
        createConsumer(controller);

        startOutputCapture();


        controller.runCommand(new ReviewEventCommand(event.getEventNumber(), "super fun event tbh"));

        stopOutputCaptureAndCompare(
                "REVIEW_EVENT_CONSUMER_NO_VALID_BOOKING");

    }
}
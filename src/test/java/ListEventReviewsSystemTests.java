
import command.ListEventReviewsCommand;
import command.ReviewEventCommand;
import controller.Controller;
import model.*;
import org.junit.jupiter.api.Test;
import state.IBookingState;
import state.IEventState;

import java.time.LocalDateTime;

public class ListEventReviewsSystemTests extends ConsoleTest {
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
    void zeroReviewsLeft() {
        Controller controller = createController();
        LocalDateTime timeNow = LocalDateTime.now();

        Event event = createEventHelper(controller, timeNow.minusHours(2), timeNow.minusHours(1));
        startOutputCapture();
        ListEventReviewsCommand ListRevsCmd = new ListEventReviewsCommand(
                "Event 1"

        );
        controller.runCommand(ListRevsCmd);
        stopOutputCaptureAndCompare("LIST_EVENT_REVIEWS_SUCCESS");
    }

    @Test
    void oneReviewLeft() {
        Controller controller = createController();
        LocalDateTime timeNow = LocalDateTime.now();

        Event event = createEventHelper(controller, timeNow.minusHours(2), timeNow.minusHours(1));

        ReviewEventCommand ReviewCmd = new ReviewEventCommand(
                event.getEventNumber(), "great show");
        controller.runCommand(ReviewCmd);

        startOutputCapture();
        ListEventReviewsCommand ListRevsCmd = new ListEventReviewsCommand(
                "Event 1"

        );
        controller.runCommand(ListRevsCmd);
        stopOutputCaptureAndCompare("LIST_EVENT_REVIEWS_SUCCESS");
    }

    @Test
    void multipleReviewsLeft() {
        Controller controller = createController();
        LocalDateTime timeNow = LocalDateTime.now();

        Event event = createEventHelper(controller, timeNow.minusHours(2), timeNow.minusHours(1));

        ReviewEventCommand ReviewCmd = new ReviewEventCommand(
                event.getEventNumber(), "great show");
        controller.runCommand(ReviewCmd);

        ReviewEventCommand ReviewCmd2 = new ReviewEventCommand(
                event.getEventNumber(), "I hated it");
        controller.runCommand(ReviewCmd2);

        startOutputCapture();
        ListEventReviewsCommand ListRevsCmd = new ListEventReviewsCommand(
                "Event 1"

        );
        controller.runCommand(ListRevsCmd);
        stopOutputCaptureAndCompare("LIST_EVENT_REVIEWS_SUCCESS");
    }
}

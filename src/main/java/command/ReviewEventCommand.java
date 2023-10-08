package command;

import controller.Context;
import model.*;
import state.IEventState;
import view.IView;

import java.time.LocalDateTime;
import java.util.Map;

public class ReviewEventCommand implements ICommand<Review> {
    private final long eventNumber;
    private String content;
    private Review reviewResult;

    /**
     * @param eventNumber identifier of the {@link Event} to book
     * 
     */
    public ReviewEventCommand(long eventNumber, String content) {
        this.eventNumber = eventNumber;
        this.content = content;

    }

    private boolean hasValidBooking(Consumer consumer) {
        int bookings = 0;
        for (Booking booking : consumer.getBookings()) {

            if (!booking.isCancelled()) {
                bookings = bookings + 1;
                if (bookings >= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void execute(Context context, IView view) {
        IEventState eventState = context.getEventState();
        Event event = eventState.findEventByNumber(eventNumber);

        if (event == null) {
            view.displayFailure(
                    "ReviewEventCommand",
                    LogStatus.REVIEW_EVENT_EVENT_NOT_FOUND,
                    Map.of("eventNumber", eventNumber));
            reviewResult = null;
            return;
        }

        if (LocalDateTime.now().isBefore(event.getEndDateTime())) {
            view.displayFailure(
                    "ReviewEventCommand",
                    LogStatus.REVIEW_EVENT_EVENT_NOT_OVER,
                    Map.of("endDateTime", event.getEndDateTime()));
            reviewResult = null;
            return;
        }

        User currentUser = context.getUserState().getCurrentUser();

        if (!(currentUser instanceof Consumer)) {
            view.displayFailure(
                    "ReviewEventCommand",
                    LogStatus.REVIEW_EVENT_USER_NOT_CONSUMER,
                    Map.of("currentUser", String.valueOf(currentUser)));
            reviewResult = null;
            return;
        }
        Consumer consumer = (Consumer) currentUser;

        if (!hasValidBooking(consumer)) {
            view.displayFailure(
                    "ReviewEventCommand",
                    LogStatus.REVIEW_EVENT_CONSUMER_NO_VALID_BOOKING,
                    Map.of("bookings", consumer.getBookings()));

            reviewResult = null;
            return;
        }

        // add review code
        Review review = new Review(consumer, event, LocalDateTime.now(), content);
        event.addReview(review);

        reviewResult = review;

        view.displaySuccess(
                "ReviewEventCommand",
                LogStatus.REVIEW_EVENT_SUCCESS,
                Map.of("review", reviewResult));
    }

    public Review getResult() {
        return reviewResult;
    }

    private enum LogStatus {
        REVIEW_EVENT_EVENT_NOT_OVER,
        REVIEW_EVENT_EVENT_NOT_FOUND,
        REVIEW_EVENT_USER_NOT_CONSUMER,
        REVIEW_EVENT_CONSUMER_NO_VALID_BOOKING,
        REVIEW_EVENT_SUCCESS
    }
}
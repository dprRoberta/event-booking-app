package command;

import controller.Context;
import model.Event;
import model.Review;

import state.IEventState;
import view.IView;

import java.util.ArrayList;
import java.util.List;

public class ListEventReviewsCommand implements ICommand<List<Review>> {
    private List<Review> reviewResult;
    private String eventTitle;


    public ListEventReviewsCommand(String eventTitle) {
        this.eventTitle = eventTitle;
        this.reviewResult = new ArrayList<Review>();

    }

    /**
     * @param context object that provides access to global application state
     * @param view    allows passing information to the user interface
     */
    @Override
    public void execute(Context context, IView view) {
        IEventState eventState = context.getEventState();
        List<Event> events = eventState.getAllEvents();
        Event myEvent = null;
        for (Event event : events) {
            if (event.getTitle().equals(eventTitle)) {
                myEvent = event;
            }
            }

        ArrayList<Review> reviewCopy = new ArrayList<>(myEvent.getReviews());
        if (myEvent != null) {
            for (Review review : reviewCopy) {
                reviewResult.add(review);
            }
            view.displaySuccess(
                    "ListEventReviewsCommand",
                    LogStatus.LIST_EVENT_REVIEWS_SUCCESS);
        }
            }



    @Override
    public List<Review> getResult() {
        return reviewResult;
    }

    private enum LogStatus {
        LIST_EVENT_REVIEWS_SUCCESS
    }
}
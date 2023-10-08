package command;

import controller.Context;
import external.OfflineMapSystem;
import model.*;
import view.IView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.graphhopper.ResponsePath;
import com.graphhopper.util.shapes.GHPoint;

/**
 * {@link ListEventsCommand} allows anyone to get a list of {@link Event}s
 * available on the system.
 * Optionally, users can specify a particular {@link LocalDate} to look up
 * events for.
 */
public class ListEventsMaxDistanceCommand extends ListEventsCommand {
    private final TransportMode transportMode;
    private final double maxDistance;

    public ListEventsMaxDistanceCommand(boolean activeEventsOnly, LocalDate searchDate,
            TransportMode transportMode, double maxDistance) {
        super(true, activeEventsOnly, searchDate);

        this.transportMode = transportMode;
        this.maxDistance = maxDistance;
    }

    /**
     * @param context object that provides access to global application state
     * @param view    allows passing information to the user interface
     * @verifies.that if userEventsOnly is set, the current user must be logged in
     */
    @Override
    public void execute(Context context, IView view) {
        User currentUser = context.getUserState().getCurrentUser();

        // Check if user is logged in
        if (currentUser == null) {
            view.displayFailure(
                    "ListEventsMaxDistanceCommand",
                    LogStatus.LIST_EVENTS_MAX_DISTANCE_NOT_LOGGED_IN,
                    Map.of());
            eventListResult = null;
            return;
        }

        // Check if user is a consumer
        if (!(currentUser instanceof Consumer)) {
            view.displayFailure(
                    "ListEventsMaxDistanceCommand",
                    LogStatus.LIST_EVENTS_MAX_DISTANCE_NOT_CONSUMER,
                    Map.of("user", currentUser));
            eventListResult = null;
            return;
        }

        // Get consumer address and fail if not set
        Consumer consumer = (Consumer) currentUser;
        String consumerAddress = consumer.getAddress();
        if (consumerAddress == null) {
            view.displayFailure(
                    "ListEventsMaxDistanceCommand",
                    LogStatus.LIST_EVENTS_MAX_DISTANCE_NO_CONSUMER_ADDRESS,
                    Map.of("consumer", consumer));
            eventListResult = null;
            return;
        }

        // Get consumer preferences/tags
        EventTagCollection preferences = consumer.getPreferences();
        Map<String, EventTag> possibleTags = context.getEventState().getPossibleTag();

        // Setup map system
        OfflineMapSystem mapSystem = context.getMapSystem();
        GHPoint consumerLocation = mapSystem.convertToCoordinates(consumerAddress);

        // Lambda function to get distance to each event
        Function<Event, Double> getDistanceToEvent = event -> {
            String venueAddress = event.getVenueAddress();
            if (venueAddress == null) {
                return null;
            }
            GHPoint venueLocation = mapSystem.convertToCoordinates(venueAddress);

            ResponsePath route = mapSystem.routeBetweenPoints(transportMode,
                    consumerLocation, venueLocation);
            if (route == null) {
                return null;
            }

            return route.getDistance();
        };

        HashMap<Long, Double> eventDistances = new HashMap<>();
        List<Event> eventsWithinDistance = context.getEventState().getAllEvents().stream()
                // Get distance to each event
                .map(event -> {
                    eventDistances.put(event.getId(), getDistanceToEvent.apply(event));
                    return event;
                })
                // Filter out events that are too far away
                .filter(event -> {
                    Double distanceToEvent = eventDistances.get(event.getId());
                    return distanceToEvent != null && distanceToEvent <= maxDistance;
                })
                .collect(Collectors.toList());

        List<Event> eventsFittingPreferences = eventsWithinDistance.stream()
                // Sort events by distance
                .sorted((event1, event2) -> {
                    Double distanceToEvent1 = eventDistances.get(event1.getId());
                    Double distanceToEvent2 = eventDistances.get(event2.getId());
                    return distanceToEvent1.compareTo(distanceToEvent2);
                })
                // Filter out events that don't satisfy the user's preferences
                .filter(event -> eventSatisfiesPreferences(possibleTags, preferences, event))
                .collect(Collectors.toList());

        eventListResult = filterEvents(eventsFittingPreferences, activeEventsOnly, searchDate);
        view.displaySuccess(
                "ListEventsMaxDistanceCommand",
                LogStatus.LIST_EVENTS_MAX_DISTANCE_SUCCESS,
                Map.of("activeEventsOnly", activeEventsOnly,
                        "userEventsOnly", true,
                        "searchDate", String.valueOf(searchDate),
                        "transportMode", transportMode,
                        "maxDistance", maxDistance,
                        "eventList", eventListResult));
    }

    /**
     * @return List of {@link Event}s if successful and null otherwise
     */
    @Override
    public List<Event> getResult() {
        return eventListResult;
    }

    private enum LogStatus {
        LIST_EVENTS_MAX_DISTANCE_SUCCESS,
        LIST_EVENTS_MAX_DISTANCE_NOT_LOGGED_IN,
        LIST_EVENTS_MAX_DISTANCE_NOT_CONSUMER,
        LIST_EVENTS_MAX_DISTANCE_NO_CONSUMER_ADDRESS
    }
}

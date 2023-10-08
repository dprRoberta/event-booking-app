package command;

import controller.Context;
import model.*;
import view.IView;

import java.util.ArrayList;
import java.util.Map;

import com.graphhopper.ResponsePath;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.Translation;
import com.graphhopper.util.shapes.GHPoint;

public class GetEventDirectionsCommand implements ICommand<String[]> {
    private final long eventNumber;
    private final TransportMode transportMode;
    private ArrayList<String> directionsResult;

    public GetEventDirectionsCommand(long eventNumber, TransportMode transportMode) {
        this.eventNumber = eventNumber;
        this.transportMode = transportMode;
    }

    @Override
    public void execute(Context context, IView view) {
        Event event = context.getEventState().findEventByNumber(eventNumber);

        // Check the event exists
        if (event == null) {
            view.displayFailure(
                    "GetEventDirectionsCommand",
                    LogStatus.GET_DIRECTIONS_EVENT_NOT_FOUND,
                    Map.of("eventNumber", eventNumber));
            directionsResult.clear();
            return;
        }

        String venueAddress = event.getVenueAddress();
        // Check the event includes a venueAddress
        if (venueAddress == null) {
            view.displayFailure(
                    "GetEventDirectionsCommand",
                    LogStatus.GET_DIRECTIONS_EVENT_NO_VENUE_ADDRESS,
                    Map.of("eventNumber", eventNumber));
            directionsResult.clear();
            return;
        }

        // Check the current user is a consumer
        User currentUser = context.getUserState().getCurrentUser();

        if (!(currentUser instanceof Consumer)) {
            view.displayFailure(
                    "GetEventDirectionsCommand",
                    LogStatus.GET_DIRECTIONS_USER_NOT_CONSUMER,
                    Map.of("eventNumber", eventNumber,
                            "currentUser", currentUser != null ? currentUser : "none"));
            directionsResult.clear();
            return;
        }

        // Check the consumer has a homeAddress
        Consumer consumer = (Consumer) currentUser;
        String consumerAddress = consumer.getAddress();
        if (consumerAddress == null) {
            view.displayFailure(
                    "GetEventDirectionsCommand",
                    LogStatus.GET_DIRECTIONS_EVENT_NO_CONSUMER_ADDRESS,
                    Map.of("currentUser", currentUser));
            directionsResult.clear();
            return;
        }

        // Get the directions
        ResponsePath path = null;
        try {
            path = context.getMapSystem().routeBetweenPoints(
                    transportMode,
                    context.getMapSystem().convertToCoordinates(consumerAddress),
                    context.getMapSystem().convertToCoordinates(venueAddress));
        } catch (Exception e) {
            view.displayFailure(
                    "GetEventDirectionsCommand",
                    LogStatus.GET_DIRECTIONS_ROUTE_ERROR,
                    Map.of("eventNumber", eventNumber,
                            "currentUser", currentUser));
            directionsResult.clear();
            return;
        }

        if (path == null) {
            view.displayFailure(
                    "GetEventDirectionsCommand",
                    LogStatus.GET_DIRECTIONS_ROUTE_ERROR,
                    Map.of("eventNumber", eventNumber,
                            "currentUser", currentUser));
            directionsResult.clear();
            return;
        }

        // Return total distance, then distance and description of each turn
        InstructionList instructions = path.getInstructions();
        Translation translation = context.getMapSystem().getTranslation();
        instructions.forEach(instruction -> directionsResult.add(instruction.getTurnDescription(translation)));
    }

    @Override
    public String[] getResult() {
        return directionsResult.toArray(new String[directionsResult.size()]);
    }

    private enum LogStatus {
        GET_DIRECTIONS_EVENT_NOT_FOUND,
        GET_DIRECTIONS_EVENT_NO_VENUE_ADDRESS,
        GET_DIRECTIONS_EVENT_NO_CONSUMER_ADDRESS,
        GET_DIRECTIONS_USER_NOT_CONSUMER,
        GET_DIRECTIONS_ROUTE_ERROR
    }
}

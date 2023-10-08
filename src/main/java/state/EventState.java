package state;

import model.Event;
import model.EventTagCollection;
import model.EventType;
import model.EventTag;
import model.Review;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

/**
 * {@link EventState} is a concrete implementation of {@link IEventState}.
 */
public class EventState implements IEventState, Serializable {
    private final List<Event> events;
    private long nextEventNumber;
    private Map<String, EventTag> possibleTags;

    /**
     * Create a new EventState with an empty list of events, which keeps track of
     * the next event and performance numbers
     * it will generate, starting from 1 and incrementing by 1 each time when
     * requested
     */
    public EventState() {
        events = new LinkedList<>();
        nextEventNumber = 1;
        possibleTags = new HashMap<>();
        possibleTags.put("hasSocialDistancing", new EventTag(new HashSet<>(Arrays.asList("true", "false")),
                "false"));
        possibleTags.put("hasAirFiltration", new EventTag(new HashSet<>(Arrays.asList("true", "false")),
                "false"));
        possibleTags.put("venueCapacity", new EventTag(new HashSet<>(Arrays.asList("<20", "20-100", "100-200", "200")),
                "<20"));
    }

    /**
     * Copy constructor to make a deep copy of another EventState instance
     *
     * @param other instance to copy
     */
    public EventState(IEventState other) {
        EventState otherImpl = (EventState) other;
        events = new LinkedList<>(otherImpl.events);
        nextEventNumber = otherImpl.nextEventNumber;
        possibleTags = new HashMap<>(otherImpl.possibleTags);
        possibleTags.put("hasSocialDistancing", new EventTag(new HashSet<>(Arrays.asList("true", "false")),
                "false"));
        possibleTags.put("hasAirFiltration", new EventTag(new HashSet<>(Arrays.asList("true", "false")),
                "false"));
        possibleTags.put("venueCapacity", new EventTag(new HashSet<>(Arrays.asList("<20", "20-100", "100-200", "200")),
                "<20"));
        ArrayList<Review> reviews = new ArrayList<Review>();
    }

    @Override
    public List<Event> getAllEvents() {
        return events;
    }

    public Map<String, EventTag> getPossibleTag() {
        return possibleTags;
    }

    @Override
    public Event findEventByNumber(long eventNumber) {
        return events.stream()
                .filter(event -> event.getEventNumber() == eventNumber)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Event createEvent(String title,
            EventType type,
            int numTickets,
            int ticketPriceInPence,
            String venueAddress,
            String description,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            EventTagCollection eventTags) {
        long eventNumber = nextEventNumber;
        nextEventNumber++;

        Event event = new Event(eventNumber, title, type, numTickets,
                ticketPriceInPence, venueAddress, description, startDateTime, endDateTime, eventTags);
        events.add(event);
        return event;
    }

    public EventTag createEventTag(String name, Set<String> values, String defaultValue) {
        EventTag eventTag = new EventTag(values, defaultValue);
        possibleTags.put(name, eventTag);
        return eventTag;
    }

    // Used by load command to add events to the active state
    public Event addEvent(Event loadedEvent) {
        // Event numbers are final so cannot just revalue them - need to create new
        // event object
        Event newEvent = createEvent(loadedEvent.getTitle(),
                loadedEvent.getType(),
                loadedEvent.getNumTicketsCap(),
                loadedEvent.getTicketPriceInPence(),
                loadedEvent.getVenueAddress(),
                loadedEvent.getDescription(),
                loadedEvent.getStartDateTime(),
                loadedEvent.getEndDateTime(),
                loadedEvent.getTags());
        newEvent.setNumTicketsLeft(loadedEvent.getNumTicketsLeft());
        return newEvent;
    }

    public void addEventTag(String newEventTagName, EventTag newEventTag) {
        possibleTags.put(newEventTagName, newEventTag);
    }

}

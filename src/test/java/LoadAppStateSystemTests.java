import command.*;
import controller.Controller;
import model.EventTagCollection;
import model.EventType;
import org.junit.jupiter.api.Test;

import java.net.CacheRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LoadAppStateSystemTests extends ConsoleTest {

    @Test
    public void staffNotLoggedIn() {
        Controller controller = createStaffAndEvent(5, 1);
        String validFileName = "file_to_load.txt";

        controller.runCommand(new LogoutCommand());

        startOutputCapture();
        controller.runCommand(new LoadAppStateCommand(validFileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_STAFF_NOT_LOGGED_IN");
    }

    @Test
    public void invalidFileName() {
        Controller controller = createStaffAndEvent(5, 1);
        String invalidFileName = "invalid?file|name.txt";

        startOutputCapture();
        controller.runCommand(new LoadAppStateCommand(invalidFileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_INVALID_FILE_PROVIDED");
    }

    @Test
    public void fileNameDoesNotExist() {
        Controller controller = createStaffAndEvent(5, 1);
        String validFileName = "this_file_does_not_exist.txt";

        startOutputCapture();
        controller.runCommand(new LoadAppStateCommand(validFileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_INVALID_FILE_PROVIDED");
    }

    @Test
    public void fileExistsButInvalidContent() {
        Controller controller = createStaffAndEvent(5, 1);
        String validFileName = "invalid_content.txt";

        startOutputCapture();
        controller.runCommand(new LoadAppStateCommand(validFileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_INVALID_FILE_PROVIDED");
    }

    @Test
    public void clashingUsers() {
        // Consumer john has a different phone number and address in the active state
        // and the saved file
        Controller controller = createController();
        controller.runCommand(new RegisterConsumerCommand("john",
                "john@gmail.com",
                "123",
                "55.94462660194542 -3.186647757484971",
                "secure123"));
        controller.runCommand(new LogoutCommand());

        createStaff(controller);
        String fileName = "user_clash.txt";
        controller.runCommand(new SaveAppStateCommand(fileName));
        controller = null;

        // Different session
        Controller newController = createController();
        newController.runCommand(new RegisterConsumerCommand("john",
                "john@gmail.com",
                "456",
                "55.95557425513147 -3.1828696694553273",
                "secure123"));
        newController.runCommand(new LogoutCommand());
        createStaff(newController);

        // startOutputCapture();
        newController.runCommand(new LoadAppStateCommand(fileName));
        // stopOutputCaptureAndCompare(
        // "LOAD_APP_CLASHING_USERS"
        // );
    }

    @Test
    public void clashingEvent() {
        // Taylor Swift concert clash - has different tickets cap and tickets price
        Controller controller = createController();
        createStaff(controller);
        controller.runCommand(new CreateEventCommand("Taylor Swift",
                EventType.Music,
                100,
                500,
                "55.94752486420547 -3.2052285611864324",
                "Taylor Swift Concert",
                LocalDateTime.of(2024, 8, 16, 14, 0, 0, 0),
                LocalDateTime.of(2024, 8, 16, 20, 0, 0, 0),
                new EventTagCollection()));
        String fileName = "event_clash.txt";
        controller.runCommand(new SaveAppStateCommand(fileName));
        controller = null;

        // New session
        Controller newController = createController();
        createStaff(newController);
        newController.runCommand(new CreateEventCommand("Taylor Swift",
                EventType.Music,
                200,
                1000,
                "55.94752486420547 -3.2052285611864324",
                "Taylor Swift Concert",
                LocalDateTime.of(2024, 8, 16, 14, 0, 0, 0),
                LocalDateTime.of(2024, 8, 16, 20, 0, 0, 0),
                new EventTagCollection()));
        startOutputCapture();
        newController.runCommand(new LoadAppStateCommand(fileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_CLASHING_EVENTS");
    }

    @Test
    public void clashingEventTags() {
        // Wheelchair access tag has a different set of values in the active and saved
        // state
        Controller controller = createController();
        createStaff(controller);
        Set<String> savedTagValues = new HashSet<>(Arrays.asList("true", "false", "maybe"));
        controller.runCommand(new AddEventTagCommand("hasWheelchairAccess",
                savedTagValues,
                "false"));

        String fileName = "event_tag_clash.txt";
        controller.runCommand(new SaveAppStateCommand(fileName));
        controller = null;

        // New session
        Controller newController = createController();
        createStaff(newController);
        Set<String> existingTagValues = new HashSet<>(Arrays.asList("true", "false"));
        newController.runCommand(new AddEventTagCommand("hasWheelchairAccess",
                existingTagValues,
                "false"));
        startOutputCapture();
        newController.runCommand(new LoadAppStateCommand(fileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_CLASHING_TAGS");
    }

    /*
     * README
     * There is no way to test for clashing bookings - since bookings are assigned a
     * timestamp exact to the nanosecond, we
     * cannot create two bookings at the same time with clashing values
     * 
     * However, to avoid the situation where an event can have inconsistent bookings
     * from the active state and the saved
     * state, (which would not be clashing bookings) we do not allow the saved state
     * to have bookings from new consumers to
     * events that exist already in the active state
     */

    @Test
    public void eventHasClashingBookings() {
        Controller controller = createController();
        controller.runCommand(new RegisterStaffCommand("admin@gmail.com",
                "secure123",
                "Nec temere nec timide"));

        controller.runCommand(new CreateEventCommand("Taylor Swift",
                EventType.Music,
                100,
                500,
                "55.94752486420547 -3.2052285611864324",
                "Taylor Swift Concert",
                LocalDateTime.of(2024, 8, 16, 14, 0, 0, 0),
                LocalDateTime.of(2024, 8, 16, 20, 0, 0, 0),
                new EventTagCollection()));

        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterConsumerCommand("john",
                "john@gmail.com",
                "123",
                "55.94462660194542 -3.186647757484971",
                "secure123"));
        controller.runCommand(new BookEventCommand(1, 5));
        controller.runCommand(new LogoutCommand());

        controller.runCommand(new LoginCommand("admin@gmail.com", "secure123"));
        String fileName = "event_with_clashing_bookings.txt";
        controller.runCommand(new SaveAppStateCommand(fileName));
        controller = null;

        // New session - This is the active state
        Controller newController = createController();
        newController.runCommand(new RegisterStaffCommand("otherAdmin@gmail.com",
                "secure123",
                "Nec temere nec timide"));
        newController.runCommand(new CreateEventCommand("Taylor Swift",
                EventType.Music,
                100,
                500,
                "55.94752486420547 -3.2052285611864324",
                "Taylor Swift Concert",
                LocalDateTime.of(2024, 8, 16, 14, 0, 0, 0),
                LocalDateTime.of(2024, 8, 16, 20, 0, 0, 0),
                new EventTagCollection()));
        // Active event needs a booking also so it has the same remaining tickets and
        // doesn't cause as clashing event error instead
        newController.runCommand(new LogoutCommand());
        newController.runCommand(new RegisterConsumerCommand("notjohn",
                "notjohn@gmail.com",
                "123",
                "55.94462660194542 -3.186647757484971",
                "secure123"));
        newController.runCommand(new BookEventCommand(1, 5));
        newController.runCommand(new LogoutCommand());

        newController.runCommand(new LoginCommand("otherAdmin@gmail.com", "secure123"));
        startOutputCapture();
        newController.runCommand(new LoadAppStateCommand(fileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_CLASHING_BOOKINGS");
    }

    // ---------SUCCESS SCENARIOS----------

    // Loads new events, eventTags, users and bookings
    @Test
    public void loadAllIntoEmptyActiveState() {
        Controller controller = createController();
        controller.runCommand(new RegisterStaffCommand("admin@gmail.com",
                "secure123",
                "Nec temere nec timide"));

        controller.runCommand(new CreateEventCommand("Taylor Swift",
                EventType.Music,
                100,
                500,
                "55.94752486420547 -3.2052285611864324",
                "Taylor Swift Concert",
                LocalDateTime.of(2024, 8, 16, 14, 0, 0, 0),
                LocalDateTime.of(2024, 8, 16, 20, 0, 0, 0),
                new EventTagCollection()));

        Set<String> savedTagValues = new HashSet<>(Arrays.asList("true", "false"));
        controller.runCommand(new AddEventTagCommand("hasWheelchairAccess",
                savedTagValues,
                "false"));

        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterConsumerCommand("john",
                "john@gmail.com",
                "123",
                "55.94462660194542 -3.186647757484971",
                "secure123"));
        controller.runCommand(new BookEventCommand(1, 5));
        controller.runCommand(new LogoutCommand());

        controller.runCommand(new LoginCommand("admin@gmail.com", "secure123"));
        String fileName = "load_totally_new.txt";
        controller.runCommand(new SaveAppStateCommand(fileName));
        controller = null;

        // New session
        Controller newController = createController();
        createStaff(newController);
        startOutputCapture();
        newController.runCommand(new LoadAppStateCommand(fileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_STATE_SUCCESS");
    }

    @Test
    public void mergeNonClashingTags() {
        // Create event tag for the saved state
        Controller controller = createController();
        createStaff(controller);
        Set<String> tagValues = new HashSet<>(Arrays.asList("true", "false"));
        controller.runCommand(new AddEventTagCommand("hasWheelchairAccess", tagValues, "true"));
        String fileName = "merge_tags.txt";
        controller.runCommand(new SaveAppStateCommand(fileName));
        controller = null;

        // New session with non-clashing tag
        Controller newController = createController();
        createStaff(newController);
        newController.runCommand(new AddEventTagCommand("hasImpairedVisionAssistance", tagValues, "true"));

        startOutputCapture();
        newController.runCommand(new LoadAppStateCommand(fileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_STATE_SUCCESS");
    }

    @Test
    public void mergeNonClashingEvents() {
        // Create event for the saved state
        Controller controller = createController();
        createStaff(controller);
        controller.runCommand(new CreateEventCommand("Taylor Swift",
                EventType.Music,
                100,
                500,
                "55.94752486420547 -3.2052285611864324",
                "Taylor Swift Concert",
                LocalDateTime.of(2024, 8, 16, 14, 0, 0, 0),
                LocalDateTime.of(2024, 8, 16, 20, 0, 0, 0),
                new EventTagCollection()));
        String fileName = "merge_events.txt";
        controller.runCommand(new SaveAppStateCommand(fileName));
        controller = null;

        // New session with non=clashing event
        Controller newController = createController();
        createStaff(newController);
        newController.runCommand(new CreateEventCommand("Bruno Mars",
                EventType.Music,
                200,
                300,
                "55.94752486420547 -3.2052285611864324",
                "Bruno Mars Concert",
                LocalDateTime.of(2025, 7, 24, 14, 0, 0, 0),
                LocalDateTime.of(2025, 7, 24, 20, 0, 0, 0),
                new EventTagCollection()));
        startOutputCapture();
        newController.runCommand(new LoadAppStateCommand(fileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_STATE_SUCCESS");
    }

    @Test
    public void mergeNonClashingUsers() {
        // Create user in the saved state
        Controller controller = createController();
        controller.runCommand(new RegisterConsumerCommand("john",
                "john@gmail.com",
                "123",
                "55.94462660194542 -3.186647757484971",
                "secure123"));
        controller.runCommand(new LogoutCommand());
        createStaff(controller);
        String fileName = "merge_users.txt";
        controller.runCommand(new SaveAppStateCommand(fileName));
        controller = null;

        // New session with non-clashing users
        Controller newController = createController();
        newController.runCommand(new RegisterConsumerCommand("sarah",
                "sarah@gmail.com",
                "456",
                "55.94462660194542 -3.186647757484971",
                "moreSecure123"));
        newController.runCommand(new LogoutCommand());
        createStaff(newController);
        startOutputCapture();
        newController.runCommand(new LoadAppStateCommand(fileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_STATE_SUCCESS");
    }

    @Test
    public void mergeNewUserNewEventsBookings() {
        // Saved state has new user with a booking to a new event
        Controller controller = createController();
        controller.runCommand(new RegisterStaffCommand("admin@gmail.com",
                "secure123",
                "Nec temere nec timide"));
        controller.runCommand(new CreateEventCommand("Taylor Swift",
                EventType.Music,
                100,
                500,
                "55.94752486420547 -3.2052285611864324",
                "Taylor Swift Concert",
                LocalDateTime.of(2024, 8, 16, 14, 0, 0, 0),
                LocalDateTime.of(2024, 8, 16, 20, 0, 0, 0),
                new EventTagCollection()));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterConsumerCommand("john",
                "john@gmail.com",
                "123",
                "55.94462660194542 -3.186647757484971",
                "secure123"));
        controller.runCommand(new BookEventCommand(1, 5));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new LoginCommand("admin@gmail.com", "secure123"));
        String fileName = "merge_new_users_new_events_booking.txt";
        controller.runCommand(new SaveAppStateCommand(fileName));
        controller = null;

        // New session with different user with a booking to a different event
        Controller newController = createController();
        newController.runCommand(new RegisterStaffCommand("otherAdmin@gmail.com",
                "secure123",
                "Nec temere nec timide"));
        newController.runCommand(new CreateEventCommand("Bruno Mars",
                EventType.Music,
                200,
                300,
                "55.94752486420547 -3.2052285611864324",
                "Bruno Mars Concert",
                LocalDateTime.of(2025, 7, 24, 14, 0, 0, 0),
                LocalDateTime.of(2025, 7, 24, 20, 0, 0, 0),
                new EventTagCollection()));
        newController.runCommand(new LogoutCommand());
        newController.runCommand(new RegisterConsumerCommand("sarah",
                "sarah@gmail.com",
                "456",
                "55.94462660194542 -3.186647757484971",
                "moreSecure123"));
        newController.runCommand(new BookEventCommand(1, 5));
        newController.runCommand(new LogoutCommand());
        newController.runCommand(new LoginCommand("otherAdmin@gmail.com", "secure123"));
        startOutputCapture();
        newController.runCommand(new LoadAppStateCommand(fileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_STATE_SUCCESS");
    }

    @Test
    public void mergeBookingsFromExistingUsersToNewEvents() {
        // Saved state has an existing user with a booking to a new event
        Controller controller = createController();
        controller.runCommand(new RegisterStaffCommand("admin@gmail.com",
                "secure123",
                "Nec temere nec timide"));
        controller.runCommand(new CreateEventCommand("Taylor Swift",
                EventType.Music,
                100,
                500,
                "55.94752486420547 -3.2052285611864324",
                "Taylor Swift Concert",
                LocalDateTime.of(2024, 8, 16, 14, 0, 0, 0),
                LocalDateTime.of(2024, 8, 16, 20, 0, 0, 0),
                new EventTagCollection()));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterConsumerCommand("john",
                "john@gmail.com",
                "123",
                "55.94462660194542 -3.186647757484971",
                "secure123"));
        controller.runCommand(new BookEventCommand(1, 5));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new LoginCommand("admin@gmail.com", "secure123"));
        String fileName = "merge_existing_users_new_events_booking.txt";
        controller.runCommand(new SaveAppStateCommand(fileName));
        controller = null;

        // New session with the same user with a booking to a different event
        Controller newController = createController();
        newController.runCommand(new RegisterStaffCommand("otherAdmin@gmail.com",
                "secure123",
                "Nec temere nec timide"));
        newController.runCommand(new CreateEventCommand("Bruno Mars",
                EventType.Music,
                200,
                300,
                "55.94752486420547 -3.2052285611864324",
                "Bruno Mars Concert",
                LocalDateTime.of(2025, 7, 24, 14, 0, 0, 0),
                LocalDateTime.of(2025, 7, 24, 20, 0, 0, 0),
                new EventTagCollection()));
        newController.runCommand(new LogoutCommand());
        newController.runCommand(new RegisterConsumerCommand("john",
                "john@gmail.com",
                "123",
                "55.94462660194542 -3.186647757484971",
                "secure123"));
        newController.runCommand(new BookEventCommand(1, 5));
        newController.runCommand(new LogoutCommand());
        newController.runCommand(new LoginCommand("otherAdmin@gmail.com", "secure123"));
        startOutputCapture();
        newController.runCommand(new LoadAppStateCommand(fileName));
        stopOutputCaptureAndCompare(
                "LOAD_APP_STATE_SUCCESS");
    }
}

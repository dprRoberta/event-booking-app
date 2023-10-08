import command.CreateEventCommand;
import command.LogoutCommand;
import command.RegisterStaffCommand;
import controller.Controller;
import model.Event;
import model.EventTagCollection;
import model.EventType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNull;

public class CreateEventSystemTests extends ConsoleTest {
        private static void registerPawsForAwwws(Controller controller) {
                controller.runCommand(new RegisterStaffCommand(
                                "hasta@vista.baby",
                                "very insecure password 123",
                                "Nec temere nec timide"));
        }

        // Create Event for testing time related issues
        private static Event createEvent(Controller controller,
                        LocalDateTime startDateTime,
                        LocalDateTime endDateTime) {
                CreateEventCommand eventCmd = new CreateEventCommand(
                                "Puppies against depression",
                                EventType.Theatre,
                                500,
                                100,
                                "55.86440964478519 -4.252880444477458",
                                "Come and enjoy some pets for pets",
                                startDateTime,
                                endDateTime,
                                new EventTagCollection());
                controller.runCommand(eventCmd);
                return eventCmd.getResult();
        }

        // Create event for testing event tag related issues
        private static Event createEvent(Controller controller,
                        EventTagCollection tags) {
                CreateEventCommand eventCmd = new CreateEventCommand(
                                "Puppies against depression",
                                EventType.Theatre,
                                500,
                                100,
                                "55.86440964478519 -4.252880444477458",
                                "Come and enjoy some pets for pets",
                                LocalDateTime.now().plusHours(1),
                                LocalDateTime.now().plusHours(2),
                                tags);
                controller.runCommand(eventCmd);
                return eventCmd.getResult();
        }

        private static Event createEvent(Controller controller,
                        int ticketPriceInPence) {
                CreateEventCommand eventCmd = new CreateEventCommand(
                                "Puppies against depression",
                                EventType.Theatre,
                                500,
                                ticketPriceInPence,
                                "55.86440964478519 -4.252880444477458",
                                "Come and enjoy some pets for pets",
                                LocalDateTime.now().plusHours(1),
                                LocalDateTime.now().plusHours(2),
                                new EventTagCollection());
                controller.runCommand(eventCmd);
                return eventCmd.getResult();
        }

        // Ticket number related tests
        @Test
        void createNonTicketedEvent() {
                startOutputCapture();
                createStaffAndEvent(0, 1);
                stopOutputCaptureAndCompare(
                                "REGISTER_STAFF_SUCCESS",
                                "STAFF_LOGIN_SUCCESS",
                                "CREATE_EVENT_INVALID_NUMBER_OF_TICKETS");
        }

        @Test
        void createTicketedEvent() {
                startOutputCapture();
                Controller controller = createStaffAndEvent(1, 1);
                controller.runCommand(new LogoutCommand());
                stopOutputCaptureAndCompare(
                                "REGISTER_STAFF_SUCCESS",
                                "STAFF_LOGIN_SUCCESS",
                                "CREATE_EVENT_SUCCESS",
                                "USER_LOGOUT_SUCCESS");
        }

        // Event time related tests
        @Test
        void createEventInThePast() {
                Controller controller = createController();
                startOutputCapture();
                registerPawsForAwwws(controller);
                Event event = createEvent(
                                controller,
                                LocalDateTime.now().minusDays(5),
                                LocalDateTime.now().minusDays(5).plusHours(2));
                assertNull(event);
                stopOutputCaptureAndCompare(
                                "REGISTER_STAFF_SUCCESS",
                                "STAFF_LOGIN_SUCCESS",
                                "CREATE_EVENT_IN_THE_PAST");
        }

        @Test
        void createEventWithEndBeforeStart() {
                Controller controller = createController();
                startOutputCapture();
                registerPawsForAwwws(controller);
                Event event = createEvent(
                                controller,
                                LocalDateTime.now().minusDays(5),
                                LocalDateTime.now().minusDays(5).minusHours(2));
                assertNull(event);
                stopOutputCaptureAndCompare(
                                "REGISTER_STAFF_SUCCESS",
                                "STAFF_LOGIN_SUCCESS",
                                "CREATE_EVENT_START_AFTER_END");
        }

        // Event tag related tests
        @Test
        void createEventWithNonExistentTags() {
                Controller controller = createController();
                startOutputCapture();
                registerPawsForAwwws(controller);
                Event event = createEvent(
                                controller,
                                new EventTagCollection("nonexistentTag=true,hasSocialDistancing=true"));
                assertNull(event);
                stopOutputCaptureAndCompare(
                                "REGISTER_STAFF_SUCCESS",
                                "STAFF_LOGIN_SUCCESS",
                                "CREATE_EVENT_TAG_NAME_NOT_KNOWN");
        }

        @Test
        void createEventWithUnavailableTagValues() {
                Controller controller = createController();
                startOutputCapture();
                registerPawsForAwwws(controller);
                Event event = createEvent(
                                controller,
                                new EventTagCollection("hasSocialDistancing=maybe,hasAirFiltration=true"));
                assertNull(event);
                stopOutputCaptureAndCompare(
                                "REGISTER_STAFF_SUCCESS",
                                "STAFF_LOGIN_SUCCESS",
                                "CREATE_EVENT_TAG_VALUE_NOT_KNOWN");
        }

        @Test
        void createEventWithTags() {
                Controller controller = createController();
                startOutputCapture();
                registerPawsForAwwws(controller);
                createEvent(
                                controller,
                                new EventTagCollection(
                                                "hasSocialDistancing=true,hasAirFiltration=true,venueCapacity=200"));
                controller.runCommand(new LogoutCommand());
                stopOutputCaptureAndCompare(
                                "REGISTER_STAFF_SUCCESS",
                                "STAFF_LOGIN_SUCCESS",
                                "CREATE_EVENT_SUCCESS",
                                "USER_LOGOUT_SUCCESS");
        }

        // Ticket price related tests
        @Test
        void createEventWithNegativeTicketPrice() {
                Controller controller = createController();
                startOutputCapture();
                registerPawsForAwwws(controller);
                Event event = createEvent(controller, -100);
                assertNull(event);
                stopOutputCaptureAndCompare(
                                "REGISTER_STAFF_SUCCESS",
                                "STAFF_LOGIN_SUCCESS",
                                "CREATE_EVENT_NEGATIVE_TICKET_PRICE");
        }

        // User is not staff
        @Test
        void createEventByConsumer() {
                Controller controller = createController();
                startOutputCapture();
                createConsumer(controller);
                Event event = createEvent(controller, 100);
                assertNull(event);
                stopOutputCaptureAndCompare(
                                "REGISTER_CONSUMER_SUCCESS",
                                "CONSUMER_LOGIN_SUCCESS",
                                "CREATE_EVENT_USER_NOT_STAFF");
        }
}

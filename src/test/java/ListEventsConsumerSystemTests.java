import command.*;
import controller.Controller;

import model.EventTagCollection;
import model.EventType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ListEventsConsumerSystemTests extends ConsoleTest {
        // Create event for testing event tag related issues
        private static void createEvent(Controller controller,
                        EventTagCollection tags) {
                CreateEventCommand eventCmd = new CreateEventCommand(
                                "Puppies against depression",
                                EventType.Theatre,
                                500,
                                100,
                                "55.86440964478519 -4.252880444477458",
                                "Come and enjoy some pets for pets",
                                LocalDateTime.now().plusHours(48),
                                LocalDateTime.now().plusHours(49),
                                tags);
                controller.runCommand(eventCmd);
        }

        private static void createAnotherEvent(Controller controller,
                        EventTagCollection tags) {
                CreateEventCommand eventCmd = new CreateEventCommand(
                                "Puppies against anxiety",
                                EventType.Theatre,
                                500,
                                100,
                                "55.86440964478519 -4.252880444477458",
                                "Come and enjoy some pets for pets",
                                LocalDateTime.now().plusHours(48),
                                LocalDateTime.now().plusHours(49),
                                tags);
                controller.runCommand(eventCmd);
        }

        @Test
        void listEventsUserNotLoggedIn() {
                Controller controller = createController();
                startOutputCapture();
                ListEventsCommand listEventsCmd = new ListEventsCommand(
                                true,
                                false,
                                LocalDate.now().plusDays(2));
                controller.runCommand(listEventsCmd);
                stopOutputCaptureAndCompare("LIST_EVENTS_NOT_LOGGED_IN");
        }

        @Test
        void listEventsEventHasNoTagsConsumerHasNoPreferences() {
                Controller controller = createStaffAndEvent(1, 48);
                controller.runCommand(new LogoutCommand());
                createConsumer(controller);
                startOutputCapture();
                ListEventsCommand listEventsCmd = new ListEventsCommand(
                                true,
                                false,
                                LocalDate.now().plusDays(2));
                controller.runCommand(listEventsCmd);
                stopOutputCaptureAndCompare("LIST_EVENTS_SUCCESS");
        }

        @Test
        void listEventsEventHasTagsConsumerHasNoPreferences() {
                Controller controller = createController();
                createStaff(controller);
                createEvent(
                                controller,
                                new EventTagCollection("hasSocialDistancing=true"));
                controller.runCommand(new LogoutCommand());
                createConsumer(controller);
                startOutputCapture();
                ListEventsCommand listEventsCmd = new ListEventsCommand(
                                true,
                                false,
                                LocalDate.now().plusDays(2));
                controller.runCommand(listEventsCmd);
                stopOutputCaptureAndCompare("LIST_EVENTS_SUCCESS");
        }

        @Test
        void listEventsEventHasNoTagsConsumerHasPreferences() {
                Controller controller = createStaffAndEvent(1, 48);
                controller.runCommand(new LogoutCommand());
                createConsumer(controller);
                UpdateConsumerProfileCommand updateCmd = new UpdateConsumerProfileCommand(
                                CONSUMER_PASSWORD,
                                "Alice",
                                CONSUMER_EMAIL,
                                "000",
                                "",
                                CONSUMER_PASSWORD,
                                new EventTagCollection("hasSocialDistancing=true"));
                controller.runCommand(updateCmd);
                startOutputCapture();
                ListEventsCommand listEventsCmd = new ListEventsCommand(
                                true,
                                false,
                                LocalDate.now().plusDays(2));
                controller.runCommand(listEventsCmd);
                stopOutputCaptureAndCompare("LIST_EVENTS_SUCCESS");
        }

        @Test
        void listEventsEventHasTagsConsumerHasPreferencesMatch() {
                Controller controller = createController();
                createStaff(controller);
                createEvent(
                                controller,
                                new EventTagCollection("hasSocialDistancing=true"));
                controller.runCommand(new LogoutCommand());
                createConsumer(controller);
                UpdateConsumerProfileCommand updateCmd = new UpdateConsumerProfileCommand(
                                CONSUMER_PASSWORD,
                                "Alice",
                                CONSUMER_EMAIL,
                                "000",
                                "",
                                CONSUMER_PASSWORD,
                                new EventTagCollection("hasSocialDistancing=true"));
                controller.runCommand(updateCmd);
                startOutputCapture();
                ListEventsCommand listEventsCmd = new ListEventsCommand(
                                true,
                                false,
                                LocalDate.now().plusDays(2));
                controller.runCommand(listEventsCmd);
                stopOutputCaptureAndCompare("LIST_EVENTS_SUCCESS");
        }

        @Test
        void listEventsEventHasTagsConsumerHasPreferencesDontMatch() {
                Controller controller = createController();
                createStaff(controller);
                createEvent(
                                controller,
                                new EventTagCollection("hasSocialDistancing=false"));
                controller.runCommand(new LogoutCommand());
                createConsumer(controller);
                UpdateConsumerProfileCommand updateCmd = new UpdateConsumerProfileCommand(
                                CONSUMER_PASSWORD,
                                "Alice",
                                CONSUMER_EMAIL,
                                "000",
                                "",
                                CONSUMER_PASSWORD,
                                new EventTagCollection("hasSocialDistancing=true"));
                controller.runCommand(updateCmd);
                startOutputCapture();
                ListEventsCommand listEventsCmd = new ListEventsCommand(
                                true,
                                false,
                                LocalDate.now().plusDays(2));
                controller.runCommand(listEventsCmd);
                stopOutputCaptureAndCompare("LIST_EVENTS_SUCCESS");
        }

        @Test
        void listEventsEventHasTagsConsumerHasPreferencesMoreThanOneEventOneMatch() {
                Controller controller = createController();
                createStaff(controller);
                createEvent(
                                controller,
                                new EventTagCollection("hasSocialDistancing=true"));
                createAnotherEvent(
                                controller,
                                new EventTagCollection("hasAirFiltration=true"));
                controller.runCommand(new LogoutCommand());
                createConsumer(controller);
                UpdateConsumerProfileCommand updateCmd = new UpdateConsumerProfileCommand(
                                CONSUMER_PASSWORD,
                                "Alice",
                                CONSUMER_EMAIL,
                                "000",
                                "",
                                CONSUMER_PASSWORD,
                                new EventTagCollection("hasSocialDistancing=true"));
                controller.runCommand(updateCmd);
                startOutputCapture();
                ListEventsCommand listEventsCmd = new ListEventsCommand(
                                true,
                                false,
                                LocalDate.now().plusDays(2));
                controller.runCommand(listEventsCmd);
                stopOutputCaptureAndCompare("LIST_EVENTS_SUCCESS");
        }

        @Test
        void listEventsEventHasTagsConsumerHasPreferencesMoreThanOneEventMoreThanOneMatch() {
                Controller controller = createController();
                createStaff(controller);
                createEvent(
                                controller,
                                new EventTagCollection("hasSocialDistancing=true,hasAirFiltration=true"));
                createAnotherEvent(
                                controller,
                                new EventTagCollection("hasAirFiltration=true,hasSocialDistancing=true"));
                controller.runCommand(new LogoutCommand());
                createConsumer(controller);
                UpdateConsumerProfileCommand updateCmd = new UpdateConsumerProfileCommand(
                                CONSUMER_PASSWORD,
                                "Alice",
                                CONSUMER_EMAIL,
                                "000",
                                "",
                                CONSUMER_PASSWORD,
                                new EventTagCollection("hasSocialDistancing=true,hasAirFiltration=true"));
                controller.runCommand(updateCmd);
                startOutputCapture();
                ListEventsCommand listEventsCmd = new ListEventsCommand(
                                true,
                                false,
                                LocalDate.now().plusDays(2));
                controller.runCommand(listEventsCmd);
                stopOutputCaptureAndCompare("LIST_EVENTS_SUCCESS");
        }
}

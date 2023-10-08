import command.*;
import controller.Controller;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class ListEventsStaffSystemTests extends ConsoleTest {
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
    void listEventsStaffNoEvents() {
        Controller controller = createController();
        createStaff(controller);
        startOutputCapture();
        ListEventsCommand listEventsCmd = new ListEventsCommand(
                true,
                false,
                LocalDate.now().plusDays(2));
        controller.runCommand(listEventsCmd);
        stopOutputCaptureAndCompare("LIST_EVENTS_SUCCESS");

    }

    @Test
    void listEventsStaffEventExists() {
        Controller controller = createStaffAndEvent(10, 48);
        startOutputCapture();
        ListEventsCommand listEventsCmd = new ListEventsCommand(
                true,
                false,
                LocalDate.now().plusDays(2));
        controller.runCommand(listEventsCmd);
        stopOutputCaptureAndCompare("LIST_EVENTS_SUCCESS");
    }
}

import command.*;
import controller.Controller;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

public class AddEventTagSystemTests extends ConsoleTest {
    @Test
    void addTagUserNotStaff() {
        Controller controller = createController();
        createConsumer(controller);
        startOutputCapture();
        AddEventTagCommand addEventTagCmd = new AddEventTagCommand(
                "wheelchairAccessible",
                new HashSet<>(Arrays.asList("true", "false")),
                "false"

        );
        controller.runCommand(addEventTagCmd);
        stopOutputCaptureAndCompare("ADD_EVENT_TAG_USER_NOT_STAFF");
    }

    @Test
    void addTagNameAlreadyExists() {
        Controller controller = createController();
        createStaff(controller);
        startOutputCapture();
        AddEventTagCommand addEventTagCmd = new AddEventTagCommand(
                "hasSocialDistancing",
                new HashSet<>(Arrays.asList("true", "false")),
                "false"

        );
        controller.runCommand(addEventTagCmd);
        stopOutputCaptureAndCompare("ADD_EVENT_TAG_TAG_NAME_EXISTS");
    }

    @Test
    void addTagNotEnoughValues() {
        Controller controller = createController();
        createStaff(controller);
        startOutputCapture();
        AddEventTagCommand addEventTagCmd = new AddEventTagCommand(
                "wheelchairAccessible",
                new HashSet<>(Arrays.asList("false")),
                "false"

        );
        controller.runCommand(addEventTagCmd);
        stopOutputCaptureAndCompare("ADD_EVENT_TAG_LESS_THAN_TWO_TAG_VALUES");
    }

    @Test
    void addTagMoreValues() {
        Controller controller = createController();
        createStaff(controller);
        startOutputCapture();
        AddEventTagCommand addEventTagCmd = new AddEventTagCommand(
                "soundLevel",
                new HashSet<>(Arrays.asList("minimum", "quite loud", "loud", "very loud", "extreme")),
                "loud"

        );
        controller.runCommand(addEventTagCmd);
        stopOutputCaptureAndCompare("ADD_EVENT_TAG_ADD_EVENT_TAG_SUCCESS");
    }

    @Test
    void addTagDefaultNotAValue() {
        Controller controller = createController();
        createStaff(controller);
        startOutputCapture();
        AddEventTagCommand addEventTagCmd = new AddEventTagCommand(
                "wheelchairAccessible",
                new HashSet<>(Arrays.asList("true", "false")),
                "maybe"

        );
        controller.runCommand(addEventTagCmd);
        stopOutputCaptureAndCompare("ADD_EVENT_TAG_DEFAULT_VALUE_NOT_IN_TAG_VALUES");
    }

    @Test
    void addTagNameExistsDefaultNotValue() {
        Controller controller = createController();
        createStaff(controller);
        startOutputCapture();
        AddEventTagCommand addEventTagCmd = new AddEventTagCommand(
                "hasSocialDistancing",
                new HashSet<>(Arrays.asList("true", "false")),
                "maybe"

        );
        controller.runCommand(addEventTagCmd);
        stopOutputCaptureAndCompare("ADD_EVENT_TAG_TAG_NAME_EXISTS");
    }

    @Test
    void addTagSuccessful() {
        Controller controller = createController();
        createStaff(controller);
        startOutputCapture();
        AddEventTagCommand addEventTagCmd = new AddEventTagCommand(
                "wheelchairAccessible",
                new HashSet<>(Arrays.asList("true", "false")),
                "false"

        );
        controller.runCommand(addEventTagCmd);
        stopOutputCaptureAndCompare("ADD_EVENT_TAG_ADD_EVENT_TAG_SUCCESS");
    }
}

import command.RegisterConsumerCommand;
import command.SaveAppStateCommand;
import controller.Controller;
import org.junit.jupiter.api.Test;

public class SaveAppStateSystemTests extends ConsoleTest {

    @Test
    public void staffNotLoggedIn() {
        Controller controller = createController();

        controller.runCommand(new RegisterConsumerCommand("Johnny",
                "johnny@johnny.com",
                "123",
                "55.94462660194542 -3.186744316998206",
                "secure123"));

        String validFileName = "fileForSave.txt";
        startOutputCapture();
        controller.runCommand(new SaveAppStateCommand(validFileName));
        stopOutputCaptureAndCompare(
                "SAVE_APP_STAFF_NOT_LOGGED_IN");
    }

    // Unable to test without using Mockito
    // @Test
    // public void invalidFileName() {
    // Controller controller = createStaffAndEvent(5, 1);

    // startOutputCapture();
    // String invalidFileName = "inva!@£$%^&*(a|idFileName?";
    // controller.runCommand(new SaveAppStateCommand(invalidFileName));
    // stopOutputCaptureAndCompare(
    // "SAVE_APP_INVALID_FILE_PROVIDED");
    // }

    @Test
    public void successfulSaveToFile() {
        Controller controller = createStaffAndEvent(5, 1);

        String validFileName = "fileForSave.txt";
        startOutputCapture();
        controller.runCommand(new SaveAppStateCommand(validFileName));
        stopOutputCaptureAndCompare(
                "SAVE_APP_STATE_SUCCESS");

    }
}

import command.*;
import controller.Controller;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class EditProfileStaffSystemTests extends ConsoleTest {

        @Test
        void updateStaffNotLoggedIn() {
                Controller controller = createController();
                startOutputCapture();
                UpdateStaffProfileCommand updateCmd = new UpdateStaffProfileCommand(
                                STAFF_PASSWORD,
                                STAFF_EMAIL,
                                STAFF_PASSWORD);
                controller.runCommand(updateCmd);
                assertFalse(updateCmd.getResult());
                stopOutputCaptureAndCompare(
                                "USER_UPDATE_PROFILE_NOT_LOGGED_IN");
        }

        @Test
        void updateStaffAsConsumer() {
                Controller controller = createController();
                startOutputCapture();
                createConsumer(controller);
                UpdateStaffProfileCommand updateCmd = new UpdateStaffProfileCommand(
                                CONSUMER_PASSWORD,
                                CONSUMER_EMAIL,
                                CONSUMER_PASSWORD);
                controller.runCommand(updateCmd);
                assertFalse(updateCmd.getResult());
                stopOutputCaptureAndCompare(
                                "REGISTER_CONSUMER_SUCCESS",
                                "CONSUMER_LOGIN_SUCCESS",
                                "USER_UPDATE_PROFILE_NOT_STAFF");
        }
}

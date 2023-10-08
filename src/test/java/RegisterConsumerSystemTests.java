import command.*;
import controller.Controller;

import org.junit.jupiter.api.Test;


public class RegisterConsumerSystemTests extends ConsoleTest{

    @Test
    void registerConsumerUserAlreadyLoggedIn() {
        Controller controller = createController();
        createConsumer(controller);
        startOutputCapture();
        RegisterConsumerCommand registerConsumerCmd = new RegisterConsumerCommand(
                "John",
                "john.doe@gmail.com",
                "0123456789",
                "55.94450196600598 -3.1870153309027947",
                "VeryStrongPassword!00"
        );
        controller.runCommand(registerConsumerCmd);
        stopOutputCaptureAndCompare("REGISTER_CONSUMER_LOGGED_IN");
    }

    @Test
    void registerConsumerNullName() {
        Controller controller = createController();
        startOutputCapture();
        RegisterConsumerCommand registerConsumerCmd = new RegisterConsumerCommand(
                null,
                "john.doe@gmail.com",
                "0123456789",
                "55.94450196600598 -3.1870153309027947",
                "VeryStrongPassword!00"
        );
        controller.runCommand(registerConsumerCmd);
        stopOutputCaptureAndCompare("REGISTER_CONSUMER_FIELDS_CANNOT_BE_NULL");
    }

    @Test
    void registerConsumerNullPhoneNumber() {
        Controller controller = createController();
        startOutputCapture();
        RegisterConsumerCommand registerConsumerCmd = new RegisterConsumerCommand(
                "John",
                "john.doe@gmail.com",
                null,
                "55.94450196600598 -3.1870153309027947",
                "VeryStrongPassword!00"
        );
        controller.runCommand(registerConsumerCmd);
        stopOutputCaptureAndCompare("REGISTER_CONSUMER_FIELDS_CANNOT_BE_NULL");
    }

    @Test
    void registerConsumerEmailDuplicate() {
        Controller controller = createController();
        createConsumer(controller);
        controller.runCommand(new LogoutCommand());
        startOutputCapture();
        RegisterConsumerCommand registerConsumerCmd = new RegisterConsumerCommand(
                "John",
                CONSUMER_EMAIL,
                "0123456789",
                "55.94450196600598 -3.1870153309027947",
                "VeryStrongPassword!00"
        );
        controller.runCommand(registerConsumerCmd);
        stopOutputCaptureAndCompare("REGISTER_CONSUMER_EMAIL_ALREADY_REGISTERED");
    }

    @Test
    void registerConsumerInvalidAddressFormat() {
        Controller controller = createController();
        startOutputCapture();
        RegisterConsumerCommand registerConsumerCmd = new RegisterConsumerCommand(
                "John",
                "john.doe@gmail.com",
                "0123456789",
                "55.94450196600598, -3.1870153309027947",
                "VeryStrongPassword!00"
        );
        controller.runCommand(registerConsumerCmd);
        stopOutputCaptureAndCompare("REGISTER_CONSUMER_INVALID_ADDRESS_FORMAT");
    }

    @Test
    void registerConsumerAddressNotLongLat() {
        Controller controller = createController();
        startOutputCapture();
        RegisterConsumerCommand registerConsumerCmd = new RegisterConsumerCommand(
                "John",
                "john.doe@gmail.com",
                "0123456789",
                "11 Crichton St, Newington, Edinburgh EH8 9LE",
                "VeryStrongPassword!00"
        );
        controller.runCommand(registerConsumerCmd);
        stopOutputCaptureAndCompare("REGISTER_CONSUMER_INVALID_ADDRESS_FORMAT");
    }

    @Test
    void registerConsumerAddressOutOfBounds() {
        Controller controller = createController();
        startOutputCapture();
        RegisterConsumerCommand registerConsumerCmd = new RegisterConsumerCommand(
                "John",
                "john.doe@gmail.com",
                "0123456789",
                "34.69045232342335 135.5647707972591",
                "VeryStrongPassword!00"
        );
        controller.runCommand(registerConsumerCmd);
        stopOutputCaptureAndCompare("REGISTER_CONSUMER_ADDRESS_OUT_OF_BOUNDS");
    }

    @Test
    void registerConsumerPasswordExists() {
        Controller controller = createController();
        startOutputCapture();
        RegisterConsumerCommand registerConsumerCmd = new RegisterConsumerCommand(
                "John",
                "john.doe@gmail.com",
                "0123456789",
                "55.94450196600598 -3.1870153309027947",
                CONSUMER_PASSWORD
        );
        controller.runCommand(registerConsumerCmd);
        stopOutputCaptureAndCompare("REGISTER_CONSUMER_SUCCESS", "CONSUMER_LOGIN_SUCCESS");
    }

    @Test
    void registerConsumerSuccess() {
        Controller controller = createController();
        startOutputCapture();
        RegisterConsumerCommand registerConsumerCmd = new RegisterConsumerCommand(
                "John",
                "john.doe@gmail.com",
                "0123456789",
                "55.94450196600598 -3.1870153309027947",
                "VeryStrongPassword!00"
        );
        controller.runCommand(registerConsumerCmd);
        stopOutputCaptureAndCompare("REGISTER_CONSUMER_SUCCESS", "CONSUMER_LOGIN_SUCCESS");
    }
}

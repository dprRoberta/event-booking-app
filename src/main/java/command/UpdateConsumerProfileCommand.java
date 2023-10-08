package command;

import controller.Context;
import model.*;
import view.IView;

import java.util.Map;
import java.util.Set;

/**
 * {@link UpdateConsumerProfileCommand} allows {@link Consumer}s to update their
 * account details and change the default
 * Covid-19 preferences.
 */
public class UpdateConsumerProfileCommand extends UpdateProfileCommand {
    private final String oldPassword;
    private final String newName;
    private final String newEmail;
    private final String newPhoneNumber;
    private final String newAddress;
    private final String newPassword;
    private final EventTagCollection newPreferences;

    /**
     * @param oldPassword    account password before the change, required for extra
     *                       security verification. Must not be null
     * @param newName        full name of the person holding this account. Must not
     *                       be null
     * @param newEmail       email address to use for this account. Must not be null
     * @param newPhoneNumber phone number to use for this account (used for
     *                       notifying the {@link Consumer} of any
     *                       {@link Event} cancellations that they have bookings
     *                       for). Must not be null
     * @param newAddress     updated Consumer address. Optional and may be null
     * @param newPassword    password to use for this account. Must not be null
     * @param newPreferences a {@link EventTagCollection} object of Covid-19
     *                       preferences, used for filtering events
     *                       in the {@link ListEventsCommand}. If null, default
     *                       preferences are applied
     */
    public UpdateConsumerProfileCommand(String oldPassword,
            String newName,
            String newEmail,
            String newPhoneNumber,
            String newAddress,
            String newPassword,
            EventTagCollection newPreferences) {
        this.oldPassword = oldPassword;
        this.newName = newName;
        this.newEmail = newEmail;
        this.newPhoneNumber = newPhoneNumber;
        this.newAddress = newAddress;
        this.newPassword = newPassword;
        this.newPreferences = newPreferences;
    }

    /**
     * @param context object that provides access to global application state
     * @param view    allows passing information to the user interface
     * @verifies.that oldPassword, newName, newEmail, newPhoneNumber, and
     *                newPassword are all not null
     * @verifies.that current user is logged in
     * @verifies.that oldPassword matches the current user's password
     * @verifies.that there is no other user already registered with the same email
     *                address as newEmail
     * @verifies.that currently logged-in user is a Consumer
     */
    @Override
    public void execute(Context context, IView view) {
        if (oldPassword == null || newName == null || newEmail == null || newPhoneNumber == null
                || newPassword == null) {
            view.displayFailure(
                    "UpdateConsumerProfileCommand",
                    LogStatus.USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL,
                    Map.of("oldPassword", "***",
                            "newName", String.valueOf(newName),
                            "newEmail", String.valueOf(newEmail),
                            "newPhoneNumber", String.valueOf(newPhoneNumber),
                            "newPassword", "***",
                            "newPreferences", newPreferences));
            successResult = false;
            return;
        }

        if (isProfileUpdateInvalid(context, view, oldPassword, newEmail)) {
            successResult = false;
            return;
        }

        User currentUser = context.getUserState().getCurrentUser();

        if (!(currentUser instanceof Consumer)) {
            view.displayFailure(
                    "UpdateConsumerProfileCommand",
                    LogStatus.USER_UPDATE_PROFILE_NOT_CONSUMER);
            successResult = false;
            return;
        }

        Map<String, EventTag> possibleTags = context.getEventState().getPossibleTag();
        Set<String> possibleTagNames = possibleTags.keySet();
        for (String name : newPreferences.getTags().keySet()) {
            if (!(possibleTagNames.contains(name))) {
                view.displayFailure("UpdateConsumerProfileCommand",
                        LogStatus.USER_UPDATE_PROFILE_TAG_NAME_NOT_KNOWN,
                        Map.of("newPreferences", newPreferences));
                successResult = false;
                return;
            } else {
                String tagValue = newPreferences.getValueFor(name);
                Set<String> possibleTagValues = possibleTags.get(name).getValues();
                if (!(possibleTagValues.contains(tagValue))) {
                    view.displayFailure("UpdateConsumerProfileCommand",
                            LogStatus.USER_UPDATE_PROFILE_TAG_VALUE_NOT_KNOWN,
                            Map.of("newPreferences", newPreferences));
                    successResult = false;
                    return;
                }
            }
        }

        changeUserEmail(context, newEmail);
        currentUser.updatePassword(newPassword);
        Consumer consumer = (Consumer) currentUser;
        consumer.setName(newName);
        consumer.setPhoneNumber(newPhoneNumber);
        consumer.setAddress(newAddress);
        consumer.setPreferences(newPreferences);

        view.displaySuccess(
                "UpdateConsumerProfileCommand",
                LogStatus.USER_UPDATE_PROFILE_SUCCESS,
                Map.of("newName", newName,
                        "newEmail", newEmail,
                        "newPhoneNumber", newPhoneNumber,
                        "newAddress", String.valueOf(newAddress),
                        "newPassword", "***",
                        "preferences", newPreferences));
        successResult = true;
    }

    private enum LogStatus {
        USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL,
        USER_UPDATE_PROFILE_NOT_CONSUMER,
        USER_UPDATE_PROFILE_SUCCESS,
        USER_UPDATE_PROFILE_TAG_NAME_NOT_KNOWN,
        USER_UPDATE_PROFILE_TAG_VALUE_NOT_KNOWN,
    }
}

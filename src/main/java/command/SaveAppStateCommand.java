package command;

import controller.Context;
import model.Staff;
import model.User;
import view.IView;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class SaveAppStateCommand implements ICommand<Boolean> {
    private Boolean successResult;
    private final String fileName;

    public SaveAppStateCommand(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param context object that provides access to global application state
     * @param view    allows passing information to the user interface
     * @verifies.that the current user is logged in
     */
    @Override
    public void execute(Context context, IView view) {
        User currentUser = context.getUserState().getCurrentUser();
        if (!(currentUser instanceof Staff)) {
            view.displayFailure("SaveAppStateCommand",
                    LogStatus.SAVE_APP_STAFF_NOT_LOGGED_IN,
                    Map.of("currentUser", currentUser != null ? currentUser : "none"));
            successResult = false;
            return;
        }

        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fos);
            out.writeObject(context);
            out.close();
            successResult = true;
            view.displaySuccess(
                    "SaveAppStateCommand",
                    LogStatus.SAVE_APP_STATE_SUCCESS,
                    Map.of("filename", fileName));
        } catch (IOException e) {
            // Consider using other displayFailure method and provide more information
            view.displayFailure("SaveAppStateCommand",
                    LogStatus.SAVE_APP_INVALID_FILE_PROVIDED,
                    Map.of("filename", fileName));
            successResult = false;
        }
    }

    /**
     * @return True if successful and false otherwise
     */
    @Override
    public Boolean getResult() {
        return successResult;
    }

    private enum LogStatus {
        SAVE_APP_STATE_SUCCESS,
        SAVE_APP_STAFF_NOT_LOGGED_IN,
        SAVE_APP_INVALID_FILE_PROVIDED
    }
}

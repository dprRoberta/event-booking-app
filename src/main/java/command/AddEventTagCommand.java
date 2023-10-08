package command;

import controller.Context;
import model.*;
import view.IView;

import java.util.Map;
import java.util.Set;

public class AddEventTagCommand implements ICommand<EventTag> {
    private final String tagName;
    private final Set<String> tagValues;
    private final String defaultValue;
    private EventTag eventTagResult;

    public AddEventTagCommand(String tagName, Set<String> tagValues, String defaultValue) {
        this.tagName = tagName;
        this.tagValues = tagValues;
        this.defaultValue = defaultValue;
    }

    @Override
    public void execute(Context context, IView view) {
        User currentUser = context.getUserState().getCurrentUser();
        if (!(currentUser instanceof Staff)) {
            view.displayFailure("AddEventTagCommand",
                    LogStatus.ADD_EVENT_TAG_USER_NOT_STAFF,
                    Map.of("currentUser", currentUser != null ? currentUser : "none"));
            eventTagResult = null;
            return;
        }

        Map<String, EventTag> possibleTags = context.getEventState().getPossibleTag();
        if (possibleTags.containsKey(tagName)) {
            view.displayFailure("AddEventTagCommand",
                    LogStatus.ADD_EVENT_TAG_TAG_NAME_EXISTS,
                    Map.of("tagName", tagName));
            eventTagResult = null;
            return;
        }

        if (tagValues.size() < 2) {
            view.displayFailure("AddEventTagCommand",
                    LogStatus.ADD_EVENT_TAG_LESS_THAN_TWO_TAG_VALUES,
                    Map.of("tagValues", tagValues));
            eventTagResult = null;
            return;
        }

        if (!(tagValues.contains(defaultValue))) {
            view.displayFailure("AddEventTagCommand",
                    LogStatus.ADD_EVENT_TAG_DEFAULT_VALUE_NOT_IN_TAG_VALUES,
                    Map.of("tagValues", tagValues,
                            "defaultValue", defaultValue));
            eventTagResult = null;
            return;
        }

        EventTag eventTag = context.getEventState().createEventTag(tagName, tagValues, defaultValue);
        view.displaySuccess("AddEventCommand",
                LogStatus.ADD_EVENT_TAG_ADD_EVENT_TAG_SUCCESS,
                Map.of("tagName", tagName,
                        "tagValues", tagValues,
                        "defaultValue", defaultValue));
        eventTagResult = eventTag;
    }

    @Override
    public EventTag getResult() {
        return eventTagResult;
    }

    private enum LogStatus {
        ADD_EVENT_TAG_USER_NOT_STAFF,
        ADD_EVENT_TAG_TAG_NAME_EXISTS,
        ADD_EVENT_TAG_LESS_THAN_TWO_TAG_VALUES,
        ADD_EVENT_TAG_DEFAULT_VALUE_NOT_IN_TAG_VALUES,
        ADD_EVENT_TAG_ADD_EVENT_TAG_SUCCESS,
    }
}

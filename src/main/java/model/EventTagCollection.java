package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EventTagCollection implements Serializable {
    private final Map<String, String> tags;

    public EventTagCollection() {
        this.tags = new HashMap<>();
    }

    public EventTagCollection(String tagValues) {
        this.tags = new HashMap<>();

        if (tagValues == null || tagValues.isEmpty()) {
            return;
        }

        // Split the string into individual tags
        // e.g. "city=Edinburgh,country=Scotland,type=Music" ->
        // ["city=Edinburgh","country=Scotland", "type=Music"]
        String[] splitTags = tagValues.split(",");

        for (String tag : splitTags) {
            // Split the tag into its name and value
            String[] splitTag = tag.split("=");

            String tagName;
            String tagValue;
            try {
                tagName = splitTag[0];
                tagValue = splitTag[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid tag: " + tag);
                continue;
            }

            if (tagName == null || tagName.isEmpty() || tagValue == null || tagValue.isEmpty()) {
                // We should be using a proper logging framework here, but for now we'll just
                // print to the console
                System.out.println(tagValues + " is not a valid tag");
                continue;
            }

            tags.put(tagName, tagValue);
        }
    }

    public String getValueFor(String tagName) {
        return tags.get(tagName);
    }

    public Map<String, String> getTags() {
        return tags;
    }

    // Required method for load command
    @Override
    public boolean equals(Object o) {
        // Object is equal to itself
        if (o == this) {
            return true;
        }

        // Check if object given is also an event
        if (!(o instanceof EventTagCollection)) {
            return false;
        }

        // Convert object to event so we can apply our check
        EventTagCollection otherEventTagCollection = (EventTagCollection) o;

        // Check all keys and values are the same in both
        if (this.tags.keySet().size() == otherEventTagCollection.tags.keySet().size()) {
            for (String tagName : this.tags.keySet()) {
                if (otherEventTagCollection.tags.containsKey(tagName)) {
                    if (!this.tags.get(tagName).equals(otherEventTagCollection.tags.get(tagName))) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
}

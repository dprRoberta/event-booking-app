package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class EventTag implements Serializable {
    public final Set<String> values;
    public final String defaultValue;

    public EventTag(Set<String> values, String defaultValue) {
        this.values = new HashSet<>();
        this.values.addAll(values);
        this.defaultValue = defaultValue;
    }

    public Set<String> getValues() {
        return values;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    // Required method for load command
    @Override
    public boolean equals(Object o) {
        // Object is equal to itself
        if (o == this) {
            return true;
        }

        // Check if object given is also an event tag
        if (!(o instanceof EventTag)) {
            return false;
        }

        // Convert object to event tag so we can apply our check
        EventTag otherEventTag = (EventTag) o;

        // Check values and default value are equal
        return (this.getValues().equals(otherEventTag.getValues()) &&
                this.getDefaultValue().equals(otherEventTag.getDefaultValue()));
    }
}

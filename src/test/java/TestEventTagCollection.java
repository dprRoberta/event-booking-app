
import model.Event;
import model.EventTagCollection;
import model.EventTag;
import model.EventType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

public class TestEventTagCollection {

  @Test
  public void testParsingEventTags() {
    EventTagCollection eventTags = new EventTagCollection("city=Edinburgh,country=Scotland,type=Music");

    assertEquals("Edinburgh", eventTags.getValueFor("city"));
    assertEquals("Scotland", eventTags.getValueFor("country"));
    assertEquals("Music", eventTags.getValueFor("type"));
  }

  @Test
  public void testNoArgumentPassedIn() {
    EventTagCollection eventTags = new EventTagCollection();

    assertEquals(0, eventTags.getTags().size());
  }

  @Test
  public void testNullPassedIn() {
    EventTagCollection eventTags = new EventTagCollection(null);

    assertEquals(0, eventTags.getTags().size());
  }

  @Test
  public void testEmptyStringPassedIn() {
    EventTagCollection eventTags = new EventTagCollection("");

    assertEquals(0, eventTags.getTags().size());
  }

  @Test
  public void testEmptyStringPassedInForTagValue() {
    EventTagCollection eventTags = new EventTagCollection("city=Edinburgh,country=,type=Music");

    assertEquals("Edinburgh", eventTags.getValueFor("city"));
    assertEquals(null, eventTags.getValueFor("country"));
    assertEquals("Music", eventTags.getValueFor("type"));
  }

  @Test
  public void testEmptyStringPassedInForTagKey() {
    EventTagCollection eventTags = new EventTagCollection("=Edinburgh,country=Scotland,type=Music");

    assertEquals("Scotland", eventTags.getValueFor("country"));
    assertEquals("Music", eventTags.getValueFor("type"));
    assertEquals(2, eventTags.getTags().size());
  }

}

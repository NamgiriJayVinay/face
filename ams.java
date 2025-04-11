package com.example.app; // Replace with your actual package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for GsonTypeConverter
 * Contains 7 positive test cases and 3 negative test cases
 */
public class GsonTypeConverterTest {

    // Sample Event class to use in tests
    // Note: This should match your actual Event class structure
    private static class Event {
        private String id;
        private String name;
        private long timestamp;
        private String description;

        public Event() {
            // Default constructor needed for Gson
        }

        public Event(String id, String name, long timestamp, String description) {
            this.id = id;
            this.name = name;
            this.timestamp = timestamp;
            this.description = description;
        }

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Event event = (Event) o;
            return timestamp == event.timestamp &&
                   (id == null ? event.id == null : id.equals(event.id)) &&
                   (name == null ? event.name == null : name.equals(event.name)) &&
                   (description == null ? event.description == null : description.equals(event.description));
        }
    }

    // Test data
    private Event event1;
    private Event event2;
    private Event event3;
    private List<Event> sampleEventList;

    @Before
    public void setUp() {
        // Initialize test events
        event1 = new Event("1", "Event 1", System.currentTimeMillis(), "First test event");
        event2 = new Event("2", "Event 2", System.currentTimeMillis() + 1000, "Second test event");
        event3 = new Event("3", "Event 3", System.currentTimeMillis() + 2000, "Third test event with special chars: !@#$%^&*()");
        
        // Create sample event list
        sampleEventList = Arrays.asList(event1, event2, event3);
    }

    /**
     * POSITIVE TEST CASE 1: Convert list of events to JSON string and back
     */
    @Test
    public void testRoundTripConversion() {
        // Convert list to JSON string
        String json = GsonTypeConverter.fromEventListToString(sampleEventList);
        
        // Convert JSON string back to list
        List<Event> convertedList = GsonTypeConverter.fromStringToEventList(json);
        
        // Verify the round-trip conversion preserves the events
        assertNotNull("Converted list should not be null", convertedList);
        assertEquals("List size should be preserved", sampleEventList.size(), convertedList.size());
        
        // Compare events
        for (int i = 0; i < sampleEventList.size(); i++) {
            Event original = sampleEventList.get(i);
            Event converted = convertedList.get(i);
            
            assertEquals("Event IDs should match", original.getId(), converted.getId());
            assertEquals("Event names should match", original.getName(), converted.getName());
            assertEquals("Event timestamps should match", original.getTimestamp(), converted.getTimestamp());
            assertEquals("Event descriptions should match", original.getDescription(), converted.getDescription());
        }
    }

    /**
     * POSITIVE TEST CASE 2: Convert empty list to JSON string and back
     */
    @Test
    public void testEmptyListConversion() {
        // Create empty list
        List<Event> emptyList = Collections.emptyList();
        
        // Convert empty list to JSON string
        String json = GsonTypeConverter.fromEventListToString(emptyList);
        
        // Convert JSON string back to list
        List<Event> convertedList = GsonTypeConverter.fromStringToEventList(json);
        
        // Verify the conversion works with empty lists
        assertNotNull("Converted list should not be null", convertedList);
        assertTrue("Converted list should be empty", convertedList.isEmpty());
    }

    /**
     * POSITIVE TEST CASE 3: Convert single event list to JSON string and back
     */
    @Test
    public void testSingleEventConversion() {
        // Create list with single event
        List<Event> singleEventList = Collections.singletonList(event1);
        
        // Convert single event list to JSON string
        String json = GsonTypeConverter.fromEventListToString(singleEventList);
        
        // Convert JSON string back to list
        List<Event> convertedList = GsonTypeConverter.fromStringToEventList(json);
        
        // Verify the conversion works with single event
        assertNotNull("Converted list should not be null", convertedList);
        assertEquals("List should contain 1 event", 1, convertedList.size());
        assertEquals("Event should be preserved correctly", event1.getId(), convertedList.get(0).getId());
        assertEquals("Event should be preserved correctly", event1.getName(), convertedList.get(0).getName());
    }

    /**
     * POSITIVE TEST CASE 4: Test with events containing special characters
     */
    @Test
    public void testSpecialCharactersInEvents() {
        // Create event with special characters
        Event specialEvent = new Event(
            "special-id",
            "Special Event Name !@#$%^&*()_+{}|:\"<>?[]\\;',./",
            System.currentTimeMillis(),
            "Description with unicode characters: ñáéíóúüÑÁÉÍÓÚÜ日本語"
        );
        
        // Convert single special event to JSON and back
        List<Event> specialEventList = Collections.singletonList(specialEvent);
        String json = GsonTypeConverter.fromEventListToString(specialEventList);
        List<Event> convertedList = GsonTypeConverter.fromStringToEventList(json);
        
        // Verify special characters are preserved
        assertNotNull("Converted list should not be null", convertedList);
        assertEquals("List should contain 1 event", 1, convertedList.size());
        assertEquals("Event ID should be preserved", specialEvent.getId(), convertedList.get(0).getId());
        assertEquals("Event name with special chars should be preserved", 
                     specialEvent.getName(), convertedList.get(0).getName());
        assertEquals("Event description with unicode should be preserved", 
                     specialEvent.getDescription(), convertedList.get(0).getDescription());
    }

    /**
     * POSITIVE TEST CASE 5: Test with large list of events
     */
    @Test
    public void testLargeListConversion() {
        // Create a large list of events (100 events)
        List<Event> largeEventList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            largeEventList.add(new Event(
                "id-" + i,
                "Event " + i,
                System.currentTimeMillis() + i,
                "Description for event " + i
            ));
        }
        
        // Convert large list to JSON and back
        String json = GsonTypeConverter.fromEventListToString(largeEventList);
        List<Event> convertedList = GsonTypeConverter.fromStringToEventList(json);
        
        // Verify large list conversion
        assertNotNull("Converted list should not be null", convertedList);
        assertEquals("List size should be preserved", largeEventList.size(), convertedList.size());
        
        // Check a few random elements
        assertEquals("First element should match", 
                     largeEventList.get(0).getId(), convertedList.get(0).getId());
        assertEquals("50th element should match", 
                     largeEventList.get(50).getId(), convertedList.get(50).getId());
        assertEquals("Last element should match", 
                     largeEventList.get(99).getId(), convertedList.get(99).getId());
    }

    /**
     * POSITIVE TEST CASE 6: Test with events containing null fields
     */
    @Test
    public void testEventsWithNullFields() {
        // Create events with null fields
        Event nullFieldsEvent1 = new Event("null-id-1", null, 0, null);
        Event nullFieldsEvent2 = new Event(null, "Event with null ID", System.currentTimeMillis(), "Description");
        
        List<Event> nullFieldsList = Arrays.asList(nullFieldsEvent1, nullFieldsEvent2);
        
        // Convert to JSON and back
        String json = GsonTypeConverter.fromEventListToString(nullFieldsList);
        List<Event> convertedList = GsonTypeConverter.fromStringToEventList(json);
        
        // Verify null fields are preserved
        assertNotNull("Converted list should not be null", convertedList);
        assertEquals("List size should be preserved", nullFieldsList.size(), convertedList.size());
        
        // Check null fields are preserved
        assertNull("Name should be null", convertedList.get(0).getName());
        assertNull("Description should be null", convertedList.get(0).getDescription());
        assertNull("ID should be null", convertedList.get(1).getId());
    }

    /**
     * POSITIVE TEST CASE 7: Test with Gson's pretty printing (manually verify JSON format)
     */
    @Test
    public void testGsonPrettyPrinting() {
        // We'll just verify that the conversion works and the JSON is valid
        // The actual formatting would need visual inspection
        
        String json = GsonTypeConverter.fromEventListToString(sampleEventList);
        List<Event> convertedList = GsonTypeConverter.fromStringToEventList(json);
        
        // Verify conversion worked
        assertNotNull("Converted list should not be null", convertedList);
        assertEquals("List size should be preserved", sampleEventList.size(), convertedList.size());
        
        // This test does not check the pretty printing format as it would require
        // looking at the actual JSON string, but it ensures the conversion works
    }

    /**
     * NEGATIVE TEST CASE 1: Convert null list to JSON string
     */
    @Test
    public void testNullListToJson() {
        // Convert null list to JSON
        String json = GsonTypeConverter.fromEventListToString(null);
        
        // Expect "null" string or empty string depending on Gson configuration
        assertTrue("JSON should be null or empty for null input", 
                   json == null || json.equals("null"));
    }

    /**
     * NEGATIVE TEST CASE 2: Convert invalid JSON string to event list
     */
    @Test
    public void testInvalidJsonToEventList() {
        try {
            // Try to convert invalid JSON
            List<Event> convertedList = GsonTypeConverter.fromStringToEventList("{invalid json}");
            
            // If no exception thrown, the test should fail
            // (unless Gson is configured to be lenient)
            assertNull("Should return null for invalid JSON", convertedList);
            
        } catch (Exception e) {
            // Expected behavior - Gson typically throws an exception for invalid JSON
            assertTrue("Exception should be from Gson", 
                      e.getMessage().contains("com.google.gson"));
        }
    }

    /**
     * NEGATIVE TEST CASE 3: Convert null JSON string to event list
     */
    @Test
    public void testNullJsonToEventList() {
        // Convert null JSON string to list
        List<Event> convertedList = GsonTypeConverter.fromStringToEventList(null);
        
        // Expect null list or empty list depending on implementation
        assertTrue("Result should be null or empty for null input", 
                   convertedList == null || convertedList.isEmpty());
    }
}
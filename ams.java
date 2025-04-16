package com.example.app; // Replace with your actual package name

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

/**
 * Test class for Event
 * Contains 3 positive test cases and 15 negative test cases
 */
public class EventTest {

    // Test constants
    private static final String VALID_PERMISSION = "android.permission.CAMERA";
    private static final String VALID_TYPE = "PERMISSION_USED";
    private static final String VALID_STATE = "ACTIVE";
    private static final double VALID_NUMBER = 1.0;
    private static final long VALID_TIMESTAMP = 1650000000000L;
    private static final double VALID_SCORE = 0.85;
    private static final Float VALID_RECONSTRUCTION_ERROR = 0.12f;
    private static final Float VALID_RECONSTRUCTION_THRESHOLD = 0.5f;
    
    private Event event;
    
    @BeforeEach
    public void setUp() {
        // Initialize with valid values before each test
        event = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            VALID_NUMBER,
            VALID_TIMESTAMP,
            VALID_SCORE,
            VALID_RECONSTRUCTION_ERROR,
            VALID_RECONSTRUCTION_THRESHOLD
        );
    }

    /* ========== POSITIVE TEST CASES ========== */

    /**
     * POSITIVE TEST CASE 1: Verify constructor initializes object with correct values
     */
    @Test
    public void testConstructorInitializesWithCorrectValues() {
        // Verify all fields have expected values
        assertEquals(VALID_PERMISSION, event.getPermission(), "Permission should match constructor value");
        assertEquals(VALID_TYPE, event.getType(), "Type should match constructor value");
        assertEquals(VALID_STATE, event.getState(), "State should match constructor value");
        assertEquals(VALID_NUMBER, event.getNumber(), "Number should match constructor value");
        assertEquals(VALID_TIMESTAMP, event.getTotalBackgroundUsage(), "Background usage should be zero by default");
        assertEquals(VALID_SCORE, event.getScore(), "Score should match constructor value");
        assertEquals(VALID_RECONSTRUCTION_ERROR, event.getReconstructionError(), "Reconstruction error should match constructor value");
        assertEquals(VALID_RECONSTRUCTION_THRESHOLD, event.getReconstructionThreshold(), "Reconstruction threshold should match constructor value");
        assertEquals(0, event.getTotalBackgroundUsage(), "Background usage should be zero by default");
        assertEquals(0, event.getTotalForegroundUsage(), "Foreground usage should be zero by default");
    }

    /**
     * POSITIVE TEST CASE 2: Verify setter methods update values correctly
     */
    @Test
    public void testSettersUpdateValuesCorrectly() {
        // New test values
        double newScore = 0.95;
        Float newReconstructionError = 0.25f;
        Float newReconstructionThreshold = 0.75f;
        long newBackgroundUsage = 3600000L; // 1 hour in milliseconds
        long newForegroundUsage = 1800000L; // 30 minutes in milliseconds
        
        // Set new values
        event.setScore(newScore);
        event.setReconstructionError(newReconstructionError);
        event.setReconstructionThreshold(newReconstructionThreshold);
        event.setTotalBackgroundUsage(newBackgroundUsage);
        event.setTotalForegroundUsage(newForegroundUsage);
        
        // Verify values were updated
        assertEquals(newScore, event.getScore(), "Score should be updated");
        assertEquals(newReconstructionError, event.getReconstructionError(), "Reconstruction error should be updated");
        assertEquals(newReconstructionThreshold, event.getReconstructionThreshold(), "Reconstruction threshold should be updated");
        assertEquals(newBackgroundUsage, event.getTotalBackgroundUsage(), "Background usage should be updated");
        assertEquals(newForegroundUsage, event.getTotalForegroundUsage(), "Foreground usage should be updated");
    }

    /**
     * POSITIVE TEST CASE 3: Test with valid null values for nullable fields
     */
    @Test
    public void testWithNullValues() {
        // Create event with null values for nullable fields
        Event nullableEvent = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            VALID_NUMBER,
            VALID_TIMESTAMP,
            VALID_SCORE,
            null, // reconstructionError
            null  // reconstructionThreshold
        );
        
        // Verify nullable fields are null
        assertNull(nullableEvent.getReconstructionError(), "Reconstruction error should be null");
        assertNull(nullableEvent.getReconstructionThreshold(), "Reconstruction threshold should be null");
        
        // Verify non-nullable fields still have values
        assertEquals(VALID_PERMISSION, nullableEvent.getPermission(), "Permission should have value");
    }

    /* ========== NEGATIVE TEST CASES ========== */

    /**
     * NEGATIVE TEST CASE 1: Test with negative numeric values
     */
    @Test
    public void testWithNegativeNumericValues() {
        // Create event with negative values
        Event negativeEvent = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            -10.5, // negative number
            -1000L, // negative timestamp
            -0.75, // negative score
            -0.3f, // negative reconstructionError
            -0.5f  // negative reconstructionThreshold
        );
        
        // Verify negative values were accepted
        assertEquals(-10.5, negativeEvent.getNumber(), "Negative number should be accepted");
        assertEquals(-0.75, negativeEvent.getScore(), "Negative score should be accepted");
        assertEquals(-0.3f, negativeEvent.getReconstructionError(), "Negative reconstruction error should be accepted");
        assertEquals(-0.5f, negativeEvent.getReconstructionThreshold(), "Negative reconstruction threshold should be accepted");
        
        // Test with negative usage values
        negativeEvent.setTotalBackgroundUsage(-5000L);
        negativeEvent.setTotalForegroundUsage(-2500L);
        
        assertEquals(-5000L, negativeEvent.getTotalBackgroundUsage(), "Negative background usage should be accepted");
        assertEquals(-2500L, negativeEvent.getTotalForegroundUsage(), "Negative foreground usage should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 2: Test with empty strings
     */
    @Test
    public void testWithEmptyStrings() {
        // Create event with empty strings
        Event emptyStringsEvent = new Event(
            "", // empty permission
            "", // empty type
            "", // empty state
            VALID_NUMBER,
            VALID_TIMESTAMP,
            VALID_SCORE,
            VALID_RECONSTRUCTION_ERROR,
            VALID_RECONSTRUCTION_THRESHOLD
        );
        
        // Verify empty strings were accepted
        assertEquals("", emptyStringsEvent.getPermission(), "Empty permission should be accepted");
        assertEquals("", emptyStringsEvent.getType(), "Empty type should be accepted");
        assertEquals("", emptyStringsEvent.getState(), "Empty state should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 3: Test with null strings
     */
    @Test
    public void testWithNullStrings() {
        // Create event with null strings
        Event nullStringsEvent = new Event(
            null, // null permission
            null, // null type
            null, // null state
            VALID_NUMBER,
            VALID_TIMESTAMP,
            VALID_SCORE,
            VALID_RECONSTRUCTION_ERROR,
            VALID_RECONSTRUCTION_THRESHOLD
        );
        
        // Verify null strings were accepted
        assertNull(nullStringsEvent.getPermission(), "Null permission should be accepted");
        assertNull(nullStringsEvent.getType(), "Null type should be accepted");
        assertNull(nullStringsEvent.getState(), "Null state should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 4: Test with extreme numeric values
     */
    @Test
    public void testWithExtremeValues() {
        // Create event with extreme values
        Event extremeEvent = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            Double.MAX_VALUE,  // max double
            Long.MAX_VALUE,    // max long
            Double.MAX_VALUE,  // max double
            Float.MAX_VALUE,   // max float
            Float.MAX_VALUE    // max float
        );
        
        // Verify extreme values were accepted
        assertEquals(Double.MAX_VALUE, extremeEvent.getNumber(), "Maximum double should be accepted");
        assertEquals(Long.MAX_VALUE, extremeEvent.getTotalBackgroundUsage(), "Maximum long should be accepted");
        assertEquals(Double.MAX_VALUE, extremeEvent.getScore(), "Maximum double should be accepted");
        assertEquals(Float.MAX_VALUE, extremeEvent.getReconstructionError(), "Maximum float should be accepted");
        assertEquals(Float.MAX_VALUE, extremeEvent.getReconstructionThreshold(), "Maximum float should be accepted");
        
        // Test with minimum values
        extremeEvent = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            Double.MIN_VALUE,  // min positive double
            Long.MIN_VALUE,    // min long
            Double.MIN_VALUE,  // min positive double
            Float.MIN_VALUE,   // min positive float
            Float.MIN_VALUE    // min positive float
        );
        
        // Verify minimum values were accepted
        assertEquals(Double.MIN_VALUE, extremeEvent.getNumber(), "Minimum double should be accepted");
        assertEquals(Long.MIN_VALUE, extremeEvent.getTotalBackgroundUsage(), "Minimum long should be accepted");
        assertEquals(Double.MIN_VALUE, extremeEvent.getScore(), "Minimum double should be accepted");
        assertEquals(Float.MIN_VALUE, extremeEvent.getReconstructionError(), "Minimum float should be accepted");
        assertEquals(Float.MIN_VALUE, extremeEvent.getReconstructionThreshold(), "Minimum float should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 5: Test with special characters in strings
     */
    @Test
    public void testWithSpecialCharacters() {
        // Create event with special characters
        String specialPermission = "android.permission.CAMERA!@#$%^&*()_+{}|:\"<>?[]\\;',./";
        String specialType = "PERMISSION_USED<>?";
        String specialState = "ACTIVE-_+=";
        
        Event specialCharsEvent = new Event(
            specialPermission,
            specialType,
            specialState,
            VALID_NUMBER,
            VALID_TIMESTAMP,
            VALID_SCORE,
            VALID_RECONSTRUCTION_ERROR,
            VALID_RECONSTRUCTION_THRESHOLD
        );
        
        // Verify special characters were accepted
        assertEquals(specialPermission, specialCharsEvent.getPermission(), "Special characters in permission should be accepted");
        assertEquals(specialType, specialCharsEvent.getType(), "Special characters in type should be accepted");
        assertEquals(specialState, specialCharsEvent.getState(), "Special characters in state should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 6: Test with NaN and Infinity values
     */
    @Test
    public void testWithNaNAndInfinity() {
        // Create event with NaN and Infinity
        Event nanInfinityEvent = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            Double.NaN,            // NaN
            VALID_TIMESTAMP,
            Double.POSITIVE_INFINITY, // +Infinity
            Float.NaN,             // NaN
            Float.NEGATIVE_INFINITY  // -Infinity
        );
        
        // Verify NaN and Infinity were accepted
        assertTrue(Double.isNaN(nanInfinityEvent.getNumber()), "NaN should be accepted for number");
        assertTrue(Double.isInfinite(nanInfinityEvent.getScore()), "Infinity should be accepted for score");
        assertTrue(Float.isNaN(nanInfinityEvent.getReconstructionError()), "NaN should be accepted for reconstruction error");
        assertTrue(Float.isInfinite(nanInfinityEvent.getReconstructionThreshold()), "Infinity should be accepted for reconstruction threshold");
    }

    /**
     * NEGATIVE TEST CASE 7: Test comparing different instances with same values
     */
    @Test
    public void testCompareEqualInstances() {
        // Create two instances with the same values
        Event event1 = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            VALID_NUMBER,
            VALID_TIMESTAMP,
            VALID_SCORE,
            VALID_RECONSTRUCTION_ERROR,
            VALID_RECONSTRUCTION_THRESHOLD
        );
        
        Event event2 = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            VALID_NUMBER,
            VALID_TIMESTAMP,
            VALID_SCORE,
            VALID_RECONSTRUCTION_ERROR,
            VALID_RECONSTRUCTION_THRESHOLD
        );
        
        // Verify objects are different even with the same values (equals() not overridden)
        assertNotEquals(event1, event2, "Different instances should not be equal");
    }

    /**
     * NEGATIVE TEST CASE 8: Test field access with reflection
     */
    @Test
    public void testFieldAccessWithReflection() throws Exception {
        // Access private fields with reflection
        Field permissionField = Event.class.getDeclaredField("permission");
        Field scoreField = Event.class.getDeclaredField("score");
        
        // Make fields accessible
        permissionField.setAccessible(true);
        scoreField.setAccessible(true);
        
        // Verify field values match getter values
        assertEquals(event.getPermission(), permissionField.get(event), "Permission field value should match getter");
        assertEquals(event.getScore(), scoreField.getDouble(event), "Score field value should match getter");
    }

    /**
     * NEGATIVE TEST CASE 9: Test field modification with reflection
     */
    @Test
    public void testFieldModificationWithReflection() throws Exception {
        // Access private fields with reflection
        Field permissionField = Event.class.getDeclaredField("permission");
        permissionField.setAccessible(true);
        
        // Modify field directly
        String newPermission = "android.permission.LOCATION";
        permissionField.set(event, newPermission);
        
        // Verify getter reflects the change
        assertEquals(newPermission, event.getPermission(), "Permission getter should reflect direct field modification");
    }

    /**
     * NEGATIVE TEST CASE 10: Test string representation
     */
    @Test
    public void testStringRepresentation() {
        // Get string representation
        String stringRepresentation = event.toString();
        
        // Verify it contains class name (default toString behavior)
        assertTrue(stringRepresentation.contains("Event@"), "String representation should contain class name");
    }

    /**
     * NEGATIVE TEST CASE 11: Test using with numeric type conversion/casting
     */
    @Test
    public void testNumericTypeConversion() {
        // Set specific value
        event.setScore(123.456);
        
        // Convert to different types
        int scoreAsInt = (int) event.getScore(); // Cast to int
        float scoreAsFloat = (float) event.getScore(); // Cast to float
        long scoreAsLong = (long) event.getScore(); // Cast to long
        
        // Verify conversions
        assertEquals(123, scoreAsInt, "Score as int should be truncated");
        assertEquals(123.456f, scoreAsFloat, 0.0001f, "Score as float should be approximately equal");
        assertEquals(123L, scoreAsLong, "Score as long should be truncated");
    }

    /**
     * NEGATIVE TEST CASE 12: Test field initialization without setters
     */
    @Test
    public void testFieldInitialization() throws Exception {
        // Create a new event
        Event newEvent = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            VALID_NUMBER,
            VALID_TIMESTAMP,
            VALID_SCORE,
            VALID_RECONSTRUCTION_ERROR,
            VALID_RECONSTRUCTION_THRESHOLD
        );
        
        // Access private fields with reflection
        Field backgroundUsageField = Event.class.getDeclaredField("totalBackgroundUsage");
        Field foregroundUsageField = Event.class.getDeclaredField("totalForegroundUsage");
        
        // Make fields accessible
        backgroundUsageField.setAccessible(true);
        foregroundUsageField.setAccessible(true);
        
        // Verify fields were initialized to 0 by default
        assertEquals(0L, backgroundUsageField.getLong(newEvent), "Background usage should be initialized to 0");
        assertEquals(0L, foregroundUsageField.getLong(newEvent), "Foreground usage should be initialized to 0");
    }

    /**
     * NEGATIVE TEST CASE 13: Test updating fields that don't have setters
     */
    @Test
    public void testUpdatingFieldsWithoutSetters() throws Exception {
        // Access private fields with reflection
        Field permissionField = Event.class.getDeclaredField("permission");
        Field typeField = Event.class.getDeclaredField("type");
        Field stateField = Event.class.getDeclaredField("state");
        Field numberField = Event.class.getDeclaredField("number");
        Field timestampField = Event.class.getDeclaredField("timestamp");
        
        // Make fields accessible
        permissionField.setAccessible(true);
        typeField.setAccessible(true);
        st
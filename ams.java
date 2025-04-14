package com.example.app; // Replace with your actual package name

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for Event model
 * Contains 7 positive test cases and 3 negative test cases
 */
public class EventTest {

    // Constants for test data
    private static final String VALID_PERMISSION = "android.permission.CAMERA";
    private static final String VALID_TYPE = "PERMISSION_USED";
    private static final String VALID_STATE = "ACTIVE";
    private static final double VALID_NUMBER = 1.0;
    private static final long VALID_TIMESTAMP = System.currentTimeMillis();
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

    /**
     * POSITIVE TEST CASE 1: Create Event with valid values and verify getters return correct values
     */
    @Test
    public void testCreateWithValidValues() {
        // Verify all getters return expected values
        assertEquals(VALID_PERMISSION, event.getPermission(), "Permission should match constructor value");
        assertEquals(VALID_TYPE, event.getType(), "Type should match constructor value");
        assertEquals(VALID_STATE, event.getState(), "State should match constructor value");
        assertEquals(VALID_NUMBER, event.getNumber(), "Number should match constructor value");
        assertEquals(VALID_TIMESTAMP, event.getTimestamp(), "Timestamp should match constructor value");
        assertEquals(VALID_SCORE, event.getScore(), "Score should match constructor value");
        assertEquals(VALID_RECONSTRUCTION_ERROR, event.getReconstructionError(), "Reconstruction error should match constructor value");
        assertEquals(VALID_RECONSTRUCTION_THRESHOLD, event.getReconstructionThreshold(), "Reconstruction threshold should match constructor value");
    }

    /**
     * POSITIVE TEST CASE 2: Test setScore method
     */
    @Test
    public void testSetScore() {
        // Set a new score
        double newScore = 0.95;
        event.setScore(newScore);
        
        // Verify the score was updated
        assertEquals(newScore, event.getScore(), "Score should be updated");
    }

    /**
     * POSITIVE TEST CASE 3: Test setReconstructionError method
     */
    @Test
    public void testSetReconstructionError() {
        // Set a new reconstruction error
        Float newReconstructionError = 0.25f;
        event.setReconstructionError(newReconstructionError);
        
        // Verify the reconstruction error was updated
        assertEquals(newReconstructionError, event.getReconstructionError(), "Reconstruction error should be updated");
    }

    /**
     * POSITIVE TEST CASE 4: Test setReconstructionThreshold method
     */
    @Test
    public void testSetReconstructionThreshold() {
        // Set a new reconstruction threshold
        Float newReconstructionThreshold = 0.75f;
        event.setReconstructionThreshold(newReconstructionThreshold);
        
        // Verify the reconstruction threshold was updated
        assertEquals(newReconstructionThreshold, event.getReconstructionThreshold(), "Reconstruction threshold should be updated");
    }

    /**
     * POSITIVE TEST CASE 5: Test with nullable Float values set to null
     */
    @Test
    public void testWithNullFloatValues() {
        // Create an Event with null Float values
        Event nullFloatEvent = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            VALID_NUMBER,
            VALID_TIMESTAMP,
            VALID_SCORE,
            null,
            null
        );
        
        // Verify null values were set correctly
        assertNull(nullFloatEvent.getReconstructionError(), "Reconstruction error should be null");
        assertNull(nullFloatEvent.getReconstructionThreshold(), "Reconstruction threshold should be null");
        
        // Test setting null values through setters
        event.setReconstructionError(null);
        event.setReconstructionThreshold(null);
        
        // Verify null values were set through setters
        assertNull(event.getReconstructionError(), "Reconstruction error should be updated to null");
        assertNull(event.getReconstructionThreshold(), "Reconstruction threshold should be updated to null");
    }

    /**
     * POSITIVE TEST CASE 6: Test with extreme numeric values
     */
    @Test
    public void testWithExtremeValues() {
        // Create an Event with extreme numeric values
        Event extremeEvent = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            Double.MAX_VALUE,
            Long.MAX_VALUE,
            Double.MAX_VALUE,
            Float.MAX_VALUE,
            Float.MAX_VALUE
        );
        
        // Verify extreme values were set correctly
        assertEquals(Double.MAX_VALUE, extremeEvent.getNumber(), "Maximum double number should be set correctly");
        assertEquals(Long.MAX_VALUE, extremeEvent.getTimestamp(), "Maximum long timestamp should be set correctly");
        assertEquals(Double.MAX_VALUE, extremeEvent.getScore(), "Maximum double score should be set correctly");
        assertEquals(Float.MAX_VALUE, extremeEvent.getReconstructionError(), "Maximum float reconstruction error should be set correctly");
        assertEquals(Float.MAX_VALUE, extremeEvent.getReconstructionThreshold(), "Maximum float reconstruction threshold should be set correctly");
    }

    /**
     * POSITIVE TEST CASE 7: Test totalBackground and totalForeground usage
     */
    @Test
    public void testUsageTracking() {
        // Initial values should be 0
        assertEquals(0, event.getTotalBackgroundUsage(), "Initial background usage should be 0");
        assertEquals(0, event.getTotalForegroundUsage(), "Initial foreground usage should be 0");
        
        // Note: The Event class doesn't have setters for these fields.
        // If these values are meant to be set by other means, those methods would need to be tested separately.
    }

    /**
     * NEGATIVE TEST CASE 1: Test with special characters in string fields
     */
    @Test
    public void testWithSpecialCharactersInStrings() {
        // Create an Event with special characters in string fields
        String specialPermission = "android.permission.CAMERA!@#$%^&*()";
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
        
        // Verify special characters were handled correctly
        assertEquals(specialPermission, specialCharsEvent.getPermission(), "Special characters in permission should be preserved");
        assertEquals(specialType, specialCharsEvent.getType(), "Special characters in type should be preserved");
        assertEquals(specialState, specialCharsEvent.getState(), "Special characters in state should be preserved");
    }

    /**
     * NEGATIVE TEST CASE 2: Test with negative numeric values
     */
    @Test
    public void testWithNegativeValues() {
        // Create an Event with negative numeric values
        double negativeNumber = -10.5;
        long negativeTimestamp = -1000L;
        double negativeScore = -0.75;
        Float negativeError = -0.3f;
        Float negativeThreshold = -0.8f;
        
        Event negativeValuesEvent = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            negativeNumber,
            negativeTimestamp,
            negativeScore,
            negativeError,
            negativeThreshold
        );
        
        // Verify negative values were set correctly
        assertEquals(negativeNumber, negativeValuesEvent.getNumber(), "Negative number should be set correctly");
        assertEquals(negativeTimestamp, negativeValuesEvent.getTimestamp(), "Negative timestamp should be set correctly");
        assertEquals(negativeScore, negativeValuesEvent.getScore(), "Negative score should be set correctly");
        assertEquals(negativeError, negativeValuesEvent.getReconstructionError(), "Negative reconstruction error should be set correctly");
        assertEquals(negativeThreshold, negativeValuesEvent.getReconstructionThreshold(), "Negative reconstruction threshold should be set correctly");
    }

    /**
     * NEGATIVE TEST CASE 3: Test that different objects with same values are not equal
     * Note: This assumes Event doesn't override equals() method
     */
    @Test
    public void testObjectEquality() {
        // Create a new Event with the same values
        Event sameValuesEvent = new Event(
            VALID_PERMISSION,
            VALID_TYPE,
            VALID_STATE,
            VALID_NUMBER,
            VALID_TIMESTAMP,
            VALID_SCORE,
            VALID_RECONSTRUCTION_ERROR,
            VALID_RECONSTRUCTION_THRESHOLD
        );
        
        // Verify they are different objects even with same values
        // This test would need to be updated if equals() is implemented
        assertNotEquals(event, sameValuesEvent, "Different objects with same values should not be equal");
    }
    
    /**
     * NEGATIVE TEST CASE 4: Test with empty string values
     */
    @Test
    public void testWithEmptyStrings() {
        // Create an Event with empty string values
        Event emptyStringsEvent = new Event(
            "",
            "",
            "",
            VALID_NUMBER,
            VALID_TIMESTAMP,
            VALID_SCORE,
            VALID_RECONSTRUCTION_ERROR,
            VALID_RECONSTRUCTION_THRESHOLD
        );
        
        // Verify empty strings were set correctly
        assertEquals("", emptyStringsEvent.getPermission(), "Empty permission should be set correctly");
        assertEquals("", emptyStringsEvent.getType(), "Empty type should be set correctly");
        assertEquals("", emptyStringsEvent.getState(), "Empty state should be set correctly");
    }
}
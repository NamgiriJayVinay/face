package com.example.app; // Replace with your actual package name

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

/**
 * Test class for MinuteWiseInferenceDataPoint
 * Contains 3 positive test cases and 15 negative test cases (1:5 ratio)
 */
public class MinuteWiseInferenceDataPointTest {

    // Mock dependencies
    @Mock
    private AiMinuteDataEntity mockMinuteData;
    
    @Mock
    private PeriodicUsageDataPoint mockPeriodicData;
    
    // Test subject
    private MinuteWiseInferenceDataPoint dataPoint;
    
    // Sample enum for testing
    private enum AIModelAttributeNames {
        PERMISSION_USED_CAMERA,
        PERMISSION_USED_MICROPHONE,
        PERMISSION_USED_GET_LOCATION,
        PERMISSION_USED_COARSE_LOCATION,
        PERMISSION_USED_SEND_SMS,
        PERMISSION_USED_CAMERA_HOURLY,
        PERMISSION_USED_CAMERA_DAILY,
        PERMISSION_USED_MICROPHONE_HOURLY,
        PERMISSION_USED_MICROPHONE_DAILY,
        PERMISSION_USED_GET_LOCATION_HOURLY,
        PERMISSION_USED_GET_LOCATION_DAILY,
        PERMISSION_USED_COARSE_LOCATION_HOURLY,
        PERMISSION_USED_COARSE_LOCATION_DAILY,
        CAMERA_DURATION,
        LOCATION_DURATION,
        MICROPHONE_DURATION,
        PING_CAMERA_HOURLY,
        PING_CAMERA_DAILY,
        PING_MICROPHONE_HOURLY,
        PING_MICROPHONE_DAILY,
        PING_GET_LOCATION_HOURLY,
        PING_GET_LOCATION_DAILY,
        PING_COARSE_LOCATION_HOURLY,
        PING_COARSE_LOCATION_DAILY,
        PING_READ_SMS_HOURLY,
        PING_READ_SMS_DAILY,
        PING_READ_CONTACTS_HOURLY,
        PING_READ_CONTACTS_DAILY,
        PING_READ_CALL_LOGS_HOURLY,
        PING_READ_CALL_LOGS_DAILY,
        PING_READ_PHOTOS_HOURLY,
        PING_READ_PHOTOS_DAILY,
        PING_READ_VIDEOS_HOURLY,
        PING_READ_VIDEOS_DAILY,
        PING_PHONE_HOURLY,
        PING_PHONE_DAILY,
        PING_NEAR_BY_DEVICES_HOURLY,
        PING_NEAR_BY_DEVICES_DAILY,
        PING_ACTIVITY_RECOGNITION_HOURLY,
        PING_ACTIVITY_RECOGNITION_DAILY,
        PING_READ_MEDIA_AURAL_HOURLY,
        PING_READ_MEDIA_AURAL_DAILY,
        PING_STORAGE_HOURLY,
        PING_STORAGE_DAILY,
        PING_CALENDAR_HOURLY,
        PING_CALENDAR_DAILY,
        BACKGROUND_FEATURE,
        APP_CATEGORY,
        APP_TRUST_LEVEL,
        ACCESS_HOURS,
        NON_EXISTENT_FEATURE // Added for negative testing
    }
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mockMinuteData
        mockMinuteData.permissionUsedCamera = 1;
        mockMinuteData.permissionUsedMicrophone = 2;
        mockMinuteData.permissionUsedGetLocation = 3;
        mockMinuteData.permissionUsedCoarseLocation = 4;
        mockMinuteData.permissionUsedSendSMS = 5;
        mockMinuteData.cameraDuration = 100L;
        mockMinuteData.locationDuration = 200L;
        mockMinuteData.microphoneDuration = 300L;
        mockMinuteData.isForeground = 0;
        mockMinuteData.appCategory = 10;
        mockMinuteData.appTrustLevel = 3;
        mockMinuteData.accessHour = 14;
        
        // Setup mockPeriodicData
        mockPeriodicData.permissionUsedCameraHourly = 11;
        mockPeriodicData.permissionUsedCameraDaily = 110;
        mockPeriodicData.permissionUsedMicrophoneHourly = 12;
        mockPeriodicData.permissionUsedMicrophoneDaily = 120;
        mockPeriodicData.permissionUsedGetLocationHourly = 13;
        mockPeriodicData.permissionUsedGetLocationDaily = 130;
        mockPeriodicData.permissionUsedCoarseLocationHourly = 14;
        mockPeriodicData.permissionUsedCoarseLocationDaily = 140;
        mockPeriodicData.pingCameraHourly = 21;
        mockPeriodicData.pingCameraDaily = 210;
        mockPeriodicData.pingMicrophoneHourly = 22;
        mockPeriodicData.pingMicrophoneDaily = 220;
        mockPeriodicData.pingGetLocationHourly = 23;
        mockPeriodicData.pingGetLocationDaily = 230;
        mockPeriodicData.pingCoarseLocationHourly = 24;
        mockPeriodicData.pingCoarseLocationDaily = 240;
        mockPeriodicData.pingReadSMSHourly = 25;
        mockPeriodicData.pingReadSMSDaily = 250;
        mockPeriodicData.pingReadContactsHourly = 26;
        mockPeriodicData.pingReadContactsDaily = 260;
        mockPeriodicData.pingReadCallLogsHourly = 27;
        mockPeriodicData.pingReadCallLogsDaily = 270;
        mockPeriodicData.pingReadPhotosHourly = 28;
        mockPeriodicData.pingReadPhotosDaily = 280;
        mockPeriodicData.pingReadVideosHourly = 29;
        mockPeriodicData.pingReadVideosDaily = 290;
        mockPeriodicData.pingPhoneHourly = 30;
        mockPeriodicData.pingPhoneDaily = 300;
        mockPeriodicData.pingNearByDevicesHourly = 31;
        mockPeriodicData.pingNearByDevicesDaily = 310;
        mockPeriodicData.pingActivityRecognitionHourly = 32;
        mockPeriodicData.pingActivityRecognitionDaily = 320;
        mockPeriodicData.pingReadMediaAuralHourly = 33;
        mockPeriodicData.pingReadMediaAuralDaily = 330;
        mockPeriodicData.pingStorageHourly = 34;
        mockPeriodicData.pingStorageDaily = 340;
        mockPeriodicData.pingCalendarHourly = 35;
        mockPeriodicData.pingCalendarDaily = 350;
        
        // Create instance of test subject with mocks
        dataPoint = new MinuteWiseInferenceDataPoint(mockMinuteData, mockPeriodicData);
    }

    // =============== POSITIVE TEST CASES ===============

    /**
     * POSITIVE TEST CASE 1: Test constructor properly initializes object with valid inputs
     */
    @Test
    public void testConstructorInitializesObjectWithValidInputs() {
        // Verify object was properly constructed
        assertNotNull(dataPoint.minuteWisePreProcessEntityForVAE);
        assertNotNull(dataPoint.periodicUsageDataPointForVAE);
        assertNotNull(dataPoint.featureValuesMap);
        
        // Check that the references are correct
        assertEquals(mockMinuteData, dataPoint.minuteWisePreProcessEntityForVAE);
        assertEquals(mockPeriodicData, dataPoint.periodicUsageDataPointForVAE);
    }

    /**
     * POSITIVE TEST CASE 2: Test getFeatureValue returns correct values for various features
     */
    @Test
    public void testGetFeatureValueReturnsCorrectValues() {
        // Test a sample of features (minute data)
        assertEquals(1, dataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA));
        assertEquals(2, dataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_MICROPHONE));
        assertEquals(100, dataPoint.getFeatureValue(AIModelAttributeNames.CAMERA_DURATION));
        
        // Test a sample of features (periodic data)
        assertEquals(11, dataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA_HOURLY));
        assertEquals(110, dataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA_DAILY));
        assertEquals(21, dataPoint.getFeatureValue(AIModelAttributeNames.PING_CAMERA_HOURLY));
        
        // Test app metadata features
        assertEquals(0, dataPoint.getFeatureValue(AIModelAttributeNames.BACKGROUND_FEATURE));
        assertEquals(10, dataPoint.getFeatureValue(AIModelAttributeNames.APP_CATEGORY));
        assertEquals(3, dataPoint.getFeatureValue(AIModelAttributeNames.APP_TRUST_LEVEL));
    }

    /**
     * POSITIVE TEST CASE 3: Test getFeatureValue returns 0 for non-existent features
     */
    @Test
    public void testGetFeatureValueReturnsZeroForNonExistentFeatures() {
        // Test with a feature that doesn't exist in the map
        assertEquals(0, dataPoint.getFeatureValue(AIModelAttributeNames.NON_EXISTENT_FEATURE));
    }

    // =============== NEGATIVE TEST CASES ===============

    /**
     * NEGATIVE TEST CASE 1: Test constructor with null AiMinuteDataEntity
     */
    @Test
    public void testConstructorWithNullMinuteData() {
        assertThrows(NullPointerException.class, () -> {
            new MinuteWiseInferenceDataPoint(null, mockPeriodicData);
        });
    }

    /**
     * NEGATIVE TEST CASE 2: Test constructor with null PeriodicUsageDataPoint
     */
    @Test
    public void testConstructorWithNullPeriodicData() {
        assertThrows(NullPointerException.class, () -> {
            new MinuteWiseInferenceDataPoint(mockMinuteData, null);
        });
    }

    /**
     * NEGATIVE TEST CASE 3: Test handling of negative permission values
     */
    @Test
    public void testHandlingOfNegativePermissionValues() {
        // Create mocks with negative values
        AiMinuteDataEntity negativeMinuteData = mock(AiMinuteDataEntity.class);
        negativeMinuteData.permissionUsedCamera = -1;
        negativeMinuteData.permissionUsedMicrophone = -2;
        negativeMinuteData.cameraDuration = -100L;
        
        // Create data point with negative values
        MinuteWiseInferenceDataPoint negativeDataPoint = new MinuteWiseInferenceDataPoint(negativeMinuteData, mockPeriodicData);
        
        // Verify negative values are preserved (not adjusted)
        assertEquals(-1, negativeDataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA));
        assertEquals(-2, negativeDataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_MICROPHONE));
        assertEquals(-100, negativeDataPoint.getFeatureValue(AIModelAttributeNames.CAMERA_DURATION));
    }

    /**
     * NEGATIVE TEST CASE 4: Test handling of extreme integer values
     */
    @Test
    public void testHandlingOfExtremeValues() {
        // Create mocks with extreme values
        AiMinuteDataEntity extremeMinuteData = mock(AiMinuteDataEntity.class);
        extremeMinuteData.permissionUsedCamera = Integer.MAX_VALUE;
        extremeMinuteData.permissionUsedMicrophone = Integer.MIN_VALUE;
        
        PeriodicUsageDataPoint extremePeriodicData = mock(PeriodicUsageDataPoint.class);
        extremePeriodicData.permissionUsedCameraHourly = Integer.MAX_VALUE;
        extremePeriodicData.permissionUsedMicrophoneHourly = Integer.MIN_VALUE;
        
        // Create data point with extreme values
        MinuteWiseInferenceDataPoint extremeDataPoint = new MinuteWiseInferenceDataPoint(extremeMinuteData, extremePeriodicData);
        
        // Verify extreme values are preserved
        assertEquals(Integer.MAX_VALUE, extremeDataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA));
        assertEquals(Integer.MIN_VALUE, extremeDataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_MICROPHONE));
        assertEquals(Integer.MAX_VALUE, extremeDataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA_HOURLY));
        assertEquals(Integer.MIN_VALUE, extremeDataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_MICROPHONE_HOURLY));
    }

    /**
     * NEGATIVE TEST CASE 5: Test handling of integer overflow in duration casting
     */
    @Test
    public void testHandlingOfIntegerOverflowInDurationCasting() {
        // Create a mock with duration value larger than Integer.MAX_VALUE
        AiMinuteDataEntity overflowMinuteData = mock(AiMinuteDataEntity.class);
        overflowMinuteData.cameraDuration = Integer.MAX_VALUE + 1L;
        
        // Create data point with overflow duration
        MinuteWiseInferenceDataPoint overflowDataPoint = new MinuteWiseInferenceDataPoint(overflowMinuteData, mockPeriodicData);
        
        // Verify overflow happened due to casting to int (should wrap to Integer.MIN_VALUE)
        assertEquals(Integer.MIN_VALUE, overflowDataPoint.getFeatureValue(AIModelAttributeNames.CAMERA_DURATION));
    }

    /**
     * NEGATIVE TEST CASE 6: Test with inconsistent data between entities
     */
    @Test
    public void testWithInconsistentData() {
        // Create minute data with foreground = 1
        AiMinuteDataEntity foregroundMinuteData = mock(AiMinuteDataEntity.class);
        foregroundMinuteData.isForeground = 1;
        foregroundMinuteData.permissionUsedCamera = 0;  // No camera permission used
        
        // But periodic data showing high camera usage
        PeriodicUsageDataPoint highUsagePeriodicData = mock(PeriodicUsageDataPoint.class);
        highUsagePeriodicData.permissionUsedCameraHourly = 100;
        highUsagePeriodicData.permissionUsedCameraDaily = 1000;
        
        // Create data point with inconsistent data
        MinuteWiseInferenceDataPoint inconsistentDataPoint = new MinuteWiseInferenceDataPoint(foregroundMinuteData, highUsagePeriodicData);
        
        // Verify inconsistency is preserved
        assertEquals(0, inconsistentDataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA));
        assertEquals(100, inconsistentDataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA_HOURLY));
        assertEquals(1000, inconsistentDataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA_DAILY));
    }

    /**
     * NEGATIVE TEST CASE 7: Test for feature key collisions in HashMap
     */
    @Test
    public void testForFeatureKeyCollisions() {
        // Check that all expected features are in the map
        for (AIModelAttributeNames feature : AIModelAttributeNames.values()) {
            if (feature != AIModelAttributeNames.NON_EXISTENT_FEATURE) {
                assertTrue(dataPoint.featureValuesMap.containsKey(feature) || 
                           dataPoint.getFeatureValue(feature) == 0,
                           "Feature " + feature + " should be in the map or return 0");
            }
        }
        
        // Verify the size of the map makes sense (should be close to enum size - 1)
        int expectedSize = AIModelAttributeNames.values().length - 1; // -1 for NON_EXISTENT_FEATURE
        assertTrue(Math.abs(dataPoint.featureValuesMap.size() - expectedSize) <= 1, 
                   "Map size should be approximately equal to the number of actual features");
    }

    /**
     * NEGATIVE TEST CASE 8: Test with null feature parameter to getFeatureValue
     */
    @Test
    public void testWithNullFeatureParameter() {
        assertThrows(NullPointerException.class, () -> {
            dataPoint.getFeatureValue(null);
        });
    }

    /**
     * NEGATIVE TEST CASE 9: Test with corrupted feature map (manually set to null)
     */
    @Test
    public void testWithCorruptedFeatureMap() {
        // Create a valid data point
        MinuteWiseInferenceDataPoint corruptedDataPoint = new MinuteWiseInferenceDataPoint(mockMinuteData, mockPeriodicData);
        
        // Corrupt the feature map by setting it to null
        corruptedDataPoint.featureValuesMap = null;
        
        // Verify that accessing features now throws NullPointerException
        assertThrows(NullPointerException.class, () -> {
            corruptedDataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA);
        });
    }

    /**
     * NEGATIVE TEST CASE 10: Test with clear() called on feature map
     */
    @Test
    public void testWithClearedFeatureMap() {
        // Clear the feature map
        dataPoint.featureValuesMap.clear();
        
        // Verify that all features now return 0
        assertEquals(0, dataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA));
        assertEquals(0, dataPoint.getFeatureValue(AIModelAttributeNames.CAMERA_DURATION));
        assertEquals(0, dataPoint.getFeatureValue(AIModelAttributeNames.PING_CAMERA_HOURLY));
    }

    /**
     * NEGATIVE TEST CASE 11: Test adding custom feature after construction
     */
    @Test
    public void testAddingCustomFeatureAfterConstruction() {
        // Define a custom feature value
        final int CUSTOM_VALUE = 999;
        
        // Add a custom feature to the map
        dataPoint.featureValuesMap.put(AIModelAttributeNames.NON_EXISTENT_FEATURE, CUSTOM_VALUE);
        
        // Verify the custom feature can be retrieved
        assertEquals(CUSTOM_VALUE, dataPoint.getFeatureValue(AIModelAttributeNames.NON_EXISTENT_FEATURE));
    }

    /**
     * NEGATIVE TEST CASE 12: Test modifying feature values after construction
     */
    @Test
    public void testModifyingFeatureValuesAfterConstruction() {
        // Original value for camera permission
        int originalValue = dataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA);
        
        // Modified value
        int modifiedValue = originalValue + 100;
        
        // Modify the value in the map
        dataPoint.featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_CAMERA, modifiedValue);
        
        // Verify the modified value is retrieved
        assertEquals(modifiedValue, dataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA));
    }

    /**
     * NEGATIVE TEST CASE 13: Test with changing source entity values after construction
     */
    @Test
    public void testChangingSourceEntityValuesAfterConstruction() {
        // Get original value
        int originalValue = dataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA);
        
        // Change value in source entity (shouldn't affect already constructed data point)
        mockMinuteData.permissionUsedCamera = originalValue + 100;
        
        // Verify data point still has original value (not the updated entity value)
        assertEquals(originalValue, dataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA));
    }

    /**
     * NEGATIVE TEST CASE 14: Test with extremely large feature map (stress test)
     */
    @Test
    public void testWithExtremelyLargeFeatureMap() {
        // Create a new map and fill it with many entries (stress test)
        HashMap<AIModelAttributeNames, Integer> largeMap = new HashMap<>();
        for (AIModelAttributeNames feature : AIModelAttributeNames.values()) {
            largeMap.put(feature, 1);
        }
        
        // Replace the regular map with the large map
        dataPoint.featureValuesMap = largeMap;
        
        // Verify we can still get values from the map
        assertEquals(1, dataPoint.getFeatureValue(AIModelAttributeNames.PERMISSION_USED_CAMERA));
        assertEquals(1, dataPoint.getFeatureValue(AIModelAttributeNames.NON_EXISTENT_FEATURE));
    }

    /**
     * NEGATIVE TEST CASE 15: Test with concurrent modification of feature map
     */
    @Test
    public void testWithConcurrentModificationOfFeatureMap() {
        // This test would ideally use multiple threads to test concurrent access
        // But we can simulate it by modifying the map while iterating
        assertThrows(Exception.class, () -> {
            for (AIModelAttributeNames feature : dataPoint.featureValuesMap.keySet()) {
                // Concurrent modification during iteration
                dataPoint.featureValuesMap.put(AIModelAttributeNames.NON_EXISTENT_FEATURE, 999);
                // Do something with the feature to prevent optimization
                System.out.println(feature.name());
            }
        });
    }
}
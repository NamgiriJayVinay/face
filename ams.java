package com.example.app; // Replace with your actual package name

// Use only one assertion library to avoid ambiguity
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AiMinuteDataEntity
 * Contains 7 positive test cases and 3 negative test cases
 */
public class AiMinuteDataEntityTest {

    // Constants for test data
    private static final long VALID_TIMESTAMP = System.currentTimeMillis();
    private static final int VALID_UID = 10001;
    private static final String VALID_PACKAGE_NAME = "com.example.testapp";
    private static final int VALID_APP_CATEGORY = 5;
    private static final int VALID_APP_TRUST_LEVEL = 2;
    private static final long VALID_ACCESS_MINUTE = 30L;
    private static final int VALID_ACCESS_HOUR = 14;
    private static final int VALID_ACCESS_DATE = 20230410;
    private static final String VALID_GRANTED_PERMISSIONS = "android.permission.CAMERA,android.permission.MICROPHONE";
    private static final String VALID_REQUESTED_PERMISSIONS = "android.permission.CAMERA,android.permission.MICROPHONE,android.permission.LOCATION";
    private static final int VALID_IS_FOREGROUND = 1;

    // Helper objects
    private PermissionUsedCount permissionUsedCount;
    private SensorDuration sensorDuration;

    @Before
    public void setUp() {
        // Initialize PermissionUsedCount
        permissionUsedCount = new PermissionUsedCount();
        permissionUsedCount.setPermissionUsedCamera(1);
        permissionUsedCount.setPermissionUsedMicrophone(1);
        permissionUsedCount.setPermissionUsedGetLocation(0);
        permissionUsedCount.setPermissionUsedCoarseLocation(0);
        permissionUsedCount.setPermissionUsedStorage(1);
        permissionUsedCount.setPermissionUsedReadMediaAural(1);
        permissionUsedCount.setPermissionUsedReadMediaVisual(1);
        permissionUsedCount.setPermissionUsedAccessVideos(0);
        permissionUsedCount.setPermissionUsedAccessPhotos(1);
        permissionUsedCount.setPermissionUsedReadContacts(0);
        permissionUsedCount.setPermissionUsedEditAndDeleteContacts(0);
        permissionUsedCount.setPermissionUsedSendSMS(0);
        permissionUsedCount.setPermissionUsedReceiveSMS(0);
        permissionUsedCount.setPermissionUsedReadSMS(0);
        permissionUsedCount.setPermissionUsedNearByDevices(0);
        permissionUsedCount.setPermissionUsedActivityRecognition(0);
        permissionUsedCount.setPermissionUsedCalendar(0);
        permissionUsedCount.setPermissionUsedCallLogs(0);
        permissionUsedCount.setPermissionUsedPhone(0);

        // Initialize SensorDuration
        sensorDuration = new SensorDuration(60000L, 45000L, 30000L);
    }

    /**
     * POSITIVE TEST CASE 1: Create valid AiMinuteDataEntity with all required fields
     */
    @Test
    public void testCreateValidEntity() {
        // Create entity with all fields
        AiMinuteDataEntity entity = new AiMinuteDataEntity(
                VALID_TIMESTAMP,
                VALID_UID,
                VALID_PACKAGE_NAME,
                VALID_APP_CATEGORY,
                VALID_APP_TRUST_LEVEL,
                VALID_ACCESS_MINUTE,
                VALID_ACCESS_HOUR,
                VALID_ACCESS_DATE,
                VALID_GRANTED_PERMISSIONS,
                VALID_REQUESTED_PERMISSIONS,
                VALID_IS_FOREGROUND,
                permissionUsedCount,
                sensorDuration
        );

        // Verify all fields have expected values
        assertEquals(VALID_TIMESTAMP, entity.timestamp);
        assertEquals(VALID_UID, entity.uid);
        assertEquals(VALID_PACKAGE_NAME, entity.packageName);
        assertEquals(VALID_APP_CATEGORY, entity.appCategory);
        assertEquals(VALID_APP_TRUST_LEVEL, entity.appTrustLevel);
        assertEquals(VALID_ACCESS_MINUTE, entity.accessMinute);
        assertEquals(VALID_ACCESS_HOUR, entity.accessHour);
        assertEquals(VALID_ACCESS_DATE, entity.accessDate);
        assertEquals(VALID_GRANTED_PERMISSIONS, entity.grantedPermissions);
        assertEquals(VALID_REQUESTED_PERMISSIONS, entity.requestedPermissions);
        assertEquals(VALID_IS_FOREGROUND, entity.isForeground);

        // Verify permission counts 
        assertEquals(1, entity.permissionUsedCamera);
        assertEquals(1, entity.permissionUsedMicrophone);
        assertEquals(0, entity.permissionUsedGetLocation);
        assertEquals(1, entity.permissionUsedStorage);
        
        // Verify sensor durations
        assertEquals(60000L, entity.cameraDuration);
        assertEquals(45000L, entity.microphoneDuration);
        assertEquals(30000L, entity.locationDuration);
    }

    /**
     * POSITIVE TEST CASE 2: Create entity with empty constructor and set fields
     */
    @Test
    public void testCreateWithEmptyConstructor() {
        // Create entity with empty constructor
        AiMinuteDataEntity entity = new AiMinuteDataEntity();
        
        // Set fields manually
        entity.timestamp = VALID_TIMESTAMP;
        entity.uid = VALID_UID;
        entity.packageName = VALID_PACKAGE_NAME;
        entity.appCategory = VALID_APP_CATEGORY;
        entity.appTrustLevel = VALID_APP_TRUST_LEVEL;
        entity.accessMinute = VALID_ACCESS_MINUTE;
        entity.accessHour = VALID_ACCESS_HOUR;
        entity.accessDate = VALID_ACCESS_DATE;
        entity.grantedPermissions = VALID_GRANTED_PERMISSIONS;
        entity.requestedPermissions = VALID_REQUESTED_PERMISSIONS;
        entity.isForeground = VALID_IS_FOREGROUND;
        
        // Set permission counts and sensor durations
        entity.permissionUsedCamera = 1;
        entity.permissionUsedMicrophone = 1;
        entity.cameraDuration = 60000L;
        entity.microphoneDuration = 45000L;
        entity.locationDuration = 30000L;

        // Verify fields were set correctly
        assertEquals(VALID_TIMESTAMP, entity.timestamp);
        assertEquals(VALID_UID, entity.uid);
        assertEquals(VALID_PACKAGE_NAME, entity.packageName);
        assertEquals(1, entity.permissionUsedCamera);
        assertEquals(60000L, entity.cameraDuration);
    }

    /**
     * POSITIVE TEST CASE 3: Test with extreme values for numeric fields
     */
    @Test
    public void testCreateWithExtremeValues() {
        // Create PermissionUsedCount with extreme values
        PermissionUsedCount extremePermissionCount = new PermissionUsedCount();
        extremePermissionCount.setPermissionUsedCamera(Integer.MAX_VALUE);
        extremePermissionCount.setPermissionUsedMicrophone(Integer.MIN_VALUE);
        
        // Create SensorDuration with extreme values
        SensorDuration extremeSensorDuration = new SensorDuration(Long.MAX_VALUE, Long.MIN_VALUE, 0L);
        
        // Create entity with extreme values
        AiMinuteDataEntity entity = new AiMinuteDataEntity(
                Long.MAX_VALUE,
                Integer.MAX_VALUE,
                VALID_PACKAGE_NAME,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Long.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                VALID_GRANTED_PERMISSIONS,
                VALID_REQUESTED_PERMISSIONS,
                1,
                extremePermissionCount,
                extremeSensorDuration
        );

        // Verify extreme values were set correctly
        assertEquals(Long.MAX_VALUE, entity.timestamp);
        assertEquals(Integer.MAX_VALUE, entity.uid);
        assertEquals(Integer.MAX_VALUE, entity.permissionUsedCamera);
        assertEquals(Integer.MIN_VALUE, entity.permissionUsedMicrophone);
        assertEquals(Long.MAX_VALUE, entity.cameraDuration);
        assertEquals(Long.MIN_VALUE, entity.microphoneDuration);
    }

    /**
     * POSITIVE TEST CASE 4: Test with complex permission strings
     */
    @Test
    public void testWithComplexPermissionStrings() {
        // Create complex permission strings
        String complexGrantedPermissions = "android.permission.CAMERA,android.permission.MICROPHONE,android.permission.FINE_LOCATION,android.permission.READ_CONTACTS,android.permission.READ_CALENDAR";
        String complexRequestedPermissions = "android.permission.CAMERA,android.permission.MICROPHONE,android.permission.FINE_LOCATION,android.permission.READ_CONTACTS,android.permission.READ_CALENDAR,android.permission.READ_SMS,android.permission.SEND_SMS";
        
        // Create entity with complex permission strings
        AiMinuteDataEntity entity = new AiMinuteDataEntity(
                VALID_TIMESTAMP,
                VALID_UID,
                VALID_PACKAGE_NAME,
                VALID_APP_CATEGORY,
                VALID_APP_TRUST_LEVEL,
                VALID_ACCESS_MINUTE,
                VALID_ACCESS_HOUR,
                VALID_ACCESS_DATE,
                complexGrantedPermissions,
                complexRequestedPermissions,
                VALID_IS_FOREGROUND,
                permissionUsedCount,
                sensorDuration
        );

        // Verify complex permission strings were set correctly
        assertEquals(complexGrantedPermissions, entity.grantedPermissions);
        assertEquals(complexRequestedPermissions, entity.requestedPermissions);
    }

    /**
     * POSITIVE TEST CASE 5: Test with updated permission counts
     */
    @Test
    public void testUpdatePermissionCounts() {
        // Create initial entity
        AiMinuteDataEntity entity = new AiMinuteDataEntity(
                VALID_TIMESTAMP,
                VALID_UID,
                VALID_PACKAGE_NAME,
                VALID_APP_CATEGORY,
                VALID_APP_TRUST_LEVEL,
                VALID_ACCESS_MINUTE,
                VALID_ACCESS_HOUR,
                VALID_ACCESS_DATE,
                VALID_GRANTED_PERMISSIONS,
                VALID_REQUESTED_PERMISSIONS,
                VALID_IS_FOREGROUND,
                permissionUsedCount,
                sensorDuration
        );
        
        // Verify initial permission counts
        assertEquals(1, entity.permissionUsedCamera);
        assertEquals(1, entity.permissionUsedMicrophone);
        assertEquals(0, entity.permissionUsedGetLocation);
        
        // Update permission counts
        entity.permissionUsedCamera = 2;
        entity.permissionUsedMicrophone = 3;
        entity.permissionUsedGetLocation = 1;
        
        // Verify updated permission counts
        assertEquals(2, entity.permissionUsedCamera);
        assertEquals(3, entity.permissionUsedMicrophone);
        assertEquals(1, entity.permissionUsedGetLocation);
    }

    /**
     * POSITIVE TEST CASE 6: Test with updated sensor durations
     */
    @Test
    public void testUpdateSensorDurations() {
        // Create initial entity
        AiMinuteDataEntity entity = new AiMinuteDataEntity(
                VALID_TIMESTAMP,
                VALID_UID,
                VALID_PACKAGE_NAME,
                VALID_APP_CATEGORY,
                VALID_APP_TRUST_LEVEL,
                VALID_ACCESS_MINUTE,
                VALID_ACCESS_HOUR,
                VALID_ACCESS_DATE,
                VALID_GRANTED_PERMISSIONS,
                VALID_REQUESTED_PERMISSIONS,
                VALID_IS_FOREGROUND,
                permissionUsedCount,
                sensorDuration
        );
        
        // Verify initial sensor durations
        assertEquals(60000L, entity.cameraDuration);
        assertEquals(45000L, entity.microphoneDuration);
        assertEquals(30000L, entity.locationDuration);
        
        // Update sensor durations
        entity.cameraDuration = 120000L;
        entity.microphoneDuration = 90000L;
        entity.locationDuration = 60000L;
        
        // Verify updated sensor durations
        assertEquals(120000L, entity.cameraDuration);
        assertEquals(90000L, entity.microphoneDuration);
        assertEquals(60000L, entity.locationDuration);
    }

    /**
     * POSITIVE TEST CASE 7: Test with empty permission strings
     */
    @Test
    public void testWithEmptyPermissionStrings() {
        // Create entity with empty permission strings
        AiMinuteDataEntity entity = new AiMinuteDataEntity(
                VALID_TIMESTAMP,
                VALID_UID,
                VALID_PACKAGE_NAME,
                VALID_APP_CATEGORY,
                VALID_APP_TRUST_LEVEL,
                VALID_ACCESS_MINUTE,
                VALID_ACCESS_HOUR,
                VALID_ACCESS_DATE,
                "",
                "",
                VALID_IS_FOREGROUND,
                permissionUsedCount,
                sensorDuration
        );

        // Verify empty permission strings were set correctly
        assertEquals("", entity.grantedPermissions);
        assertEquals("", entity.requestedPermissions);
    }

    /**
     * NEGATIVE TEST CASE 1: Create entity with null package name
     */
    @Test
    public void testCreateWithNullPackageName() {
        try {
            // Attempt to create entity with null package name
            AiMinuteDataEntity entity = new AiMinuteDataEntity(
                    VALID_TIMESTAMP,
                    VALID_UID,
                    null, // This might throw an exception if there is validation
                    VALID_APP_CATEGORY,
                    VALID_APP_TRUST_LEVEL,
                    VALID_ACCESS_MINUTE,
                    VALID_ACCESS_HOUR,
                    VALID_ACCESS_DATE,
                    VALID_GRANTED_PERMISSIONS,
                    VALID_REQUESTED_PERMISSIONS,
                    VALID_IS_FOREGROUND,
                    permissionUsedCount,
                    sensorDuration
            );
            
            // If no exception was thrown, check if the operation was successful
            // In this case, the test should not fail as there's no explicit @NonNull annotation
            assertEquals(null, entity.packageName);
            
        } catch (Exception e) {
            // If an exception was thrown (e.g., if there is implicit validation),
            // the test should pass
            assertTrue(true);
        }
    }

    /**
     * NEGATIVE TEST CASE 2: Create entity with null PermissionUsedCount
     */
    @Test
    public void testCreateWithNullPermissionUsedCount() {
        try {
            // Attempt to create entity with null PermissionUsedCount
            AiMinuteDataEntity entity = new AiMinuteDataEntity(
                    VALID_TIMESTAMP,
                    VALID_UID,
                    VALID_PACKAGE_NAME,
                    VALID_APP_CATEGORY,
                    VALID_APP_TRUST_LEVEL,
                    VALID_ACCESS_MINUTE,
                    VALID_ACCESS_HOUR,
                    VALID_ACCESS_DATE,
                    VALID_GRANTED_PERMISSIONS,
                    VALID_REQUESTED_PERMISSIONS,
                    VALID_IS_FOREGROUND,
                    null, // This might throw an exception if there is validation
                    sensorDuration
            );
            
            fail("Expected NullPointerException was not thrown for null PermissionUsedCount");
            
        } catch (NullPointerException e) {
            // This is expected, as PermissionUsedCount methods would be called on a null object
            assertTrue(true);
        } catch (Exception e) {
            // Other exceptions might be thrown depending on implementation
            assertTrue(true);
        }
    }

    /**
     * NEGATIVE TEST CASE 3: Create entity with null SensorDuration
     */
    @Test
    public void testCreateWithNullSensorDuration() {
        try {
            // Attempt to create entity with null SensorDuration
            AiMinuteDataEntity entity = new AiMinuteDataEntity(
                    VALID_TIMESTAMP,
                    VALID_UID,
                    VALID_PACKAGE_NAME,
                    VALID_APP_CATEGORY,
                    VALID_APP_TRUST_LEVEL,
                    VALID_ACCESS_MINUTE,
                    VALID_ACCESS_HOUR,
                    VALID_ACCESS_DATE,
                    VALID_GRANTED_PERMISSIONS,
                    VALID_REQUESTED_PERMISSIONS,
                    VALID_IS_FOREGROUND,
                    permissionUsedCount,
                    null // This might throw an exception if there is validation
            );
            
            fail("Expected NullPointerException was not thrown for null SensorDuration");
            
        } catch (NullPointerException e) {
            // This is expected, as SensorDuration methods would be called on a null object
            assertTrue(true);
        } catch (Exception e) {
            // Other exceptions might be thrown depending on implementation
            assertTrue(true);
        }
    }
}
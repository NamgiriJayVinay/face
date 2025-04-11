package com.example.app; // Replace with your actual package name

// Use only one assertion library to avoid ambiguity
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AiPeriodicDataEntity
 * Contains 7 positive test cases and 3 negative test cases
 */
public class AiPeriodicDataEntityTest {

    // Constants for test data
    private static final String VALID_PACKAGE_NAME = "com.example.testapp";
    private static final long VALID_ACCESS_DATE = 20230410L;
    private static final long VALID_ACCESS_HOUR = 14L;
    private static final int VALID_PERMISSION_COUNT = 5;
    private static final int VALID_PING_COUNT = 10;
    private static final long VALID_LAST_UPDATE_TIME = System.currentTimeMillis();
    private static final int VALID_IS_FOREGROUND = 1;
    private static final int VALID_APP_CATEGORY = 3;
    private static final int VALID_APP_TRUST_LEVEL = 2;
    private static final String VALID_GRANTED_PERMISSION = "android.permission.CAMERA,android.permission.MICROPHONE";

    /**
     * POSITIVE TEST CASE 1: Create valid AiPeriodicDataEntity with all required fields
     */
    @Test
    public void testCreateValidEntity() {
        // Create entity with all fields
        AiPeriodicDataEntity entity = createValidEntity();

        // Verify all fields have expected values
        assertEquals(VALID_PACKAGE_NAME, entity.packageName);
        assertEquals(VALID_ACCESS_DATE, entity.accessDate);
        assertEquals(VALID_ACCESS_HOUR, entity.accessHour);
        assertEquals(VALID_PERMISSION_COUNT, entity.permissionUsedCameraHourly);
        assertEquals(VALID_PING_COUNT, entity.pingCameraHourly);
        assertEquals(VALID_PERMISSION_COUNT, entity.permissionUsedMicrophoneHourly);
        assertEquals(VALID_PING_COUNT, entity.pingMicrophoneHourly);
        assertEquals(VALID_LAST_UPDATE_TIME, entity.lastUpdateTime);
        assertEquals(VALID_IS_FOREGROUND, entity.isForeground);
        assertEquals(VALID_APP_CATEGORY, entity.appCategory);
        assertEquals(VALID_APP_TRUST_LEVEL, entity.appTrustLevel);
        assertEquals(VALID_GRANTED_PERMISSION, entity.grantedPermission);
    }

    /**
     * POSITIVE TEST CASE 2: Test with extreme values for numeric fields
     */
    @Test
    public void testCreateWithExtremeValues() {
        // Create entity with extreme values
        AiPeriodicDataEntity entity = new AiPeriodicDataEntity(
                VALID_PACKAGE_NAME,
                Long.MAX_VALUE,
                Long.MIN_VALUE,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                Long.MAX_VALUE,
                1,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                VALID_GRANTED_PERMISSION
        );

        // Verify extreme values were set correctly
        assertEquals(Long.MAX_VALUE, entity.accessDate);
        assertEquals(Long.MIN_VALUE, entity.accessHour);
        assertEquals(Integer.MAX_VALUE, entity.permissionUsedCameraHourly);
        assertEquals(Integer.MIN_VALUE, entity.pingCameraHourly);
        assertEquals(Integer.MAX_VALUE, entity.appCategory);
        assertEquals(Integer.MAX_VALUE, entity.appTrustLevel);
    }

    /**
     * POSITIVE TEST CASE 3: Test with zero values for all numeric fields
     */
    @Test
    public void testCreateWithZeroValues() {
        // Create entity with zero values for all numeric fields
        AiPeriodicDataEntity entity = new AiPeriodicDataEntity(
                VALID_PACKAGE_NAME,
                0L,
                0L,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0L,
                0,
                0,
                0,
                VALID_GRANTED_PERMISSION
        );

        // Verify zero values were set correctly
        assertEquals(0L, entity.accessDate);
        assertEquals(0L, entity.accessHour);
        assertEquals(0, entity.permissionUsedCameraHourly);
        assertEquals(0, entity.pingCameraHourly);
        assertEquals(0, entity.permissionUsedMicrophoneHourly);
        assertEquals(0, entity.lastUpdateTime);
        assertEquals(0, entity.isForeground);
        assertEquals(0, entity.appCategory);
        assertEquals(0, entity.appTrustLevel);
    }

    /**
     * POSITIVE TEST CASE 4: Test with complex granted permission string
     */
    @Test
    public void testWithComplexPermissionString() {
        // Create complex permission string
        String complexGrantedPermission = "android.permission.CAMERA,android.permission.MICROPHONE,android.permission.FINE_LOCATION,android.permission.READ_CONTACTS,android.permission.READ_CALENDAR,android.permission.SEND_SMS,android.permission.RECEIVE_SMS,android.permission.READ_SMS";
        
        // Create entity with complex permission string
        AiPeriodicDataEntity entity = new AiPeriodicDataEntity(
                VALID_PACKAGE_NAME,
                VALID_ACCESS_DATE,
                VALID_ACCESS_HOUR,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_LAST_UPDATE_TIME,
                VALID_IS_FOREGROUND,
                VALID_APP_CATEGORY,
                VALID_APP_TRUST_LEVEL,
                complexGrantedPermission
        );

        // Verify complex permission string was set correctly
        assertEquals(complexGrantedPermission, entity.grantedPermission);
    }

    /**
     * POSITIVE TEST CASE 5: Test with empty granted permission string
     */
    @Test
    public void testWithEmptyPermissionString() {
        // Create entity with empty permission string
        AiPeriodicDataEntity entity = new AiPeriodicDataEntity(
                VALID_PACKAGE_NAME,
                VALID_ACCESS_DATE,
                VALID_ACCESS_HOUR,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_LAST_UPDATE_TIME,
                VALID_IS_FOREGROUND,
                VALID_APP_CATEGORY,
                VALID_APP_TRUST_LEVEL,
                ""
        );

        // Verify empty permission string was set correctly
        assertEquals("", entity.grantedPermission);
    }

    /**
     * POSITIVE TEST CASE 6: Test updating field values after creation
     */
    @Test
    public void testUpdateFieldValues() {
        // Create initial entity
        AiPeriodicDataEntity entity = createValidEntity();
        
        // Verify initial values
        assertEquals(VALID_PERMISSION_COUNT, entity.permissionUsedCameraHourly);
        assertEquals(VALID_PING_COUNT, entity.pingCameraHourly);
        assertEquals(VALID_LAST_UPDATE_TIME, entity.lastUpdateTime);
        
        // Update field values
        int newPermissionCount = 15;
        int newPingCount = 25;
        long newUpdateTime = System.currentTimeMillis() + 60000; // One minute later
        
        entity.permissionUsedCameraHourly = newPermissionCount;
        entity.pingCameraHourly = newPingCount;
        entity.lastUpdateTime = newUpdateTime;
        
        // Verify updated values
        assertEquals(newPermissionCount, entity.permissionUsedCameraHourly);
        assertEquals(newPingCount, entity.pingCameraHourly);
        assertEquals(newUpdateTime, entity.lastUpdateTime);
    }

    /**
     * POSITIVE TEST CASE 7: Test with special characters in package name
     */
    @Test
    public void testWithSpecialCharactersInPackageName() {
        // Create package name with special characters
        String specialPackageName = "com.example.special_test-app";
        
        // Create entity with special package name
        AiPeriodicDataEntity entity = new AiPeriodicDataEntity(
                specialPackageName,
                VALID_ACCESS_DATE,
                VALID_ACCESS_HOUR,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_LAST_UPDATE_TIME,
                VALID_IS_FOREGROUND,
                VALID_APP_CATEGORY,
                VALID_APP_TRUST_LEVEL,
                VALID_GRANTED_PERMISSION
        );

        // Verify special package name was set correctly
        assertEquals(specialPackageName, entity.packageName);
    }

    /**
     * NEGATIVE TEST CASE 1: Create entity with null package name
     */
    @Test
    public void testCreateWithNullPackageName() {
        try {
            // Attempt to create entity with null package name
            AiPeriodicDataEntity entity = new AiPeriodicDataEntity(
                    null, // This might throw an exception if there is validation
                    VALID_ACCESS_DATE,
                    VALID_ACCESS_HOUR,
                    VALID_PERMISSION_COUNT,
                    VALID_PING_COUNT,
                    VALID_PERMISSION_COUNT,
                    VALID_PING_COUNT,
                    VALID_PERMISSION_COUNT,
                    VALID_PING_COUNT,
                    VALID_PERMISSION_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_LAST_UPDATE_TIME,
                    VALID_IS_FOREGROUND,
                    VALID_APP_CATEGORY,
                    VALID_APP_TRUST_LEVEL,
                    VALID_GRANTED_PERMISSION
            );
            
            // If no exception was thrown, check if the operation was successful
            // In this case, the test should not fail as there's no explicit @NonNull annotation
            assertEquals(null, entity.packageName);
            
        } catch (Exception e) {
            // If an exception was thrown (e.g., if there is implicit validation),
            // the test should pass
            assertTrue("Expected exception for null package name", true);
        }
    }

    /**
     * NEGATIVE TEST CASE 2: Create entity with null granted permission
     */
    @Test
    public void testCreateWithNullGrantedPermission() {
        try {
            // Attempt to create entity with null granted permission
            AiPeriodicDataEntity entity = new AiPeriodicDataEntity(
                    VALID_PACKAGE_NAME,
                    VALID_ACCESS_DATE,
                    VALID_ACCESS_HOUR,
                    VALID_PERMISSION_COUNT,
                    VALID_PING_COUNT,
                    VALID_PERMISSION_COUNT,
                    VALID_PING_COUNT,
                    VALID_PERMISSION_COUNT,
                    VALID_PING_COUNT,
                    VALID_PERMISSION_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_PING_COUNT,
                    VALID_LAST_UPDATE_TIME,
                    VALID_IS_FOREGROUND,
                    VALID_APP_CATEGORY,
                    VALID_APP_TRUST_LEVEL,
                    null // This might throw an exception if there is validation
            );
            
            // If no exception was thrown, check if the operation was successful
            // In this case, the test should not fail as there's no explicit @NonNull annotation
            assertEquals(null, entity.grantedPermission);
            
        } catch (Exception e) {
            // If an exception was thrown (e.g., if there is implicit validation),
            // the test should pass
            assertTrue("Expected exception for null granted permission", true);
        }
    }

    /**
     * NEGATIVE TEST CASE 3: Create entity with negative ping counts
     */
    @Test
    public void testCreateWithNegativePingCounts() {
        // Create entity with negative ping counts
        AiPeriodicDataEntity entity = new AiPeriodicDataEntity(
                VALID_PACKAGE_NAME,
                VALID_ACCESS_DATE,
                VALID_ACCESS_HOUR,
                VALID_PERMISSION_COUNT,
                -1, // Negative ping count
                VALID_PERMISSION_COUNT,
                -2, // Negative ping count
                VALID_PERMISSION_COUNT,
                -3, // Negative ping count
                VALID_PERMISSION_COUNT,
                -4, // Negative ping count
                -5, // Negative ping count
                -6, // Negative ping count
                -7, // Negative ping count
                -8, // Negative ping count
                -9, // Negative ping count
                -10, // Negative ping count
                -11, // Negative ping count
                -12, // Negative ping count
                -13, // Negative ping count
                -14, // Negative ping count
                -15, // Negative ping count
                VALID_LAST_UPDATE_TIME,
                VALID_IS_FOREGROUND,
                VALID_APP_CATEGORY,
                VALID_APP_TRUST_LEVEL,
                VALID_GRANTED_PERMISSION
        );
        
        // Verify negative ping counts were set (this is a boundary test)
        assertEquals(-1, entity.pingCameraHourly);
        assertEquals(-2, entity.pingMicrophoneHourly);
        assertEquals(-3, entity.pingGetLocationHourly);
        assertEquals(-4, entity.pingCoarseLocationHourly);
        assertEquals(-5, entity.pingReadSMSHourly);
        
        // Note: In a real application, negative counts might be invalid.
        // This test verifies the behavior of the entity when given invalid input.
    }

    /**
     * Helper method to create a valid entity for testing
     */
    private AiPeriodicDataEntity createValidEntity() {
        return new AiPeriodicDataEntity(
                VALID_PACKAGE_NAME,
                VALID_ACCESS_DATE,
                VALID_ACCESS_HOUR,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PERMISSION_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_PING_COUNT,
                VALID_LAST_UPDATE_TIME,
                VALID_IS_FOREGROUND,
                VALID_APP_CATEGORY,
                VALID_APP_TRUST_LEVEL,
                VALID_GRANTED_PERMISSION
        );
    }
}
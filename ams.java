package com.example.app; // Replace with your actual package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AppInfo model
 * Contains 7 positive test cases and 3 negative test cases
 */
public class AppInfoTest {

    // Constants for test data
    private static final int VALID_UID = 10001;
    private static final int VALID_APP_CATEGORY_ID = 5;
    private static final int VALID_APP_TRUST_LEVEL = 2;
    
    private AppInfo appInfo;
    
    @Before
    public void setUp() {
        // Initialize with valid values before each test
        appInfo = new AppInfo(VALID_UID, VALID_APP_CATEGORY_ID, VALID_APP_TRUST_LEVEL);
    }

    /**
     * POSITIVE TEST CASE 1: Create AppInfo with valid values and verify getters return correct values
     */
    @Test
    public void testCreateWithValidValues() {
        // Verify all getters return expected values
        assertEquals("UID should match constructor value", VALID_UID, appInfo.getUid());
        assertEquals("App category ID should match constructor value", 
                    VALID_APP_CATEGORY_ID, appInfo.getAppCategoryId());
        assertEquals("App trust level should match constructor value", 
                    VALID_APP_TRUST_LEVEL, appInfo.getAppTrustLevel());
    }

    /**
     * POSITIVE TEST CASE 2: Test setUid method
     */
    @Test
    public void testSetUid() {
        // Set a new UID
        int newUid = 20002;
        appInfo.setUid(newUid);
        
        // Verify the UID was updated
        assertEquals("UID should be updated", newUid, appInfo.getUid());
    }

    /**
     * POSITIVE TEST CASE 3: Test setAppCategoryId method
     */
    @Test
    public void testSetAppCategoryId() {
        // Set a new app category ID
        int newCategoryId = 8;
        appInfo.setAppCategoryId(newCategoryId);
        
        // Verify the app category ID was updated
        assertEquals("App category ID should be updated", newCategoryId, appInfo.getAppCategoryId());
    }

    /**
     * POSITIVE TEST CASE 4: Test setAppTrustLevel method
     */
    @Test
    public void testSetAppTrustLevel() {
        // Set a new app trust level
        int newTrustLevel = 4;
        appInfo.setAppTrustLevel(newTrustLevel);
        
        // Verify the app trust level was updated
        assertEquals("App trust level should be updated", newTrustLevel, appInfo.getAppTrustLevel());
    }

    /**
     * POSITIVE TEST CASE 5: Test with extreme integer values
     */
    @Test
    public void testWithExtremeValues() {
        // Create an AppInfo with extreme integer values
        AppInfo extremeAppInfo = new AppInfo(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        );
        
        // Verify extreme values were set correctly
        assertEquals("Maximum integer UID should be set correctly", 
                    Integer.MAX_VALUE, extremeAppInfo.getUid());
        assertEquals("Maximum integer app category ID should be set correctly", 
                    Integer.MAX_VALUE, extremeAppInfo.getAppCategoryId());
        assertEquals("Maximum integer app trust level should be set correctly", 
                    Integer.MAX_VALUE, extremeAppInfo.getAppTrustLevel());
        
        // Update with minimum integer values
        extremeAppInfo.setUid(Integer.MIN_VALUE);
        extremeAppInfo.setAppCategoryId(Integer.MIN_VALUE);
        extremeAppInfo.setAppTrustLevel(Integer.MIN_VALUE);
        
        // Verify minimum values were set correctly
        assertEquals("Minimum integer UID should be set correctly", 
                    Integer.MIN_VALUE, extremeAppInfo.getUid());
        assertEquals("Minimum integer app category ID should be set correctly", 
                    Integer.MIN_VALUE, extremeAppInfo.getAppCategoryId());
        assertEquals("Minimum integer app trust level should be set correctly", 
                    Integer.MIN_VALUE, extremeAppInfo.getAppTrustLevel());
    }

    /**
     * POSITIVE TEST CASE 6: Test with zero values
     */
    @Test
    public void testWithZeroValues() {
        // Create an AppInfo with zero values
        AppInfo zeroAppInfo = new AppInfo(0, 0, 0);
        
        // Verify zero values were set correctly
        assertEquals("Zero UID should be set correctly", 0, zeroAppInfo.getUid());
        assertEquals("Zero app category ID should be set correctly", 0, zeroAppInfo.getAppCategoryId());
        assertEquals("Zero app trust level should be set correctly", 0, zeroAppInfo.getAppTrustLevel());
    }

    /**
     * POSITIVE TEST CASE 7: Test setting all fields at once
     */
    @Test
    public void testSettingAllFields() {
        // Create initial AppInfo
        AppInfo testAppInfo = new AppInfo(1000, 5, 2);
        
        // Set all fields to new values
        int newUid = 2000;
        int newCategoryId = 10;
        int newTrustLevel = 3;
        
        testAppInfo.setUid(newUid);
        testAppInfo.setAppCategoryId(newCategoryId);
        testAppInfo.setAppTrustLevel(newTrustLevel);
        
        // Verify all fields were updated
        assertEquals("UID should be updated", newUid, testAppInfo.getUid());
        assertEquals("App category ID should be updated", newCategoryId, testAppInfo.getAppCategoryId());
        assertEquals("App trust level should be updated", newTrustLevel, testAppInfo.getAppTrustLevel());
    }

    /**
     * NEGATIVE TEST CASE 1: Test with negative values
     */
    @Test
    public void testWithNegativeValues() {
        // Create an AppInfo with negative values
        AppInfo negativeAppInfo = new AppInfo(-100, -5, -1);
        
        // Verify negative values were set correctly
        // Note: This is a boundary test that checks if the class handles negative values,
        // which might be invalid in a real application context
        assertEquals("Negative UID should be set", -100, negativeAppInfo.getUid());
        assertEquals("Negative app category ID should be set", -5, negativeAppInfo.getAppCategoryId());
        assertEquals("Negative app trust level should be set", -1, negativeAppInfo.getAppTrustLevel());
    }

    /**
     * NEGATIVE TEST CASE 2: Test that different objects with same values are not equal
     * Note: This assumes AppInfo doesn't override equals() method
     */
    @Test
    public void testObjectEquality() {
        // Create a new AppInfo with the same values
        AppInfo sameValuesAppInfo = new AppInfo(VALID_UID, VALID_APP_CATEGORY_ID, VALID_APP_TRUST_LEVEL);
        
        // Verify they are different objects even with same values
        // This test would need to be updated if equals() is implemented
        assertNotEquals("Different objects with same values should not be equal", 
                       appInfo, sameValuesAppInfo);
    }

    /**
     * NEGATIVE TEST CASE 3: Test boundary values for app trust level
     */
    @Test
    public void testBoundaryValuesTrustLevel() {
        // Test with a very large trust level
        // (Assuming there might be some business rules for valid trust levels)
        AppInfo largeValueAppInfo = new AppInfo(VALID_UID, VALID_APP_CATEGORY_ID, 1000);
        
        // Verify large value was set (this is a boundary test, not necessarily a failure)
        assertEquals("Large trust level should be set", 1000, largeValueAppInfo.getAppTrustLevel());
        
        // Test with a negative trust level
        AppInfo negativeValueAppInfo = new AppInfo(VALID_UID, VALID_APP_CATEGORY_ID, -10);
        
        // Verify negative value was set (this is a boundary test, not necessarily a failure)
        assertEquals("Negative trust level should be set", -10, negativeValueAppInfo.getAppTrustLevel());
    }
}
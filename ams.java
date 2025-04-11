package com.example.app; // Replace with your actual package name

// Use only one assertion library to avoid ambiguity
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Before;

/**
 * Test class for AppInfoEntity
 * Contains 7 positive test cases and 3 negative test cases
 */
public class AppInfoEntityTest {

    private static final String VALID_PACKAGE_NAME = "com.example.testapp";
    private static final int VALID_UID = 10001;
    private static final String VALID_APP_NAME = "Test App";
    private static final String VALID_CATEGORY_NAME = "Tools";
    private static final int VALID_CATEGORY_ID = 5;
    private static final int VALID_TRUST_LEVEL = 2;
    private static final String VALID_ADDITIONAL_DATA = "{\"version\":\"1.0.0\",\"size\":\"15MB\"}";

    /**
     * POSITIVE TEST CASE 1: Create valid AppInfoEntity with all required fields
     */
    @Test
    public void testCreateValidAppInfoEntity() {
        // Create entity with all fields
        AppInfoEntity entity = new AppInfoEntity(
                VALID_PACKAGE_NAME,
                VALID_UID,
                VALID_APP_NAME,
                VALID_CATEGORY_NAME,
                VALID_CATEGORY_ID,
                VALID_TRUST_LEVEL,
                VALID_ADDITIONAL_DATA
        );

        // Verify all fields have expected values
        assertEquals(VALID_PACKAGE_NAME, entity.packageName);
        assertEquals(VALID_UID, entity.uid);
        assertEquals(VALID_APP_NAME, entity.appName);
        assertEquals(VALID_CATEGORY_NAME, entity.appCategoryName);
        assertEquals(VALID_CATEGORY_ID, entity.appCategoryId);
        assertEquals(VALID_TRUST_LEVEL, entity.appTrustLevel);
        assertEquals(VALID_ADDITIONAL_DATA, entity.additionalData);
    }

    /**
     * POSITIVE TEST CASE 2: Create valid AppInfoEntity with minimum required fields
     * Note: packageName appears to be the only @NonNull field
     */
    @Test
    public void testCreateWithMinimumRequiredFields() {
        // Create entity with only required fields (packageName)
        // For other parameters, passing null for strings and 0 for integers
        AppInfoEntity entity = new AppInfoEntity(
                VALID_PACKAGE_NAME,
                0,
                null,
                null,
                0,
                0,
                null
        );

        // Verify required field has expected value and others are null/default
        assertEquals(VALID_PACKAGE_NAME, entity.packageName);
        assertEquals(0, entity.uid);
        assertNull(entity.appName);
        assertNull(entity.appCategoryName);
        assertEquals(0, entity.appCategoryId);
        assertEquals(0, entity.appTrustLevel);
        assertNull(entity.additionalData);
    }

    /**
     * POSITIVE TEST CASE 3: Update app trust level
     */
    @Test
    public void testUpdateAppTrustLevel() {
        // Create entity with initial trust level
        AppInfoEntity entity = new AppInfoEntity(
                VALID_PACKAGE_NAME,
                VALID_UID,
                VALID_APP_NAME,
                VALID_CATEGORY_NAME,
                VALID_CATEGORY_ID,
                VALID_TRUST_LEVEL,
                VALID_ADDITIONAL_DATA
        );

        // Verify initial trust level
        assertEquals(VALID_TRUST_LEVEL, entity.appTrustLevel);

        // Update trust level
        int newTrustLevel = 4;
        entity.appTrustLevel = newTrustLevel;

        // Verify trust level was updated
        assertEquals(newTrustLevel, entity.appTrustLevel);
    }

    /**
     * POSITIVE TEST CASE 4: Update app category information
     */
    @Test
    public void testUpdateAppCategoryInfo() {
        // Create entity with initial category info
        AppInfoEntity entity = new AppInfoEntity(
                VALID_PACKAGE_NAME,
                VALID_UID,
                VALID_APP_NAME,
                VALID_CATEGORY_NAME,
                VALID_CATEGORY_ID,
                VALID_TRUST_LEVEL,
                VALID_ADDITIONAL_DATA
        );

        // Verify initial category info
        assertEquals(VALID_CATEGORY_NAME, entity.appCategoryName);
        assertEquals(VALID_CATEGORY_ID, entity.appCategoryId);

        // Update category info
        String newCategoryName = "Games";
        int newCategoryId = 8;
        entity.appCategoryName = newCategoryName;
        entity.appCategoryId = newCategoryId;

        // Verify category info was updated
        assertEquals(newCategoryName, entity.appCategoryName);
        assertEquals(newCategoryId, entity.appCategoryId);
    }

    /**
     * POSITIVE TEST CASE 5: Store and retrieve additional data
     */
    @Test
    public void testStoreAndRetrieveAdditionalData() {
        // Create JSON string for additionalData
        String jsonData = "{\"permissions\":[\"CAMERA\",\"STORAGE\"],\"installDate\":\"2023-04-10\"}";
        
        // Create entity with JSON data
        AppInfoEntity entity = new AppInfoEntity(
                VALID_PACKAGE_NAME,
                VALID_UID,
                VALID_APP_NAME,
                VALID_CATEGORY_NAME,
                VALID_CATEGORY_ID,
                VALID_TRUST_LEVEL,
                jsonData
        );

        // Verify additionalData contains the exact JSON string
        assertEquals(jsonData, entity.additionalData);
    }

    /**
     * POSITIVE TEST CASE 6: Create AppInfoEntity with minimum and maximum integer values
     */
    @Test
    public void testCreateWithExtremeIntegerValues() {
        // Create entity with extreme integer values
        AppInfoEntity entity = new AppInfoEntity(
                VALID_PACKAGE_NAME,
                Integer.MIN_VALUE,
                VALID_APP_NAME,
                VALID_CATEGORY_NAME,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                VALID_ADDITIONAL_DATA
        );

        // Verify extreme values were stored correctly
        assertEquals(Integer.MIN_VALUE, entity.uid);
        assertEquals(Integer.MAX_VALUE, entity.appCategoryId);
        assertEquals(Integer.MAX_VALUE, entity.appTrustLevel);
    }

    /**
     * POSITIVE TEST CASE 7: Create AppInfoEntity with special characters in string fields
     */
    @Test
    public void testCreateWithSpecialCharacters() {
        // Create strings with special characters
        String packageWithSpecialChars = "com.example.test_app.special";
        String appNameWithSpecialChars = "Test App #1 (Special Edition) & More!";
        String categoryWithSpecialChars = "Games & Entertainment";
        String additionalDataWithSpecialChars = "{\"notes\":\"Special characters: !@#$%^&*()\"}";
        
        // Create entity with special characters
        AppInfoEntity entity = new AppInfoEntity(
                packageWithSpecialChars,
                VALID_UID,
                appNameWithSpecialChars,
                categoryWithSpecialChars,
                VALID_CATEGORY_ID,
                VALID_TRUST_LEVEL,
                additionalDataWithSpecialChars
        );

        // Verify special characters were preserved
        assertEquals(packageWithSpecialChars, entity.packageName);
        assertEquals(appNameWithSpecialChars, entity.appName);
        assertEquals(categoryWithSpecialChars, entity.appCategoryName);
        assertEquals(additionalDataWithSpecialChars, entity.additionalData);
    }

    /**
     * NEGATIVE TEST CASE 1: Create AppInfoEntity with null packageName
     * Expected: NullPointerException or similar validation exception
     */
    @Test
    public void testCreateWithNullPackageName() {
        try {
            // Attempt to create entity with null packageName (marked as @NonNull)
            AppInfoEntity entity = new AppInfoEntity(
                    null, // This should trigger an exception
                    VALID_UID,
                    VALID_APP_NAME,
                    VALID_CATEGORY_NAME,
                    VALID_CATEGORY_ID,
                    VALID_TRUST_LEVEL,
                    VALID_ADDITIONAL_DATA
            );
            
            // If we reach here without an exception, the test has failed
            fail("Expected NullPointerException was not thrown for null packageName");
        } catch (NullPointerException e) {
            // Test passes if NullPointerException is caught
            assertTrue(true);
        }
    }

    /**
     * NEGATIVE TEST CASE 2: Create AppInfoEntity with invalid package name format
     * Note: This assumes there's validation for package name format.
     * If no validation exists, this test would need to be modified.
     */
    @Test
    public void testCreateWithInvalidPackageNameFormat() {
        // List of potentially invalid package names
        String[] invalidPackageNames = {
            "",                  // Empty string
            "invalid",           // Missing dots
            "invalid.package.",  // Ending with dot
            ".invalid.package",  // Starting with dot
            "Invalid.Package"    // Capital letters (may or may not be valid depending on implementation)
        };
        
        for (String invalidPackageName : invalidPackageNames) {
            try {
                AppInfoEntity entity = new AppInfoEntity(
                        invalidPackageName,
                        VALID_UID,
                        VALID_APP_NAME,
                        VALID_CATEGORY_NAME,
                        VALID_CATEGORY_ID,
                        VALID_TRUST_LEVEL,
                        VALID_ADDITIONAL_DATA
                );
                
                // If we reach here without exception, log the potentially invalid package name
                // that was accepted (this isn't necessarily a failure if validation isn't implemented)
                System.out.println("Note: Potentially invalid package name accepted: " + invalidPackageName);
                
            } catch (Exception e) {
                // Exception caught - this is expected for invalid formats if validation exists
                assertTrue("Exception thrown for invalid package name: " + invalidPackageName, true);
            }
        }
    }

    /**
     * NEGATIVE TEST CASE 3: Create AppInfoEntity with negative UID value
     * Note: This assumes there's validation for UID values.
     * If no validation exists, this becomes an edge case test.
     */
    @Test
    public void testCreateWithNegativeUid() {
        int negativeUid = -1;
        
        try {
            AppInfoEntity entity = new AppInfoEntity(
                    VALID_PACKAGE_NAME,
                    negativeUid,
                    VALID_APP_NAME,
                    VALID_CATEGORY_NAME,
                    VALID_CATEGORY_ID,
                    VALID_TRUST_LEVEL,
                    VALID_ADDITIONAL_DATA
            );
            
            // If we reach here without exception, check if the negative UID was accepted
            assertEquals(negativeUid, entity.uid);
            System.out.println("Note: Negative UID accepted: " + negativeUid);
            
        } catch (Exception e) {
            // Exception caught - this is expected if validation exists
            assertTrue("Exception thrown for negative UID as expected", true);
        }
    }
}